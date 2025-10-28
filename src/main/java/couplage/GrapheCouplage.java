package couplage;

import java.util.*;

/**
 * Classe pour calculer et gérer le graphe de couplage entre les classes.
 * Le couplage mesure la force des relations entre deux classes.
 */
public class GrapheCouplage {
    
    // Graphe d'appel de méthodes (du TP1)
    private Map<String, Set<String>> grapheAppel;
    
    // Matrice de couplage : stocke le couplage entre chaque paire de classes
    private Map<String, Map<String, Double>> matriceCouplage;
    
    // Liste de toutes les classes trouvées
    private Set<String> classes;
    
    // Nombre total de relations d'appel dans l'application
    private int totalRelations;
    
    /**
     * Constructeur : prend le graphe d'appel des méthodes
     * @param grapheAppel le graphe d'appel construit par VisiteurGrapheAppel
     */
    public GrapheCouplage(Map<String, Set<String>> grapheAppel) {
        this.grapheAppel = grapheAppel;
        this.matriceCouplage = new HashMap<>();
        this.classes = new HashSet<>();
        
        // On calcule le nombre total de relations
        calculerTotalRelations();
        
        // On extrait les noms des classes à partir des méthodes
        extraireClasses();
        
        // On construit la matrice de couplage
        construireMatriceCouplage();
    }
    
    /**
     * Calcule le nombre total de relations d'appel dans l'application
     */
    private void calculerTotalRelations() {
        totalRelations = 0;
        for (Set<String> appelees : grapheAppel.values()) {
            totalRelations += appelees.size();
        }
    }
    
    /**
     * Extrait les noms des classes à partir des noms complets des méthodes
     * Par exemple: "exemple.modele.Utilisateur.getNom" -> "exemple.modele.Utilisateur"
     */
    private void extraireClasses() {
        // Pour chaque méthode appelante
        for (String methode : grapheAppel.keySet()) {
            String classe = extraireNomClasse(methode);
            if (classe != null) {
                classes.add(classe);
            }
        }
        
        // Pour chaque méthode appelée
        for (Set<String> appelees : grapheAppel.values()) {
            for (String methode : appelees) {
                String classe = extraireNomClasse(methode);
                if (classe != null) {
                    classes.add(classe);
                }
            }
        }
    }
    
    /**
     * Extrait le nom de la classe à partir du nom complet d'une méthode
     * @param nomCompletMethode le nom complet (package.classe.methode)
     * @return le nom de la classe (package.classe)
     */
    private String extraireNomClasse(String nomCompletMethode) {
        if (nomCompletMethode == null || nomCompletMethode.isEmpty()) {
            return null;
        }
        
        // On enlève le dernier élément (le nom de la méthode)
        int dernierPoint = nomCompletMethode.lastIndexOf('.');
        if (dernierPoint > 0) {
            return nomCompletMethode.substring(0, dernierPoint);
        }
        return null;
    }
    
    /**
     * Construit la matrice de couplage pour toutes les paires de classes
     */
    private void construireMatriceCouplage() {
        // Pour chaque paire de classes, on calcule le couplage
        for (String classeA : classes) {
            matriceCouplage.put(classeA, new HashMap<>());
            for (String classeB : classes) {
                double couplage = calculerCouplage(classeA, classeB);
                matriceCouplage.get(classeA).put(classeB, couplage);
            }
        }
    }
    
    /**
     * Calcule le couplage entre deux classes A et B
     * Formule: Couplage(A,B) = (Nombre de relations entre A et B) / (Total des relations)
     * 
     * @param classeA première classe
     * @param classeB deuxième classe
     * @return la valeur de couplage (entre 0 et 1)
     */
    public double calculerCouplage(String classeA, String classeB) {
        if (totalRelations == 0) {
            return 0.0;
        }
        
        int relationsEntreAetB = compterRelationsEntreClasses(classeA, classeB);
        
        // On divise par le total pour obtenir une valeur normalisée
        return (double) relationsEntreAetB / totalRelations;
    }
    
    /**
     * Compte le nombre de relations d'appel entre les méthodes de deux classes
     * @param classeA première classe
     * @param classeB deuxième classe
     * @return le nombre de relations
     */
    private int compterRelationsEntreClasses(String classeA, String classeB) {
        int compteur = 0;
        
        // On parcourt toutes les méthodes du graphe d'appel
        for (Map.Entry<String, Set<String>> entry : grapheAppel.entrySet()) {
            String methodeAppelante = entry.getKey();
            String classeAppelante = extraireNomClasse(methodeAppelante);
            
            // Si la méthode appelante appartient à classeA
            if (classeA.equals(classeAppelante)) {
                // On compte combien de méthodes de classeB elle appelle
                for (String methodeAppelee : entry.getValue()) {
                    String classeAppelee = extraireNomClasse(methodeAppelee);
                    if (classeB.equals(classeAppelee)) {
                        compteur++;
                    }
                }
            }
        }
        
        return compteur;
    }
    
    /**
     * Retourne la matrice de couplage complète
     * @return la matrice sous forme de Map<Classe, Map<Classe, Couplage>>
     */
    public Map<String, Map<String, Double>> getMatriceCouplage() {
        return matriceCouplage;
    }
    
    /**
     * Retourne la liste de toutes les classes
     * @return ensemble des noms de classes
     */
    public Set<String> getClasses() {
        return classes;
    }
    
    /**
     * Obtient le couplage entre deux classes spécifiques
     * @param classeA première classe
     * @param classeB deuxième classe
     * @return la valeur de couplage
     */
    public double getCouplage(String classeA, String classeB) {
        if (matriceCouplage.containsKey(classeA) && 
            matriceCouplage.get(classeA).containsKey(classeB)) {
            return matriceCouplage.get(classeA).get(classeB);
        }
        return 0.0;
    }
    
    /**
     * Affiche la matrice de couplage de manière lisible
     */
    public void afficherMatriceCouplage() {
        System.out.println("\n=== MATRICE DE COUPLAGE ===\n");
        
        if (classes.isEmpty()) {
            System.out.println("Aucune classe trouvée.");
            return;
        }
        
        // On convertit en liste pour avoir un ordre fixe
        List<String> listeClasses = new ArrayList<>(classes);
        Collections.sort(listeClasses);
        
        // On affiche seulement les couplages non-nuls pour plus de clarté
        for (String classeA : listeClasses) {
            for (String classeB : listeClasses) {
                double couplage = getCouplage(classeA, classeB);
                
                // On affiche seulement si le couplage est significatif
                if (couplage > 0.0 && !classeA.equals(classeB)) {
                    System.out.printf("%s <-> %s : %.4f%n", 
                        getNomCourt(classeA), 
                        getNomCourt(classeB), 
                        couplage);
                }
            }
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
     * Retourne le nombre total de relations
     * @return le nombre total de relations d'appel
     */
    public int getTotalRelations() {
        return totalRelations;
    }
}

