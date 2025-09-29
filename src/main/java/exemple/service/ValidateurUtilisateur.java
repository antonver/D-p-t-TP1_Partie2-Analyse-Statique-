package exemple.service;

import exemple.modele.Utilisateur;

public class ValidateurUtilisateur {

    public boolean validerEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    public boolean validerNom(String nom) {
        if (nom == null || nom.isEmpty()) {
            return false;
        }
        return nom.length() >= 2 && nom.matches("[a-zA-ZÀ-ÿ\\s]+");
    }

    public boolean validerUtilisateur(Utilisateur utilisateur) {
        return validerNom(utilisateur.getNom()) &&
               validerEmail(utilisateur.getEmail());
    }

    public boolean validerMotDePasse(String motDePasse) {
        return motDePasse != null &&
               motDePasse.length() >= 8 &&
               motDePasse.matches(".*[A-Z].*") &&
               motDePasse.matches(".*[0-9].*");
    }
}
