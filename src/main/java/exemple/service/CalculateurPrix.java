package exemple.service;

import exemple.modele.Utilisateur;
import exemple.modele.Profil;
import exemple.modele.NiveauFidelite;

public class CalculateurPrix {

    public double calculerRemiseUtilisateur(Utilisateur utilisateur) {
        Profil profil = utilisateur.getProfil();
        utilisateur.mettreAJourProfil();

        NiveauFidelite niveau = profil.getNiveauFidelite();
        return niveau.obtenirRemise(profil.getMontantTotal());
    }

    public double appliquerRemise(double montantBase, double remise) {
        return montantBase - remise;
    }

    public double calculerTaxes(double montant, double tauxTaxe) {
        return montant * tauxTaxe;
    }

    public double calculerMontantFinal(double montantBase, double remise, double taxes) {
        double montantAvecRemise = appliquerRemise(montantBase, remise);
        return montantAvecRemise + taxes;
    }

    public boolean appliquerRemiseVIP(Utilisateur utilisateur) {
        return utilisateur.estClientVIP();
    }
}
