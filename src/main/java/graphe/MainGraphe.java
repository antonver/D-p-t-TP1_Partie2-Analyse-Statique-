package graphe;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

// Le programme principal pour créer et afficher le graphe d'appel.
public class MainGraphe {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Donnez le chemin du projet à analyser svp.");
            return;
        }

        final File folder = new File(args[0]);
        String[] sourcepathEntries = { folder.getAbsolutePath() }; // Important pour le binding

        System.out.println("Analyse du projet: " + folder.getAbsolutePath());

        // On cherche tous les fichiers .java
        Collection<File> javaFiles = FileUtils.listFiles(folder, new String[]{"java"}, true);

        VisiteurGrapheAppel visiteur = new VisiteurGrapheAppel();

        for (File file : javaFiles) {
            String sourceCode = FileUtils.readFileToString(file, "UTF-8");

            ASTParser parser = ASTParser.newParser(AST.JLS11);
            parser.setKind(ASTParser.K_COMPILATION_UNIT);

            // Configuration du parser. C'est TRES important pour que le "binding" fonctionne.
            parser.setResolveBindings(true);
            parser.setBindingsRecovery(true); // On essaie de récupérer même s'il y a des erreurs

            Map<String, String> options = JavaCore.getOptions();
            JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
            parser.setCompilerOptions(options);

            parser.setEnvironment(null, sourcepathEntries, new String[] { "UTF-8" }, true);
            parser.setUnitName(file.getName()); // Donner un nom au fichier aide le parser
            parser.setSource(sourceCode.toCharArray());

            CompilationUnit cu = (CompilationUnit) parser.createAST(null);

            // On lance le visiteur
            cu.accept(visiteur);
        }

        // L'analyse est finie, on affiche le graphe !
        dessinerGraphe(visiteur.getGrapheAppel());
    }

    public static void dessinerGraphe(Map<String, Set<String>> grapheAppel) {
        // On dit à GraphStream d'utiliser l'interface Swing ou JavaFX
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Graphe d'Appel");

        // Ajout du style pour que ça soit plus joli
        graph.setAttribute("ui.stylesheet",
                "node { text-size: 12; text-alignment: under; fill-color: #EEE; stroke-mode: plain; stroke-color: #555; padding: 5px, 4px; }" +
                        "edge { shape: cubic-curve; arrow-size: 10px, 6px; fill-color: #444; }"
        );

        // 1. On ajoute tous les noeuds (les méthodes)
        grapheAppel.keySet().forEach(methode -> {
            graph.addNode(methode).setAttribute("ui.label", getNomCourt(methode));
        });
        grapheAppel.values().forEach(callees -> {
            callees.forEach(methode -> {
                if (graph.getNode(methode) == null) {
                    graph.addNode(methode).setAttribute("ui.label", getNomCourt(methode));
                }
            });
        });

        // 2. On ajoute tous les arcs (les appels)
        int edgeId = 0;
        for (String caller : grapheAppel.keySet()) {
            for (String callee : grapheAppel.get(caller)) {
                if (graph.getNode(caller) != null && graph.getNode(callee) != null) {
                    graph.addEdge(String.valueOf(edgeId++), caller, callee, true);
                }
            }
        }

        // On affiche la fenêtre avec le graphe
        graph.display();
    }

    // Petite fonction pour rendre les noms de méthodes plus lisibles sur le graphe
    private static String getNomCourt(String nomComplet) {
        String[] parties = nomComplet.split("\\.");
        if (parties.length > 1) {
            return parties[parties.length - 2] + "." + parties[parties.length - 1];
        }
        return nomComplet;
    }
}