package clustering;

import couplage.GrapheCouplage;
import java.util.*;

/**
 * Implémente l'algorithme de clustering hiérarchique (agglomératif).
 * L'algorithme fusionne progressivement les clusters les plus couplés.
 */
public class ClusteringHierarchique {
    
    private GrapheCouplage grapheCouplage;
    
    // Le dendrogramme final (l'arbre résultant)
    private Cluster dendrogramme;
    
    /**
     * Constructeur
     * @param grapheCouplage le graphe de couplage entre les classes
     */
    public ClusteringHierarchique(GrapheCouplage grapheCouplage) {
        this.grapheCouplage = grapheCouplage;
    }
    
    /**
     * Exécute l'algorithme de clustering hiérarchique
     * @return le dendrogramme (cluster racine)
     */
    public Cluster executerClustering() {
        // Réinitialiser le compteur d'IDs des clusters
        Cluster.reinitialiserCompteur();
        
        // Étape 1: Créer un cluster pour chaque classe (feuilles de l'arbre)
        List<Cluster> clustersActifs = new ArrayList<>();
        for (String classe : grapheCouplage.getClasses()) {
            clustersActifs.add(new Cluster(classe));
        }
        
        System.out.println("\n=== DÉBUT DU CLUSTERING HIÉRARCHIQUE ===");
        System.out.println("Nombre de clusters initiaux: " + clustersActifs.size());
        
        int etape = 1;
        
        // Étape 2: Répéter jusqu'à ce qu'il ne reste qu'un seul cluster
        while (clustersActifs.size() > 1) {
            System.out.println("\n--- Étape " + etape + " ---");
            System.out.println("Nombre de clusters actifs: " + clustersActifs.size());
            
            // Trouver les deux clusters les plus couplés
            PaireCluster meilleurePaire = trouverClustersPlusCouples(clustersActifs);
            
            if (meilleurePaire == null) {
                // Si on ne trouve plus de paire, on fusionne arbitrairement les deux premiers
                System.out.println("Aucun couplage trouvé, fusion arbitraire");
                Cluster c1 = clustersActifs.get(0);
                Cluster c2 = clustersActifs.get(1);
                Cluster nouveauCluster = new Cluster(c1, c2, 0.0);
                
                clustersActifs.remove(c1);
                clustersActifs.remove(c2);
                clustersActifs.add(nouveauCluster);
            } else {
                // Fusionner ces deux clusters
                System.out.printf("Fusion de %s et %s (couplage = %.4f)%n",
                    meilleurePaire.cluster1.toString(),
                    meilleurePaire.cluster2.toString(),
                    meilleurePaire.couplage);
                
                Cluster nouveauCluster = new Cluster(
                    meilleurePaire.cluster1, 
                    meilleurePaire.cluster2, 
                    meilleurePaire.couplage
                );
                
                // Retirer les anciens clusters et ajouter le nouveau
                clustersActifs.remove(meilleurePaire.cluster1);
                clustersActifs.remove(meilleurePaire.cluster2);
                clustersActifs.add(nouveauCluster);
            }
            
            etape++;
        }
        
        // Le dernier cluster restant est la racine du dendrogramme
        dendrogramme = clustersActifs.get(0);
        System.out.println("\n=== FIN DU CLUSTERING ===");
        System.out.println("Dendrogramme créé avec succès!");
        
        return dendrogramme;
    }
    
    /**
     * Trouve les deux clusters avec le couplage le plus élevé
     * @param clusters la liste des clusters actifs
     * @return la meilleure paire de clusters
     */
    private PaireCluster trouverClustersPlusCouples(List<Cluster> clusters) {
        PaireCluster meilleurePaire = null;
        double meilleurCouplage = -1.0;
        
        // On teste toutes les paires possibles
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                Cluster c1 = clusters.get(i);
                Cluster c2 = clusters.get(j);
                
                // Calculer le couplage moyen entre ces deux clusters
                double couplage = calculerCouplageMoyen(c1, c2);
                
                // Si c'est le meilleur couplage trouvé jusqu'ici
                if (couplage > meilleurCouplage) {
                    meilleurCouplage = couplage;
                    meilleurePaire = new PaireCluster(c1, c2, couplage);
                }
            }
        }
        
        return meilleurePaire;
    }
    
    /**
     * Calcule le couplage moyen entre deux clusters
     * Le couplage entre deux clusters = moyenne des couplages entre toutes les paires de classes
     * @param c1 premier cluster
     * @param c2 deuxième cluster
     * @return le couplage moyen
     */
    private double calculerCouplageMoyen(Cluster c1, Cluster c2) {
        Set<String> classes1 = c1.getClasses();
        Set<String> classes2 = c2.getClasses();
        
        double sommeCouplage = 0.0;
        int nombrePaires = 0;
        
        // Pour chaque paire de classes (une de c1, une de c2)
        for (String classe1 : classes1) {
            for (String classe2 : classes2) {
                // On prend le max des deux directions (A->B et B->A)
                double couplageAB = grapheCouplage.getCouplage(classe1, classe2);
                double couplageBA = grapheCouplage.getCouplage(classe2, classe1);
                double couplageBidirectionnel = Math.max(couplageAB, couplageBA);
                
                sommeCouplage += couplageBidirectionnel;
                nombrePaires++;
            }
        }
        
        // Retourner la moyenne
        if (nombrePaires > 0) {
            return sommeCouplage / nombrePaires;
        }
        return 0.0;
    }
    
    /**
     * Obtient le dendrogramme (arbre hiérarchique)
     * @return le cluster racine
     */
    public Cluster getDendrogramme() {
        return dendrogramme;
    }
    
    /**
     * Affiche le dendrogramme de manière lisible
     */
    public void afficherDendrogramme() {
        System.out.println("\n=== DENDROGRAMME ===\n");
        if (dendrogramme != null) {
            dendrogramme.afficherArbre("");
        } else {
            System.out.println("Le dendrogramme n'a pas encore été créé. Exécutez d'abord executerClustering().");
        }
    }
    
    /**
     * Classe interne pour représenter une paire de clusters
     */
    private static class PaireCluster {
        Cluster cluster1;
        Cluster cluster2;
        double couplage;
        
        PaireCluster(Cluster c1, Cluster c2, double couplage) {
            this.cluster1 = c1;
            this.cluster2 = c2;
            this.couplage = couplage;
        }
    }
}

