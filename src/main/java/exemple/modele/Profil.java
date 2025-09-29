package exemple.modele;

public class Profil {
    private int nombreCommandes;
    private double montantTotal;
    private String statut;
    private NiveauFidelite niveauFidelite;

    public Profil() {
        this.nombreCommandes = 0;
        this.montantTotal = 0.0;
        this.statut = "Nouveau";
        this.niveauFidelite = new NiveauFidelite();
    }

    public void incrementerNombreCommandes() {
        nombreCommandes++;
        niveauFidelite.evaluer(this);
    }

    public double calculerRemise() {
        return niveauFidelite.obtenirRemise(montantTotal);
    }

    public void reinitialiser() {
        nombreCommandes = 0;
        montantTotal = 0.0;
        statut = "Nouveau";
        niveauFidelite.reinitialiser();
    }

    public int getNombreCommandes() { return nombreCommandes; }
    public double getMontantTotal() { return montantTotal; }
    public String getStatut() { return statut; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }
    public void setStatut(String statut) { this.statut = statut; }
    public NiveauFidelite getNiveauFidelite() { return niveauFidelite; }
}
