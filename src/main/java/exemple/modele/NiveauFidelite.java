package exemple.modele;

public class NiveauFidelite {
    private String niveau;
    private int pointsFidelite;
    private double tauxRemise;

    public NiveauFidelite() {
        this.niveau = "Bronze";
        this.pointsFidelite = 0;
        this.tauxRemise = 0.0;
    }

    public void evaluer(Profil profil) {
        int commandes = profil.getNombreCommandes();
        double montant = profil.getMontantTotal();

        calculerPoints(montant);
        mettreAJourNiveau(commandes, montant);
        definirTauxRemise();
    }

    private void calculerPoints(double montant) {
        pointsFidelite += (int)(montant / 10);
    }

    private void mettreAJourNiveau(int commandes, double montant) {
        if (commandes >= 20 || montant >= 2000) {
            niveau = "Platine";
        } else if (commandes >= 10 || montant >= 1000) {
            niveau = "Or";
        } else if (commandes >= 5 || montant >= 500) {
            niveau = "Argent";
        } else {
            niveau = "Bronze";
        }
    }

    private void definirTauxRemise() {
        switch (niveau) {
            case "Platine": tauxRemise = 0.15; break;
            case "Or": tauxRemise = 0.10; break;
            case "Argent": tauxRemise = 0.05; break;
            default: tauxRemise = 0.0;
        }
    }

    public double obtenirRemise(double montant) {
        return montant * tauxRemise;
    }

    public void reinitialiser() {
        niveau = "Bronze";
        pointsFidelite = 0;
        tauxRemise = 0.0;
    }

    public String getNiveau() { return niveau; }
    public int getPointsFidelite() { return pointsFidelite; }
    public double getTauxRemise() { return tauxRemise; }
}
