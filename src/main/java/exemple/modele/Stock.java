package exemple.modele;

public class Stock {
    private int quantite;
    private int quantiteReservee;
    private int seuilMinimum;

    public Stock() {
        this.quantite = 100;
        this.quantiteReservee = 0;
        this.seuilMinimum = 10;
    }

    public boolean peutReserver(int quantite) {
        return (this.quantite - quantiteReservee) >= quantite;
    }

    public void reserver(int quantite) {
        if (peutReserver(quantite)) {
            quantiteReservee += quantite;
        }
    }

    public void liberer(int quantite) {
        quantiteReservee = Math.max(0, quantiteReservee - quantite);
    }

    public void consommer(int quantite) {
        this.quantite -= quantite;
        this.quantiteReservee -= quantite;
    }

    public boolean estEnRupture() {
        return quantite <= seuilMinimum;
    }

    public void reapprovisionner(int quantite) {
        this.quantite += quantite;
    }

    public int getQuantite() { return quantite; }
    public int getQuantiteReservee() { return quantiteReservee; }
    public int getSeuilMinimum() { return seuilMinimum; }
}
