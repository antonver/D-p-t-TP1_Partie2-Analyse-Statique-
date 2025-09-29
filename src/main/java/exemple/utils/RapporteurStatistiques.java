package exemple.utils;

import exemple.modele.*;
import exemple.service.*;
import java.util.List;
import java.util.ArrayList;

public class RapporteurStatistiques {
    private List<Commande> historiqueCommandes;
    private List<Utilisateur> utilisateursActifs;
    private CalculateurStatistiques calculateur;
    private GenerateurRapport generateur;

    public RapporteurStatistiques() {
        this.historiqueCommandes = new ArrayList<>();
        this.utilisateursActifs = new ArrayList<>();
        this.calculateur = new CalculateurStatistiques();
        this.generateur = new GenerateurRapport();
    }

    public void initialiserRapports() {
        System.out.println("Initialisation des rapports...");
    }

    public void enregistrerCommande(Commande commande, Utilisateur utilisateur) {
        historiqueCommandes.add(commande);
        if (!utilisateursActifs.contains(utilisateur)) {
            utilisateursActifs.add(utilisateur);
        }
        calculateur.mettreAJourStatistiques(commande);
    }

    public void genererRapportVentes() {
        double totalVentes = calculateur.calculerTotalVentes(historiqueCommandes);
        double moyenneCommande = calculateur.calculerMoyenneCommande(historiqueCommandes);

        generateur.creerRapportVentes(totalVentes, moyenneCommande, historiqueCommandes.size());
    }

    public void genererRapportUtilisateurs() {
        int nombreUtilisateurs = utilisateursActifs.size();
        int nombreClientsVIP = calculateur.compterClientsVIP(utilisateursActifs);

        generateur.creerRapportUtilisateurs(nombreUtilisateurs, nombreClientsVIP);

        for (Utilisateur utilisateur : utilisateursActifs) {
            analyserProfilUtilisateur(utilisateur);
        }
    }

    private void analyserProfilUtilisateur(Utilisateur utilisateur) {
        Profil profil = utilisateur.getProfil();
        String analyse = calculateur.analyserComportement(utilisateur);
        generateur.ajouterAnalyseUtilisateur(utilisateur.getNom(), analyse);
    }

    public void genererRapportProduits(List<Produit> produits) {
        for (Produit produit : produits) {
            analyserPerformanceProduit(produit);
        }
    }

    private void analyserPerformanceProduit(Produit produit) {
        boolean populaire = calculateur.estProduitPopulaire(produit, historiqueCommandes);
        generateur.ajouterAnalyseProduit(produit.getNom(), populaire);
    }
}
