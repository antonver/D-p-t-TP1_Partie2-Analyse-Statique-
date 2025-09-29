package exemple.application;

import exemple.modele.*;
import exemple.service.*;
import exemple.utils.*;

public class ApplicationCommerce {
    private GestionnaireUtilisateurs gestionnaireUtilisateurs;
    private GestionnaireCommandes gestionnaireCommandes;
    private CatalogueProduitse catalogue;
    private RapporteurStatistiques rapporteur;
    private ConfigurationApplication config;

    public ApplicationCommerce() {
        this.gestionnaireUtilisateurs = new GestionnaireUtilisateurs();
        this.gestionnaireCommandes = new GestionnaireCommandes();
        this.catalogue = new CatalogueProduitse();
        this.rapporteur = new RapporteurStatistiques();
        this.config = new ConfigurationApplication();

        initialiserApplication();
    }

    public void demarrer() {
        System.out.println("Démarrage de l'application commerce...");

        Utilisateur utilisateur1 = creerUtilisateurTest();
        Utilisateur utilisateur2 = creerAutreUtilisateur();

        simulerActiviteCommerciale(utilisateur1);
        simulerActiviteCommerciale(utilisateur2);

        genererRapports();
    }

    private void initialiserApplication() {
        config.chargerConfiguration();
        catalogue.initialiserProduits();
        rapporteur.initialiserRapports();
    }

    private Utilisateur creerUtilisateurTest() {
        Utilisateur utilisateur = gestionnaireUtilisateurs.creerUtilisateur("Jean Dupont", "jean@email.com");
        if (utilisateur != null) {
            System.out.println("Utilisateur créé: " + utilisateur.getNom());
        }
        return utilisateur;
    }

    private Utilisateur creerAutreUtilisateur() {
        return gestionnaireUtilisateurs.creerUtilisateur("Marie Martin", "marie@email.com");
    }

    private void simulerActiviteCommerciale(Utilisateur utilisateur) {
        if (utilisateur == null) return;

        Commande commande = gestionnaireCommandes.creerCommande("CMD-" + System.currentTimeMillis(), utilisateur);

        Produit produit1 = catalogue.rechercherProduit("PROD-001");
        Produit produit2 = catalogue.rechercherProduit("PROD-002");

        if (produit1 != null) {
            gestionnaireCommandes.ajouterProduitACommande(commande, produit1, 2);
        }
        if (produit2 != null) {
            gestionnaireCommandes.ajouterProduitACommande(commande, produit2, 1);
        }

        double montantTotal = gestionnaireCommandes.calculerMontantTotal(commande, utilisateur);
        System.out.println("Montant total commande: " + montantTotal);

        gestionnaireCommandes.traiterCommande(commande);
        rapporteur.enregistrerCommande(commande, utilisateur);
    }

    private void genererRapports() {
        gestionnaireUtilisateurs.mettreAJourProfils();
        rapporteur.genererRapportVentes();
        rapporteur.genererRapportUtilisateurs();
    }

    public static void main(String[] args) {
        ApplicationCommerce app = new ApplicationCommerce();
        app.demarrer();
    }
}
