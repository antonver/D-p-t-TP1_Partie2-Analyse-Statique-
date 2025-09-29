package graphe;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

// Le contrôleur pour l'interface du graphe, version simplifiée.
public class ControleurGraphe {

    @FXML private BorderPane mainPane;
    @FXML private TextField cheminDossierField;
    @FXML private Button analyserButton;
    @FXML private ProgressIndicator progressIndicator;

    private File dossierSelectionne;

    // Le bouton pour choisir un dossier de projet
    @FXML
    private void handleChoisirDossierClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sélectionner le projet Java");
        File dossier = directoryChooser.showDialog(null);
        if (dossier != null) {
            dossierSelectionne = dossier;
            cheminDossierField.setText(dossier.getAbsolutePath());
            analyserButton.setDisable(false);
        }
    }

    // Le bouton pour lancer l'analyse et dessiner le graphe
    @FXML
    private void handleAnalyserClick() {
        if (dossierSelectionne == null) return;

        Task<SwingNode> task = new Task<>() {
            @Override
            protected SwingNode call() throws Exception {
                Map<String, Set<String>> grapheAppel = analyserProjet(dossierSelectionne);
                Graph graph = creerGraphe(grapheAppel);
                SwingViewer viewer = new SwingViewer(graph, SwingViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
                viewer.enableAutoLayout();
                View view = viewer.addDefaultView(false); // false pour l'avoir en tant que JPanel

                SwingNode swingNode = new SwingNode();
                swingNode.setContent((javax.swing.JComponent) view);

                return swingNode;
            }
        };

        task.setOnSucceeded(event -> {
            SwingNode swingNode = task.getValue();
            mainPane.setCenter(swingNode);
            progressIndicator.setVisible(false);
            analyserButton.setDisable(false);
        });

        task.setOnFailed(event -> {
            task.getException().printStackTrace();
            progressIndicator.setVisible(false);
            analyserButton.setDisable(false);
        });

        progressIndicator.setVisible(true);
        analyserButton.setDisable(true);
        new Thread(task).start();
    }

    // La méthode qui analyse le code source
    private Map<String, Set<String>> analyserProjet(File folder) throws IOException {
        String[] sourcepathEntries = { folder.getAbsolutePath() };
        Collection<File> javaFiles = FileUtils.listFiles(folder, new String[]{"java"}, true);
        VisiteurGrapheAppel visiteur = new VisiteurGrapheAppel();
        for (File file : javaFiles) {
            String sourceCode = FileUtils.readFileToString(file, "UTF-8");
            ASTParser parser = ASTParser.newParser(AST.JLS11);
            parser.setResolveBindings(true);
            parser.setBindingsRecovery(true);
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            Map<String, String> options = JavaCore.getOptions();
            JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
            parser.setCompilerOptions(options);
            parser.setEnvironment(null, sourcepathEntries, new String[] { "UTF-8" }, true);
            parser.setUnitName(file.getName());
            parser.setSource(sourceCode.toCharArray());
            CompilationUnit cu = (CompilationUnit) parser.createAST(null);
            cu.accept(visiteur);
        }
        return visiteur.getGrapheAppel();
    }

    // La méthode qui crée l'objet Graph de GraphStream
    private Graph creerGraphe(Map<String, Set<String>> grapheAppel) {
        Graph graph = new SingleGraph("Graphe d'Appel");
        // Возвращаем старый, нейтральный стиль
        graph.setAttribute("ui.stylesheet",
                "node { text-size: 12; text-alignment: under; fill-color: #EEE; stroke-mode: plain; stroke-color: #555; padding: 5px, 4px; }" +
                        "edge { shape: cubic-curve; arrow-size: 10px, 6px; fill-color: #444; }"
        );
        graph.setStrict(false);
        graph.setAutoCreate(true);

        for (Map.Entry<String, Set<String>> entry : grapheAppel.entrySet()) {
            String caller = entry.getKey();
            graph.addNode(caller).setAttribute("ui.label", getNomCourt(caller));
            for (String callee : entry.getValue()) {
                graph.addNode(callee).setAttribute("ui.label", getNomCourt(callee));
                graph.addEdge(caller + "->" + callee, caller, callee, true);
            }
        }
        return graph;
    }

    private static String getNomCourt(String nomComplet) {
        String[] parties = nomComplet.split("\\.");
        if (parties.length > 1) {
            return parties[parties.length - 2] + "." + parties[parties.length - 1];
        }
        return nomComplet;
    }
}