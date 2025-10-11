package graphe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

// Le programme principal pour créer et AFFICHER EN TEXTE le graphe d'appel.
public class MainGraphe {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Donnez le chemin du projet à analyser svp.");
            return;
        }

        final File folder = new File(args[0]);
        String[] sourcepathEntries = { folder.getAbsolutePath() };

        System.out.println("Analyse du projet: " + folder.getAbsolutePath());

        Collection<File> javaFiles = FileUtils.listFiles(folder, new String[]{"java"}, true);

        VisiteurGrapheAppel visiteur = new VisiteurGrapheAppel();

        for (File file : javaFiles) {
            System.out.println(" -> Analyse du fichier: " + file.getName());
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

        // On appelle la méthode qui affiche le graphe DANS LA CONSOLE.
        afficherGrapheConsole(visiteur.getGrapheAppel());
    }

    // Cette méthode affiche le graphe en format texte.
    private static void afficherGrapheConsole(Map<String, Set<String>> grapheAppel) {
        System.out.println("\n--- GRAPHE D'APPEL (Version Texte) ---");

        if (grapheAppel.isEmpty()) {
            System.out.println("Le graphe d'appel est vide ou aucune méthode n'a été trouvée.");
            return;
        }

        // Pour un affichage plus propre, on trie les méthodes par ordre alphabétique
        List<String> methodesAppelantes = new ArrayList<>(grapheAppel.keySet());
        Collections.sort(methodesAppelantes);

        for (String appelante : methodesAppelantes) {
            Set<String> appelees = grapheAppel.get(appelante);
            if (appelees != null && !appelees.isEmpty()) {
                // On affiche la méthode qui fait l'appel
                System.out.println("\n" + getNomCourt(appelante) + " appelle :");

                // On trie aussi les méthodes appelées pour la lisibilité
                List<String> appeleesTriees = new ArrayList<>(appelees);
                Collections.sort(appeleesTriees);

                for (String appelee : appeleesTriees) {
                    // On affiche chaque méthode qu'elle appelle, avec un décalage
                    System.out.println("  -> " + getNomCourt(appelee));
                }
            }
        }
        System.out.println("\n--- FIN DU GRAPHE D'APPEL ---");
    }

    // Petite fonction pour rendre les noms de méthodes plus lisibles
    private static String getNomCourt(String nomComplet) {
        String[] parties = nomComplet.split("\\.");
        if (parties.length > 1) {
            return parties[parties.length - 2] + "." + parties[parties.length - 1];
        }
        return nomComplet;
    }
}