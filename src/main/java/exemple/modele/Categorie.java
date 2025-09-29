package exemple.modele;

public class Categorie {
    private String nom;
    private double tauxTaxe;
    private String description;

    public Categorie(String nom, double tauxTaxe, String description) {
        this.nom = nom;
        this.tauxTaxe = tauxTaxe;
        this.description = description;
    }

    public double calculerTaxe(double montant) {
        return montant * tauxTaxe;
    }

    public boolean estTaxable() {
        return tauxTaxe > 0;
    }

    public String getNom() { return nom; }
    public double getTauxTaxe() { return tauxTaxe; }
    public String getDescription() { return description; }
}
