package analyseur;

import java.util.*;
import java.util.stream.Collectors;

// Cette classe va contenir TOUTES les informations du projet analysé.
// C'est aussi ici qu'on va faire tous les calculs demandés.
public class InfosProjet {

    private Map<String, InfosClasse> classes = new HashMap<>();
    private Set<String> packages = new HashSet<>();
    private int totalLignesDeCode = 0;

    // Méthodes pour ajouter les informations pendant le parsing
    public void addClasse(String nom) {
        classes.putIfAbsent(nom, new InfosClasse(nom));
    }

    public void addLignesDeCode(int nombre) {
        this.totalLignesDeCode += nombre;
    }

    public void addPackage(String nomPackage) {
        packages.add(nomPackage);
    }

    public void addMethode(String nomClasse, int lignes, int parametres) {
        InfosClasse classe = classes.get(nomClasse);
        if (classe != null) {
            classe.addMethode(new InfosClasse.InfosMethode(lignes, parametres));
        }
    }

    public void addAttributs(String nomClasse, int nombre) {
        InfosClasse classe = classes.get(nomClasse);
        if (classe != null) {
            classe.addAttributs(nombre);
        }
    }

    // --- DEBUT DES CALCULS ---

    // 1. Nombre de classes
    public int getNombreClasses() {
        return classes.size();
    }

    // 2. Nombre de lignes de code
    public int getTotalLignesDeCode() {
        return totalLignesDeCode;
    }

    // 3. Nombre total de méthodes
    public int getTotalMethodes() {
        return classes.values().stream().mapToInt(InfosClasse::getNombreMethodes).sum();
    }

    // 4. Nombre total de packages
    public int getTotalPackages() {
        return packages.size();
    }

    // 5. Nombre moyen de méthodes par classe
    public double getMoyenneMethodesParClasse() {
        if (classes.isEmpty()) return 0;
        return (double) getTotalMethodes() / getNombreClasses();
    }

    // 6. Nombre moyen de lignes de code par méthode
    public double getMoyenneLignesParMethode() {
        if (getTotalMethodes() == 0) return 0;
        int totalLignesMethodes = classes.values().stream()
                .flatMap(c -> c.getMethodes().stream())
                .mapToInt(InfosClasse.InfosMethode::getLignesDeCode)
                .sum();
        return (double) totalLignesMethodes / getTotalMethodes();
    }

    // 7. Nombre moyen d’attributs par classe
    public double getMoyenneAttributsParClasse() {
        if (classes.isEmpty()) return 0;
        int totalAttributs = classes.values().stream().mapToInt(InfosClasse::getNombreAttributs).sum();
        return (double) totalAttributs / getNombreClasses();
    }

    // 8. Les 10% des classes qui possèdent le plus grand nombre de méthodes
    public List<InfosClasse> getTop10PercentClassesParMethodes() {
        if (classes.isEmpty()) return new ArrayList<>();
        int limit = (int) Math.ceil(classes.size() * 0.10);
        return classes.values().stream()
                .sorted(Comparator.comparingInt(InfosClasse::getNombreMethodes).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 9. Les 10% des classes qui possèdent le plus grand nombre d’attributs
    public List<InfosClasse> getTop10PercentClassesParAttributs() {
        if (classes.isEmpty()) return new ArrayList<>();
        int limit = (int) Math.ceil(classes.size() * 0.10);
        return classes.values().stream()
                .sorted(Comparator.comparingInt(InfosClasse::getNombreAttributs).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 10. Les classes qui font partie en même temps des deux catégories précédentes
    public List<InfosClasse> getClassesDansLesDeuxTops() {
        List<InfosClasse> topMethodes = getTop10PercentClassesParMethodes();
        List<InfosClasse> topAttributs = getTop10PercentClassesParAttributs();
        return topMethodes.stream()
                .filter(topAttributs::contains)
                .collect(Collectors.toList());
    }

    // 11. Les classes qui possèdent plus de X méthodes
    public List<InfosClasse> getClassesAvecPlusDeXMethodes(int x) {
        return classes.values().stream()
                .filter(c -> c.getNombreMethodes() > x)
                .collect(Collectors.toList());
    }

    // 12. Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe)
    public Map<String, List<InfosClasse.InfosMethode>> getTop10PercentMethodesParLignes() {
        Map<String, List<InfosClasse.InfosMethode>> resultat = new HashMap<>();
        for (InfosClasse classe : classes.values()) {
            if (!classe.getMethodes().isEmpty()) {
                int limit = (int) Math.ceil(classe.getMethodes().size() * 0.10);
                List<InfosClasse.InfosMethode> topMethodes = classe.getMethodes().stream()
                        .sorted(Comparator.comparingInt(InfosClasse.InfosMethode::getLignesDeCode).reversed())
                        .limit(limit)
                        .collect(Collectors.toList());
                resultat.put(classe.getNom(), topMethodes);
            }
        }
        return resultat;
    }

    // 13. Le nombre maximal de paramètres
    public int getMaxParametres() {
        return classes.values().stream()
                .flatMap(c -> c.getMethodes().stream())
                .mapToInt(InfosClasse.InfosMethode::getNbParametres)
                .max()
                .orElse(0);
    }
}