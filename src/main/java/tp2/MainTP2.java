package tp2;

import couplage.GrapheCouplage;
import clustering.ClusteringHierarchique;
import clustering.Cluster;
import clustering.IdentificateurModules;
import graphe.VisiteurGrapheAppel;

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

/**
 * Classe principale pour le TP2 -Compréhension des programmes
 * Démontre l'utilisation des trois exercices:
 * 1. Graphe de couplage
 * 2. Clustering hiérarchique et identification de modules
 * 3. Analyse avec Spoon
 */
public class MainTP2 {
    
    public static void main(String[] args) throws IOException {
        System.out.println("===============================================");
        System.out.println("       TP2 - Compréhension des programmes");
        System.out.println("===============================================\n");
        
        // Déterminer le chemin du projet à analyser
        String cheminProjet;
        if (args.length > 0) {
            cheminProjet = args[0];
        } else {
            // Par défaut, on analyse le projet exemple dans src/main/java/exemple
            cheminProjet = "src/main/java/exemple";
            System.out.println("Utilisation du projet exemple par défaut.");
            System.out.println("Pour analyser un autre projet: java MainTP2 /chemin/vers/projet\n");
        }
        
        File folder = new File(cheminProjet);
        if (!folder.exists()) {
            System.err.println("ERREUR: Le chemin n'existe pas: " + cheminProjet);
            return;
        }
        
        // ===================================
        // ÉTAPE 1: Construire le graphe d'appel (TP1)
        // ===================================
        System.out.println("\n--- ÉTAPE 1: Construction du graphe d'appel ---");
        Map<String, Set<String>> grapheAppel = construireGrapheAppel(folder);
        
        if (grapheAppel.isEmpty()) {
            System.err.println("ERREUR: Le graphe d'appel est vide. Vérifiez le chemin du projet.");
            return;
        }
        
        System.out.println("Graphe d'appel construit avec succès!");
        System.out.println("Nombre de méthodes appelantes: " + grapheAppel.size());
        
        // ===================================
        // EXERCICE 1: Graphe de couplage
        // ===================================
        System.out.println("\n\n===============================================");
        System.out.println("        EXERCICE 1: GRAPHE DE COUPLAGE");
        System.out.println("===============================================");
        
        GrapheCouplage grapheCouplage = new GrapheCouplage(grapheAppel);
        System.out.println("\nNombre de classes détectées: " + grapheCouplage.getClasses().size());
        System.out.println("Nombre total de relations: " + grapheCouplage.getTotalRelations());
        
        // Afficher la matrice de couplage
        grapheCouplage.afficherMatriceCouplage();
        
        // ===================================
        // EXERCICE 2: Clustering et Modules
        // ===================================
        System.out.println("\n\n===============================================");
        System.out.println("   EXERCICE 2: CLUSTERING ET MODULES");
        System.out.println("===============================================");
        
        // Vérifier qu'il y a assez de classes pour faire du clustering
        if (grapheCouplage.getClasses().size() < 2) {
            System.out.println("Pas assez de classes pour effectuer le clustering (minimum 2 classes).");
        } else {
            // Étape 2.1: Clustering hiérarchique
            ClusteringHierarchique clustering = new ClusteringHierarchique(grapheCouplage);
            Cluster dendrogramme = clustering.executerClustering();
            
            // Afficher le dendrogramme
            clustering.afficherDendrogramme();
            
            // Étape 2.2: Identification des modules
            // CP = seuil de couplage (on peut ajuster cette valeur)
            double seuilCP = 0.01; // Seuil assez bas pour trouver des modules
            
            IdentificateurModules identificateur = new IdentificateurModules(grapheCouplage, dendrogramme);
            identificateur.identifierModules(seuilCP);
            identificateur.afficherModules();
        }
        
        // ===================================
        // EXERCICE 3: Analyse avec Spoon
        // ===================================
        System.out.println("\n\n===============================================");
        System.out.println("      EXERCICE 3: ANALYSE AVEC SPOON");
        System.out.println("===============================================");
        
        System.out.println("\nPour exécuter l'Exercice 3 avec Spoon, utilisez:");
        System.out.println("  ./gradlew runTP2Spoon");
        System.out.println("\nCette version utilise la bibliothèque Spoon pour analyser automatiquement le code");
        System.out.println("et refait les exercices 1 et 2 avec les données extraites par Spoon.");
        
        System.out.println("\n\n===============================================");
        System.out.println("              FIN DU TP2");
        System.out.println("===============================================");
    }
    
    /**
     * Construit le graphe d'appel à partir d'un dossier de code Java
     * (Utilise la méthode du TP1 avec Eclipse JDT)
     * @param folder le dossier contenant les fichiers Java
     * @return le graphe d'appel
     */
    private static Map<String, Set<String>> construireGrapheAppel(File folder) throws IOException {
        String[] sourcepathEntries = { folder.getAbsolutePath() };
        
        // Trouver tous les fichiers .java
        Collection<File> javaFiles = FileUtils.listFiles(folder, new String[]{"java"}, true);
        System.out.println("Nombre de fichiers Java trouvés: " + javaFiles.size());
        
        // Créer un visiteur pour construire le graphe
        VisiteurGrapheAppel visiteur = new VisiteurGrapheAppel();
        
        // Parser chaque fichier Java
        for (File file : javaFiles) {
            String sourceCode = FileUtils.readFileToString(file, "UTF-8");
            
            // Créer un parser Eclipse JDT
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
    
    /**
     * Obtient un nom de classe court (sans le package)
     * @param nomComplet le nom complet
     * @return le nom court
     */
    private static String getNomCourt(String nomComplet) {
        int dernierPoint = nomComplet.lastIndexOf('.');
        if (dernierPoint >= 0) {
            return nomComplet.substring(dernierPoint + 1);
        }
        return nomComplet;
    }
}

