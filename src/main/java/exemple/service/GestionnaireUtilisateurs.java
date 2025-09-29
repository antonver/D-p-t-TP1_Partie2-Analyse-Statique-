package exemple.service;

import exemple.modele.*;
import java.util.ArrayList;
import java.util.List;

public class GestionnaireUtilisateurs {
    private List<Utilisateur> utilisateurs;
    private ValidateurUtilisateur validateur;
    private NotificateurEmail notificateur;

    public GestionnaireUtilisateurs() {
        this.utilisateurs = new ArrayList<>();
        this.validateur = new ValidateurUtilisateur();
        this.notificateur = new NotificateurEmail();
    }

    public Utilisateur creerUtilisateur(String nom, String email) {
        if (validateur.validerEmail(email) && validateur.validerNom(nom)) {
            Utilisateur utilisateur = new Utilisateur(nom, email);
            utilisateurs.add(utilisateur);
            notificateur.envoyerBienvenue(utilisateur);
            return utilisateur;
        }
        return null;
    }

    public Utilisateur rechercherParEmail(String email) {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getEmail().equals(email)) {
                return utilisateur;
            }
        }
        return null;
    }

    public void mettreAJourProfils() {
        for (Utilisateur utilisateur : utilisateurs) {
            utilisateur.mettreAJourProfil();
            if (utilisateur.estClientVIP()) {
                notificateur.notifierStatutVIP(utilisateur);
            }
        }
    }

    public List<Utilisateur> obtenirClientsVIP() {
        List<Utilisateur> vips = new ArrayList<>();
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.estClientVIP()) {
                vips.add(utilisateur);
            }
        }
        return vips;
    }

    public void supprimerUtilisateur(Utilisateur utilisateur) {
        utilisateurs.remove(utilisateur);
        notificateur.confirmerSuppression(utilisateur);
    }
}
