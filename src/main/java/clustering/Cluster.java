package clustering;

import java.util.*;

/**
 * Représente un cluster (groupe) de classes.
 * Peut être une classe simple ou un groupe fusionné de plusieurs classes.
 */
public class Cluster {
    
    // Identifiant unique du cluster
    private int id;
    
    // Les classes contenues dans ce cluster
    private Set<String> classes;
    
    // Les enfants de ce cluster (si c'est un cluster fusionné)
    // null si c'est une feuille (classe individuelle)
    private Cluster enfantGauche;
    private Cluster enfantDroit;
    
    // Le couplage moyen qui a causé cette fusion
    private double couplage;
    
    // Compteur statique pour générer des IDs uniques
    private static int compteurId = 0;
    
    /**
     * Constructeur pour un cluster feuille (une seule classe)
     * @param nomClasse le nom de la classe
     */
    public Cluster(String nomClasse) {
        this.id = compteurId++;
        this.classes = new HashSet<>();
        this.classes.add(nomClasse);
        this.enfantGauche = null;
        this.enfantDroit = null;
        this.couplage = 0.0;
    }
    
    /**
     * Constructeur pour un cluster fusionné (résultat de fusion de deux clusters)
     * @param c1 premier cluster
     * @param c2 deuxième cluster
     * @param couplage le couplage entre ces deux clusters
     */
    public Cluster(Cluster c1, Cluster c2, double couplage) {
        this.id = compteurId++;
        this.classes = new HashSet<>();
        this.classes.addAll(c1.getClasses());
        this.classes.addAll(c2.getClasses());
        this.enfantGauche = c1;
        this.enfantDroit = c2;
        this.couplage = couplage;
    }
    
    /**
     * Vérifie si ce cluster est une feuille (classe individuelle)
     * @return true si c'est une feuille
     */
    public boolean estFeuille() {
        return enfantGauche == null && enfantDroit == null;
    }
    
    /**
     * Obtient toutes les classes de ce cluster
     * @return ensemble des noms de classes
     */
    public Set<String> getClasses() {
        return classes;
    }
    
    /**
     * Obtient le nombre de classes dans ce cluster
     * @return le nombre de classes
     */
    public int getTaille() {
        return classes.size();
    }
    
    public int getId() {
        return id;
    }
    
    public Cluster getEnfantGauche() {
        return enfantGauche;
    }
    
    public Cluster getEnfantDroit() {
        return enfantDroit;
    }
    
    public double getCouplage() {
        return couplage;
    }
    
    /**
     * Réinitialise le compteur d'IDs (utile pour les tests)
     */
    public static void reinitialiserCompteur() {
        compteurId = 0;
    }
    
    @Override
    public String toString() {
        if (estFeuille()) {
            // Pour une feuille, on affiche juste le nom de la classe
            return classes.iterator().next();
        } else {
            // Pour un cluster fusionné, on affiche la taille
            return "Cluster" + id + "(" + getTaille() + " classes)";
        }
    }
    
    /**
     * Affiche l'arbre du cluster de manière récursive
     * @param prefixe le préfixe pour l'indentation
     */
    public void afficherArbre(String prefixe) {
        System.out.println(prefixe + toString() + 
            (estFeuille() ? "" : " [couplage=" + String.format("%.4f", couplage) + "]"));
        
        if (!estFeuille()) {
            if (enfantGauche != null) {
                enfantGauche.afficherArbre(prefixe + "  ├─ ");
            }
            if (enfantDroit != null) {
                enfantDroit.afficherArbre(prefixe + "  └─ ");
            }
        }
    }
}

