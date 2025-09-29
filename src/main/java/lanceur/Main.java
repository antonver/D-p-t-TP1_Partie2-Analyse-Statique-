package lanceur;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import analyseur.AnalyseurAST;
import analyseur.InfosProjet;

// C'est le programme principal qui va tout lancer.
public class Main {

    public static void main(String[] args) throws IOException {
        // On vérifie qu'on a bien donné un chemin de dossier en argument.
        if (args.length < 1) {
            System.err.println("S'il vous plaît, donnez le chemin du projet à analyser.");
            return;
        }

        final File folder = new File(args[0]);
        if (!folder.isDirectory()) {
            System.err.println("Le chemin n'est pas un dossier valide.");
            return;
        }

        // On va chercher tous les fichiers .java dans le dossier et ses sous-dossiers.
        Collection<File> javaFiles = FileUtils.listFiles(folder, new String[]{"java"}, true);
        System.out.println("Analyse de " + javaFiles.size() + " fichiers java...");

        InfosProjet infosProjet = new InfosProjet();

        // Pour chaque fichier, on va parser le code.
        for (File file : javaFiles) {
            String sourceCode = FileUtils.readFileToString(file, "UTF-8");

            ASTParser parser = ASTParser.newParser(AST.JLS11); // On peut choisir la version de Java
            parser.setKind(ASTParser.K_COMPILATION_UNIT);

            // On configure le parser.
            Map<String, String> options = JavaCore.getOptions();
            JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
            parser.setCompilerOptions(options);

            parser.setSource(sourceCode.toCharArray());

            CompilationUnit cu = (CompilationUnit) parser.createAST(null);

            // On lance notre visiteur sur l'AST du fichier.
            AnalyseurAST analyseur = new AnalyseurAST(infosProjet, cu);
            cu.accept(analyseur);
        }

        // C'est fini! Maintenant on affiche les résultats.
        afficherResultats(infosProjet);
    }

    private static void afficherResultats(InfosProjet infos) {
        System.out.println("\n--- RÉSULTATS DE L'ANALYSE ---");
        System.out.println("1. Nombre de classes : " + infos.getNombreClasses());
        System.out.println("2. Nombre de lignes de code : " + infos.getTotalLignesDeCode());
        System.out.println("3. Nombre total de méthodes : " + infos.getTotalMethodes());
        System.out.println("4. Nombre total de packages : " + infos.getTotalPackages());
        System.out.printf("5. Nombre moyen de méthodes par classe : %.2f\n", infos.getMoyenneMethodesParClasse());
        System.out.printf("6. Nombre moyen de lignes de code par méthode : %.2f\n", infos.getMoyenneLignesParMethode());
        System.out.printf("7. Nombre moyen d’attributs par classe : %.2f\n", infos.getMoyenneAttributsParClasse());

        System.out.println("\n8. Les 10% des classes avec le plus de méthodes :");
        infos.getTop10PercentClassesParMethodes().forEach(c ->
                System.out.println("   - " + c.getNom() + " (" + c.getNombreMethodes() + " méthodes)"));

        System.out.println("\n9. Les 10% des classes avec le plus d’attributs :");
        infos.getTop10PercentClassesParAttributs().forEach(c ->
                System.out.println("   - " + c.getNom() + " (" + c.getNombreAttributs() + " attributs)"));

        System.out.println("\n10. Classes dans les deux catégories précédentes :");
        infos.getClassesDansLesDeuxTops().forEach(c -> System.out.println("   - " + c.getNom()));

        int x = 10; // On peut demander cette valeur à l'utilisateur
        System.out.println("\n11. Classes avec plus de " + x + " méthodes :");
        infos.getClassesAvecPlusDeXMethodes(x).forEach(c ->
                System.out.println("   - " + c.getNom() + " (" + c.getNombreMethodes() + " méthodes)"));

        System.out.println("\n12. Les 10% des méthodes les plus longues (par classe) :");
        infos.getTop10PercentMethodesParLignes().forEach((nomClasse, methodes) -> {
            if (!methodes.isEmpty()) {
                System.out.println("   - Dans la classe " + nomClasse + " :");
                methodes.forEach(m -> System.out.println("     -> une méthode avec " + m.getLignesDeCode() + " lignes"));
            }
        });

        System.out.println("\n13. Nombre maximal de paramètres dans une méthode : " + infos.getMaxParametres());
        System.out.println("--- FIN DE L'ANALYSE ---");
    }
}