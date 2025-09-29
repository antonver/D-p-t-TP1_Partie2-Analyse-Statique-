package exemple.utils;

import exemple.modele.*;
import java.util.List;

public class CalculateurStatistiques {

    public void mettreAJourStatistiques(Commande commande) {
        System.out.println("Mise à jour des statistiques pour commande: " + commande.getId());
    }

    public double calculerTotalVentes(List<Commande> commandes) {
        double total = 0;
        for (Commande commande : commandes) {
            total += commande.calculerMontant();
        }
        return total;
    }

    public double calculerMoyenneCommande(List<Commande> commandes) {
        if (commandes.isEmpty()) return 0;
        double total = calculerTotalVentes(commandes);
        return total / commandes.size();
    }

    public int compterClientsVIP(List<Utilisateur> utilisateurs) {
        int count = 0;
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.estClientVIP()) {
                count++;
            }
        }
        return count;
    }

    public String analyserComportement(Utilisateur utilisateur) {
        Profil profil = utilisateur.getProfil();
        int commandes = profil.getNombreCommandes();
        double montant = profil.getMontantTotal();

        if (commandes > 15 && montant > 1500) {
            return "Client très actif et haute valeur";
        } else if (commandes > 10 || montant > 1000) {
            return "Client régulier";
        } else if (commandes > 5) {
            return "Client occasionnel";
        } else {
            return "Nouveau client";
        }
    }

    public boolean estProduitPopulaire(Produit produit, List<Commande> commandes) {
        int compteur = 0;
        for (Commande commande : commandes) {
            for (LigneCommande ligne : commande.getLignes()) {
                if (ligne.getProduit().getId().equals(produit.getId())) {
                    compteur += ligne.getQuantite();
                }
            }
        }
        return compteur > 10;
    }

    public double calculerTauxConversion(int visiteurs, int acheteurs) {
        if (visiteurs == 0) return 0;
        return (double) acheteurs / visiteurs * 100;
    }

    public double calculerPanierMoyen(List<Commande> commandes) {
        return calculerMoyenneCommande(commandes);
    }
}
