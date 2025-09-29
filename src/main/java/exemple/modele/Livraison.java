package exemple.modele;

import java.time.LocalDate;

public class Livraison {
    private LocalDate datePrevue;
    private LocalDate dateReelle;
    private String adresse;
    private String transporteur;
    private boolean confirmee;

    public Livraison() {
        this.confirmee = false;
        this.transporteur = "Standard";
    }

    public void planifier(Commande commande) {
        this.datePrevue = LocalDate.now().plusDays(3);
        this.adresse = obtenirAdresseLivraison(commande);
        choisirTransporteur(commande);
    }

    public void confirmer() {
        this.confirmee = true;
        this.dateReelle = LocalDate.now();
    }

    private String obtenirAdresseLivraison(Commande commande) {
        return "Adresse par défaut";
    }

    private void choisirTransporteur(Commande commande) {
        double montant = commande.calculerMontant();
        if (montant > 100) {
            this.transporteur = "Express";
        } else {
            this.transporteur = "Standard";
        }
    }

    public LocalDate getDatePrevue() { return datePrevue; }
    public LocalDate getDateReelle() { return dateReelle; }
    public String getAdresse() { return adresse; }
    public String getTransporteur() { return transporteur; }
    public boolean isConfirmee() { return confirmee; }
}
