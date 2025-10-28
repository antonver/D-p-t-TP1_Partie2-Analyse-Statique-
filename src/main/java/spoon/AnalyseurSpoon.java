package spoon;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;
import java.util.Collection;

/**
 * Analyseur de code Java utilisant la bibliothèque Spoon.
 * Extrait les classes, méthodes et construit le graphe d'appel.
 */
public class AnalyseurSpoon {
    
    // Le modèle Spoon du projet analysé
    private CtModel model;
    
    // Le graphe d'appel (même structure que dans TP1)
    private Map<String, Set<String>> grapheAppel;
    
    // Liste de toutes les classes trouvées
    private Set<String> classes;
    
    /**
     * Constructeur
     * @param cheminProjet le chemin vers le projet Java à analyser
     */
    public AnalyseurSpoon(String cheminProjet) {
        this.grapheAppel = new HashMap<>();
        this.classes = new HashSet<>();
        
        // Initialiser Spoon
        analyserProjet(cheminProjet);
    }
    
    /**
     * Analyse le projet avec Spoon
     * @param cheminProjet le chemin du projet
     */
    private void analyserProjet(String cheminProjet) {
        System.out.println("\n=== ANALYSE AVEC SPOON ===");
        System.out.println("Projet: " + cheminProjet);
        
        // Créer un launcher Spoon
        Launcher launcher = new Launcher();
        
        // Ajouter le chemin source du projet
        launcher.addInputResource(cheminProjet);
        
        // Configuration : on veut analyser sans compiler
        launcher.getEnvironment().setNoClasspath(true);
        launcher.getEnvironment().setAutoImports(false);
        launcher.getEnvironment().setCommentEnabled(false);
        
        // Construire le modèle
        System.out.println("Construction du modèle Spoon...");
        model = launcher.buildModel();
        System.out.println("Modèle construit avec succès!");
        
        // Extraire les classes
        extraireClasses();
        
        // Construire le graphe d'appel
        construireGrapheAppel();
        
        System.out.println("\nStatistiques:");
        System.out.println("  - Nombre de classes: " + classes.size());
        System.out.println("  - Nombre de méthodes appelantes: " + grapheAppel.size());
    }
    
    /**
     * Extrait toutes les classes du modèle
     */
    private void extraireClasses() {
        // Obtenir tous les types (classes, interfaces, etc.)
        Collection<CtType<?>> allTypes = model.getAllTypes();
        
        for (CtType<?> type : allTypes) {
            // On ne prend que les vraies classes (pas les interfaces, etc.)
            if (type instanceof CtClass) {
                String nomComplet = type.getQualifiedName();
                classes.add(nomComplet);
            }
        }
    }
    
    /**
     * Construit le graphe d'appel en parcourant toutes les méthodes
     */
    private void construireGrapheAppel() {
        // Pour chaque type dans le modèle
        for (CtType<?> type : model.getAllTypes()) {
            String nomClasse = type.getQualifiedName();
            
            // Pour chaque méthode de cette classe
            for (CtMethod<?> methode : type.getMethods()) {
                String nomMethodeAppelante = nomClasse + "." + methode.getSimpleName();
                
                // Trouver tous les appels de méthode dans cette méthode
                List<CtInvocation<?>> invocations = methode.getElements(new TypeFilter<>(CtInvocation.class));
                
                for (CtInvocation<?> invocation : invocations) {
                    try {
                        // Obtenir la méthode appelée
                        if (invocation.getExecutable() != null && 
                            invocation.getExecutable().getDeclaringType() != null) {
                            
                            String classeAppelee = invocation.getExecutable()
                                .getDeclaringType().getQualifiedName();
                            String nomMethodeAppelee = invocation.getExecutable().getSimpleName();
                            String methodeAppelee = classeAppelee + "." + nomMethodeAppelee;
                            
                            // Ajouter l'arc au graphe
                            ajouterArc(nomMethodeAppelante, methodeAppelee);
                        }
                    } catch (Exception e) {
                        // Ignorer les erreurs (méthodes non résolues, etc.)
                        // C'est normal avec setNoClasspath(true)
                    }
                }
            }
        }
    }
    
    /**
     * Ajoute un arc dans le graphe d'appel
     * @param depuis la méthode appelante
     * @param vers la méthode appelée
     */
    private void ajouterArc(String depuis, String vers) {
        if (depuis == null || vers == null || depuis.isEmpty() || vers.isEmpty()) {
            return;
        }
        
        grapheAppel.putIfAbsent(depuis, new HashSet<>());
        grapheAppel.get(depuis).add(vers);
    }
    
    /**
     * Obtient le graphe d'appel
     * @return le graphe d'appel (même structure que VisiteurGrapheAppel)
     */
    public Map<String, Set<String>> getGrapheAppel() {
        return grapheAppel;
    }
    
    /**
     * Obtient la liste des classes
     * @return ensemble des noms de classes
     */
    public Set<String> getClasses() {
        return classes;
    }
    
    /**
     * Affiche le graphe d'appel
     */
    public void afficherGrapheAppel() {
        System.out.println("\n=== GRAPHE D'APPEL (SPOON) ===\n");
        
        if (grapheAppel.isEmpty()) {
            System.out.println("Le graphe d'appel est vide.");
            return;
        }
        
        // Trier pour un affichage plus lisible
        List<String> methodesTriees = new ArrayList<>(grapheAppel.keySet());
        Collections.sort(methodesTriees);
        
        for (String methodeAppelante : methodesTriees) {
            Set<String> appelees = grapheAppel.get(methodeAppelante);
            if (!appelees.isEmpty()) {
                System.out.println(getNomCourt(methodeAppelante) + " appelle:");
                
                List<String> appeleesTriees = new ArrayList<>(appelees);
                Collections.sort(appeleesTriees);
                
                for (String appelee : appeleesTriees) {
                    System.out.println("  -> " + getNomCourt(appelee));
                }
                System.out.println();
            }
        }
    }
    
    /**
     * Obtient un nom de méthode plus court
     * @param nomComplet le nom complet (package.classe.methode)
     * @return un nom court (classe.methode)
     */
    private String getNomCourt(String nomComplet) {
        String[] parties = nomComplet.split("\\.");
        if (parties.length > 1) {
            return parties[parties.length - 2] + "." + parties[parties.length - 1];
        }
        return nomComplet;
    }
}

