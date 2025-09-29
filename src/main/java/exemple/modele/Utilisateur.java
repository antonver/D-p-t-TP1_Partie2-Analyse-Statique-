package exemple.modele;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
    private String nom;
    private String email;
    private List<Commande> commandes;
    private Profil profil;

    public Utilisateur(String nom, String email) {
        this.nom = nom;
        this.email = email;
        this.commandes = new ArrayList<>();
        this.profil = new Profil();
    }

    public void ajouterCommande(Commande commande) {
        commandes.add(commande);
        profil.incrementerNombreCommandes();
    }

    public double calculerTotalCommandes() {
        double total = 0;
        for (Commande cmd : commandes) {
            total += cmd.calculerMontant();
        }
        return total;
    }

    public boolean estClientVIP() {
        return profil.getNombreCommandes() > 10 || calculerTotalCommandes() > 1000.0;
    }

    public void mettreAJourProfil() {
        profil.setMontantTotal(calculerTotalCommandes());
        profil.setStatut(estClientVIP() ? "VIP" : "Standard");
    }

    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public List<Commande> getCommandes() { return commandes; }
    public Profil getProfil() { return profil; }
}
