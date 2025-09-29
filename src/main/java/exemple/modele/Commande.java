package exemple.modele;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private String id;
    private LocalDate dateCommande;
    private List<LigneCommande> lignes;
    private StatutCommande statut;
    private Livraison livraison;

    public Commande(String id) {
        this.id = id;
        this.dateCommande = LocalDate.now();
        this.lignes = new ArrayList<>();
        this.statut = StatutCommande.EN_ATTENTE;
        this.livraison = new Livraison();
    }

    public void ajouterLigne(Produit produit, int quantite) {
        LigneCommande ligne = new LigneCommande(produit, quantite);
        lignes.add(ligne);
        mettreAJourStatut();
    }

    public double calculerMontant() {
        double total = 0;
        for (LigneCommande ligne : lignes) {
            total += ligne.calculerSousTotal();
        }
        return total;
    }

    public void traiter() {
        if (statut == StatutCommande.EN_ATTENTE) {
            statut = StatutCommande.EN_COURS;
            livraison.planifier(this);
        }
    }

    public void valider() {
        if (statut == StatutCommande.EN_COURS) {
            statut = StatutCommande.VALIDEE;
            livraison.confirmer();
        }
    }

    private void mettreAJourStatut() {
        if (lignes.isEmpty()) {
            statut = StatutCommande.EN_ATTENTE;
        } else if (calculerMontant() > 0) {
            statut = StatutCommande.PRETE;
        }
    }

    public String getId() { return id; }
    public LocalDate getDateCommande() { return dateCommande; }
    public List<LigneCommande> getLignes() { return lignes; }
    public StatutCommande getStatut() { return statut; }
    public Livraison getLivraison() { return livraison; }
}
