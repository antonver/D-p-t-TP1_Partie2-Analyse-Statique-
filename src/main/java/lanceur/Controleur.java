package lanceur;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import analyseur.AnalyseurAST;
import analyseur.InfosClasse;
import analyseur.InfosProjet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

// Le contrôleur pour notre interface graphique.
// Il fait le lien entre les boutons (la vue) et le code d'analyse (le modèle).
public class Controleur {

    @FXML private TextField cheminDossierField;
    @FXML private TextArea resultatsArea;
    @FXML private Button analyserButton;

    private File dossierSelectionne;

    // Cette méthode est appelée quand on clique sur le bouton "Choisir un dossier...".
    @FXML
    private void handleChoisirDossierClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sélectionner le projet Java à analyser");
        File dossier = directoryChooser.showDialog(null);
        if (dossier != null) {
            dossierSelectionne = dossier;
            cheminDossierField.setText(dossier.getAbsolutePath());
            analyserButton.setDisable(false); // On active le bouton d'analyse
        }
    }

    // Cette méthode est appelée quand on clique sur "Analyser le projet".
    @FXML
    private void handleAnalyserClick() {
        if (dossierSelectionne == null) {
            resultatsArea.setText("Veuillez d'abord sélectionner un dossier.");
            return;
        }

        resultatsArea.setText("Analyse en cours, veuillez patienter...");

        // On lance l'analyse. Pour une grosse application, il faudrait le faire
        // dans un autre thread pour ne pas bloquer l'interface. Mais pour un projet étudiant, c'est ok.
        try {
            InfosProjet infos = analyserProjet(dossierSelectionne);
            afficherResultatsDansTextArea(infos);
        } catch (IOException e) {
            // Si il y a une erreur, on l'affiche.
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            resultatsArea.setText("Une erreur est survenue :\n" + sw.toString());
        }
    }

    // C'est la même logique que dans la classe Main, mais adaptée pour le contrôleur.
    private InfosProjet analyserProjet(File folder) throws IOException {
        Collection<File> javaFiles = FileUtils.listFiles(folder, new String[]{"java"}, true);
        InfosProjet infosProjet = new InfosProjet();

        for (File file : javaFiles) {
            String sourceCode = FileUtils.readFileToString(file, "UTF-8");
            ASTParser parser = ASTParser.newParser(AST.JLS11);
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            Map<String, String> options = JavaCore.getOptions();
            JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
            parser.setCompilerOptions(options);
            parser.setSource(sourceCode.toCharArray());
            CompilationUnit cu = (CompilationUnit) parser.createAST(null);
            AnalyseurAST analyseur = new AnalyseurAST(infosProjet, cu);
            cu.accept(analyseur);
        }
        return infosProjet;
    }

    // Formatte les résultats pour les afficher joliment dans la zone de texte.
    private void afficherResultatsDansTextArea(InfosProjet infos) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- RÉSULTATS DE L'ANALYSE ---\n");
        sb.append("1. Nombre de classes : ").append(infos.getNombreClasses()).append("\n");
        sb.append("2. Nombre de lignes de code : ").append(infos.getTotalLignesDeCode()).append("\n");
        sb.append("3. Nombre total de méthodes : ").append(infos.getTotalMethodes()).append("\n");
        sb.append("4. Nombre total de packages : ").append(infos.getTotalPackages()).append("\n");
        sb.append(String.format("5. Nombre moyen de méthodes par classe : %.2f\n", infos.getMoyenneMethodesParClasse()));
        sb.append(String.format("6. Nombre moyen de lignes de code par méthode : %.2f\n", infos.getMoyenneLignesParMethode()));
        sb.append(String.format("7. Nombre moyen d’attributs par classe : %.2f\n", infos.getMoyenneAttributsParClasse()));

        sb.append("\n8. Les 10% des classes avec le plus de méthodes :\n");
        infos.getTop10PercentClassesParMethodes().forEach(c ->
                sb.append("   - ").append(c.getNom()).append(" (").append(c.getNombreMethodes()).append(" méthodes)\n"));

        sb.append("\n9. Les 10% des classes avec le plus d’attributs :\n");
        infos.getTop10PercentClassesParAttributs().forEach(c ->
                sb.append("   - ").append(c.getNom()).append(" (").append(c.getNombreAttributs()).append(" attributs)\n"));

        sb.append("\n10. Classes dans les deux catégories précédentes :\n");
        infos.getClassesDansLesDeuxTops().forEach(c -> sb.append("   - ").append(c.getNom()).append("\n"));

        int x = 10;
        sb.append("\n11. Classes avec plus de ").append(x).append(" méthodes :\n");
        infos.getClassesAvecPlusDeXMethodes(x).forEach(c ->
                sb.append("   - ").append(c.getNom()).append(" (").append(c.getNombreMethodes()).append(" méthodes)\n"));

        sb.append("\n12. Les 10% des méthodes les plus longues (par classe) :\n");
        infos.getTop10PercentMethodesParLignes().forEach((nomClasse, methodes) -> {
            if (!methodes.isEmpty()) {
                sb.append("   - Dans la classe ").append(nomClasse).append(" :\n");
                methodes.forEach(m -> sb.append("     -> une méthode avec ").append(m.getLignesDeCode()).append(" lignes\n"));
            }
        });

        sb.append("\n13. Nombre maximal de paramètres dans une méthode : ").append(infos.getMaxParametres()).append("\n");
        sb.append("--- FIN DE L'ANALYSE ---");

        resultatsArea.setText(sb.toString());
    }
}