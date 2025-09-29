package exemple.modele;

public class LigneCommande {
    private Produit produit;
    private int quantite;
    private double prixUnitaire;

    public LigneCommande(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = produit.calculerPrixAvecTaxe();
        produit.reserverStock(quantite);
    }

    public double calculerSousTotal() {
        return prixUnitaire * quantite;
    }

    public void modifierQuantite(int nouvelleQuantite) {
        int difference = nouvelleQuantite - quantite;
        if (difference > 0) {
            produit.reserverStock(difference);
        } else if (difference < 0) {
            produit.libererStock(-difference);
        }
        this.quantite = nouvelleQuantite;
    }

    public boolean estValide() {
        return produit.estDisponible() && quantite > 0;
    }

    public Produit getProduit() { return produit; }
    public int getQuantite() { return quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
}
