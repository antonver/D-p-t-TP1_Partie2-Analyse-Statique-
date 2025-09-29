package exemple.service;

import exemple.modele.*;
import java.util.ArrayList;
import java.util.List;

public class GestionnaireCommandes {
    private List<Commande> commandes;
    private GestionnaireStock gestionnaireStock;
    private CalculateurPrix calculateurPrix;

    public GestionnaireCommandes() {
        this.commandes = new ArrayList<>();
        this.gestionnaireStock = new GestionnaireStock();
        this.calculateurPrix = new CalculateurPrix();
    }

    public Commande creerCommande(String id, Utilisateur utilisateur) {
        Commande commande = new Commande(id);
        commandes.add(commande);
        utilisateur.ajouterCommande(commande);
        return commande;
    }

    public void ajouterProduitACommande(Commande commande, Produit produit, int quantite) {
        if (gestionnaireStock.verifierDisponibilite(produit, quantite)) {
            commande.ajouterLigne(produit, quantite);
            gestionnaireStock.reserverStock(produit, quantite);
        }
    }

    public double calculerMontantTotal(Commande commande, Utilisateur utilisateur) {
        double montantBase = commande.calculerMontant();
        double remise = calculateurPrix.calculerRemiseUtilisateur(utilisateur);
        return calculateurPrix.appliquerRemise(montantBase, remise);
    }

    public void traiterCommande(Commande commande) {
        commande.traiter();
        for (LigneCommande ligne : commande.getLignes()) {
            gestionnaireStock.consommerStock(ligne.getProduit(), ligne.getQuantite());
        }
    }

    public List<Commande> obtenirCommandesUtilisateur(Utilisateur utilisateur) {
        return utilisateur.getCommandes();
    }

    public void annulerCommande(Commande commande) {
        for (LigneCommande ligne : commande.getLignes()) {
            gestionnaireStock.libererStock(ligne.getProduit(), ligne.getQuantite());
        }
    }
}
