package tp2;

import couplage.GrapheCouplage;
import clustering.ClusteringHierarchique;
import clustering.Cluster;
import clustering.IdentificateurModules;
import spoon.AnalyseurSpoon;

/**
 * Version alternative du TP2 Exercice 3 utilisant UNIQUEMENT Spoon.
 * Cette version évite les conflits avec Eclipse JDT.
 * 
 * IMPORTANT: Pour utiliser cette version, commentez la dépendance Eclipse JDT
 * dans build.gradle.kts et décommentez la ligne Spoon.
 */
public class MainTP2Spoon {
    
    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println("   TP2 - EXERCICE 3 : ANALYSE AVEC SPOON");
        System.out.println("===============================================\n");
        
        // Déterminer le chemin du projet à analyser
        String cheminProjet;
        if (args.length > 0) {
            cheminProjet = args[0];
        } else {
            // Par défaut, on analyse le projet exemple
            cheminProjet = "src/main/java/exemple";
            System.out.println("Utilisation du projet exemple par défaut.");
            System.out.println("Pour analyser un autre projet: java MainTP2Spoon /chemin/vers/projet\n");
        }
        
        try {
            // ===================================
            // Analyse avec Spoon
            // ===================================
            System.out.println("\n--- Analyse du code avec Spoon ---");
            AnalyseurSpoon analyseurSpoon = new spoon.AnalyseurSpoon(cheminProjet);
            
            // Afficher quelques statistiques
            System.out.println("\nClasses trouvées par Spoon:");
            int count = 0;
            for (String classe : analyseurSpoon.getClasses()) {
                if (count < 10) {
                    System.out.println("  - " + classe);
                    count++;
                }
            }
            if (analyseurSpoon.getClasses().size() > 10) {
                System.out.println("  ... et " + (analyseurSpoon.getClasses().size() - 10) + " autres");
            }
            
            // ===================================
            // Exercice 1 avec Spoon
            // ===================================
            System.out.println("\n\n===============================================");
            System.out.println("  EXERCICE 1 : GRAPHE DE COUPLAGE (SPOON)");
            System.out.println("===============================================");
            
            GrapheCouplage grapheCouplageSpoon = new GrapheCouplage(analyseurSpoon.getGrapheAppel());
            System.out.println("\nNombre de classes détectées: " + grapheCouplageSpoon.getClasses().size());
            System.out.println("Nombre total de relations: " + grapheCouplageSpoon.getTotalRelations());
            
            // Afficher la matrice de couplage
            grapheCouplageSpoon.afficherMatriceCouplage();
            
            // ===================================
            // Exercice 2 avec Spoon
            // ===================================
            if (grapheCouplageSpoon.getClasses().size() >= 2) {
                System.out.println("\n\n===============================================");
                System.out.println("  EXERCICE 2 : CLUSTERING ET MODULES (SPOON)");
                System.out.println("===============================================");
                
                // Clustering hiérarchique
                ClusteringHierarchique clusteringSpoon = new ClusteringHierarchique(grapheCouplageSpoon);
                Cluster dendrogrammeSpoon = clusteringSpoon.executerClustering();
                
                // Afficher le dendrogramme
                clusteringSpoon.afficherDendrogramme();
                
                // Identification des modules
                double seuilCP = 0.01;
                IdentificateurModules identificateur = new IdentificateurModules(grapheCouplageSpoon, dendrogrammeSpoon);
                identificateur.identifierModules(seuilCP);
                identificateur.afficherModules();
            } else {
                System.out.println("\nPas assez de classes pour effectuer le clustering.");
            }
            
            System.out.println("\n\n===============================================");
            System.out.println("           FIN DE L'ANALYSE SPOON");
            System.out.println("===============================================");
            
        } catch (Exception e) {
            System.err.println("\nErreur lors de l'analyse avec Spoon:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

