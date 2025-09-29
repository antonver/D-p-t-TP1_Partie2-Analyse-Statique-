package exemple.modele;

public class Produit {
    private String id;
    private String nom;
    private double prix;
    private Categorie categorie;
    private Stock stock;

    public Produit(String id, String nom, double prix, Categorie categorie) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.categorie = categorie;
        this.stock = new Stock();
    }

    public double calculerPrixAvecTaxe() {
        return prix * (1 + categorie.getTauxTaxe());
    }

    public boolean estDisponible() {
        return stock.getQuantite() > 0;
    }

    public void reserverStock(int quantite) {
        if (stock.peutReserver(quantite)) {
            stock.reserver(quantite);
        }
    }

    public void libererStock(int quantite) {
        stock.liberer(quantite);
    }

    public String getId() { return id; }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }
    public Categorie getCategorie() { return categorie; }
    public Stock getStock() { return stock; }
}
