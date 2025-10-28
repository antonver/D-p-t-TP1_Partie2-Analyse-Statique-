package clustering;

import couplage.GrapheCouplage;
import java.util.*;

/**
 * Identifie les modules (groupes de classes cohésifs) à partir du dendrogramme.
 * Applique les 3 règles définies dans le TP.
 */
public class IdentificateurModules {
    
    private GrapheCouplage grapheCouplage;
    private Cluster dendrogramme;
    
    // Le nombre total de classes
    private int nombreTotalClasses;
    
    // Les modules identifiés
    private List<Module> modules;
    
    /**
     * Constructeur
     * @param grapheCouplage le graphe de couplage
     * @param dendrogramme le dendrogramme issu du clustering
     */
    public IdentificateurModules(GrapheCouplage grapheCouplage, Cluster dendrogramme) {
        this.grapheCouplage = grapheCouplage;
        this.dendrogramme = dendrogramme;
        this.nombreTotalClasses = grapheCouplage.getClasses().size();
        this.modules = new ArrayList<>();
    }
    
    /**
     * Identifie les modules selon les 3 règles:
     * 1. Maximum M/2 modules (M = nombre de classes)
     * 2. Chaque module provient d'une seule branche
     * 3. Couplage moyen interne > CP (paramètre)
     * 
     * @param CP le seuil minimal de couplage interne
     * @return la liste des modules identifiés
     */
    public List<Module> identifierModules(double CP) {
        modules.clear();
        
        System.out.println("\n=== IDENTIFICATION DES MODULES ===");
        System.out.println("Nombre total de classes: " + nombreTotalClasses);
        System.out.println("Nombre maximum de modules: " + (nombreTotalClasses / 2));
        System.out.println("Seuil de couplage (CP): " + CP);
        
        // On explore le dendrogramme pour trouver les modules
        explorerDendrogramme(dendrogramme, CP);
        
        // Règle 1: Si on a trop de modules, on ne garde que les plus gros
        int maxModules = nombreTotalClasses / 2;
        if (modules.size() > maxModules) {
            System.out.println("\nTrop de modules (" + modules.size() + "), réduction à " + maxModules);
            // On trie par taille décroissante et on ne garde que les M/2 plus gros
            modules.sort((m1, m2) -> Integer.compare(m2.getTaille(), m1.getTaille()));
            modules = modules.subList(0, maxModules);
        }
        
        System.out.println("\nNombre final de modules: " + modules.size());
        
        return modules;
    }
    
    /**
     * Explore récursivement le dendrogramme pour trouver les modules
     * @param cluster le cluster actuel
     * @param CP le seuil de couplage
     */
    private void explorerDendrogramme(Cluster cluster, double CP) {
        // Si c'est une feuille (une seule classe), on ne crée pas de module
        if (cluster.estFeuille()) {
            return;
        }
        
        // Calculer le couplage moyen interne de ce cluster
        double couplageInterne = calculerCouplageMoyenInterne(cluster.getClasses());
        
        // Règle 3: Si le couplage interne est suffisant, on en fait un module
        if (couplageInterne >= CP) {
            Module module = new Module(cluster.getClasses(), couplageInterne);
            modules.add(module);
            
            System.out.printf("Module trouvé: %d classes, couplage interne = %.4f%n", 
                module.getTaille(), couplageInterne);
            
            // On ne continue pas à explorer les enfants car on a déjà un module valide
            return;
        }
        
        // Sinon, on explore récursivement les enfants (Règle 2: chaque module = une branche)
        if (cluster.getEnfantGauche() != null) {
            explorerDendrogramme(cluster.getEnfantGauche(), CP);
        }
        if (cluster.getEnfantDroit() != null) {
            explorerDendrogramme(cluster.getEnfantDroit(), CP);
        }
    }
    
    /**
     * Calcule le couplage moyen interne d'un ensemble de classes
     * (moyenne des couplages entre toutes les paires de classes du groupe)
     * @param classes l'ensemble des classes
     * @return le couplage moyen
     */
    private double calculerCouplageMoyenInterne(Set<String> classes) {
        if (classes.size() <= 1) {
            return 0.0;
        }
        
        List<String> listeClasses = new ArrayList<>(classes);
        double sommeCouplage = 0.0;
        int nombrePaires = 0;
        
        // Pour chaque paire de classes distinctes
        for (int i = 0; i < listeClasses.size(); i++) {
            for (int j = i + 1; j < listeClasses.size(); j++) {
                String classe1 = listeClasses.get(i);
                String classe2 = listeClasses.get(j);
                
                // Couplage bidirectionnel (on prend la somme des deux directions)
                double couplageAB = grapheCouplage.getCouplage(classe1, classe2);
                double couplageBA = grapheCouplage.getCouplage(classe2, classe1);
                
                sommeCouplage += (couplageAB + couplageBA);
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
     * Obtient la liste des modules identifiés
     * @return liste des modules
     */
    public List<Module> getModules() {
        return modules;
    }
    
    /**
     * Affiche les modules de manière lisible
     */
    public void afficherModules() {
        System.out.println("\n=== MODULES IDENTIFIÉS ===\n");
        
        if (modules.isEmpty()) {
            System.out.println("Aucun module identifié.");
            return;
        }
        
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            System.out.println("Module " + (i + 1) + ":");
            System.out.printf("  Taille: %d classes%n", module.getTaille());
            System.out.printf("  Couplage interne: %.4f%n", module.getCouplageMoyen());
            System.out.println("  Classes:");
            
            // Afficher les classes avec des noms courts
            List<String> classesTriees = new ArrayList<>(module.getClasses());
            Collections.sort(classesTriees);
            for (String classe : classesTriees) {
                System.out.println("    - " + getNomCourt(classe));
            }
            System.out.println();
        }
    }
    
    /**
     * Obtient un nom de classe plus court (sans le package)
     * @param nomComplet le nom complet de la classe
     * @return le nom simple
     */
    private String getNomCourt(String nomComplet) {
        int dernierPoint = nomComplet.lastIndexOf('.');
        if (dernierPoint >= 0) {
            return nomComplet.substring(dernierPoint + 1);
        }
        return nomComplet;
    }
    
    /**
     * Classe interne représentant un module
     */
    public static class Module {
        private Set<String> classes;
        private double couplageMoyen;
        
        public Module(Set<String> classes, double couplageMoyen) {
            this.classes = new HashSet<>(classes);
            this.couplageMoyen = couplageMoyen;
        }
        
        public Set<String> getClasses() {
            return classes;
        }
        
        public int getTaille() {
            return classes.size();
        }
        
        public double getCouplageMoyen() {
            return couplageMoyen;
        }
    }
}

