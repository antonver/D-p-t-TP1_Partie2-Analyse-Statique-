package exemple.utils;

public class GenerateurRapport {

    public void creerRapportVentes(double totalVentes, double moyenneCommande, int nombreCommandes) {
        System.out.println("=== RAPPORT DES VENTES ===");
        System.out.println("Total des ventes: " + totalVentes + "€");
        System.out.println("Moyenne par commande: " + moyenneCommande + "€");
        System.out.println("Nombre de commandes: " + nombreCommandes);
        System.out.println("==========================");
    }

    public void creerRapportUtilisateurs(int nombreUtilisateurs, int nombreClientsVIP) {
        System.out.println("=== RAPPORT UTILISATEURS ===");
        System.out.println("Nombre d'utilisateurs: " + nombreUtilisateurs);
        System.out.println("Clients VIP: " + nombreClientsVIP);
        System.out.println("Taux VIP: " + calculerPourcentage(nombreClientsVIP, nombreUtilisateurs) + "%");
        System.out.println("=============================");
    }

    public void ajouterAnalyseUtilisateur(String nom, String analyse) {
        System.out.println("Utilisateur " + nom + ": " + analyse);
    }

    public void ajouterAnalyseProduit(String nomProduit, boolean populaire) {
        String statut = populaire ? "Populaire" : "Standard";
        System.out.println("Produit " + nomProduit + ": " + statut);
    }

    public void genererRapportDetaille() {
        System.out.println("Génération du rapport détaillé...");
        ajouterSectionVentes();
        ajouterSectionStock();
        ajouterSectionRecommandations();
    }

    private void ajouterSectionVentes() {
        System.out.println("--- Section Ventes ---");
    }

    private void ajouterSectionStock() {
        System.out.println("--- Section Stock ---");
    }

    private void ajouterSectionRecommandations() {
        System.out.println("--- Recommandations ---");
    }

    private double calculerPourcentage(int valeur, int total) {
        if (total == 0) return 0;
        return (double) valeur / total * 100;
    }
}
