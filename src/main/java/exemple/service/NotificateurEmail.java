package exemple.service;

import exemple.modele.Utilisateur;

public class NotificateurEmail {

    public void envoyerBienvenue(Utilisateur utilisateur) {
        String message = construireMessageBienvenue(utilisateur);
        envoyerEmail(utilisateur.getEmail(), "Bienvenue", message);
    }

    public void notifierStatutVIP(Utilisateur utilisateur) {
        String message = construireMessageVIP(utilisateur);
        envoyerEmail(utilisateur.getEmail(), "Statut VIP", message);
    }

    public void confirmerSuppression(Utilisateur utilisateur) {
        String message = construireMessageSuppression(utilisateur);
        envoyerEmail(utilisateur.getEmail(), "Compte supprimé", message);
    }

    public void notifierCommande(Utilisateur utilisateur, String commandeId) {
        String message = construireMessageCommande(utilisateur, commandeId);
        envoyerEmail(utilisateur.getEmail(), "Nouvelle commande", message);
    }

    private String construireMessageBienvenue(Utilisateur utilisateur) {
        return "Bienvenue " + utilisateur.getNom() + "! Votre compte a été créé avec succès.";
    }

    private String construireMessageVIP(Utilisateur utilisateur) {
        return "Félicitations " + utilisateur.getNom() + "! Vous êtes maintenant client VIP.";
    }

    private String construireMessageSuppression(Utilisateur utilisateur) {
        return "Au revoir " + utilisateur.getNom() + ". Votre compte a été supprimé.";
    }

    private String construireMessageCommande(Utilisateur utilisateur, String commandeId) {
        return "Bonjour " + utilisateur.getNom() + ", votre commande " + commandeId + " a été créée.";
    }

    private void envoyerEmail(String email, String sujet, String message) {
        System.out.println("Email envoyé à " + email + ": " + sujet);
    }
}
