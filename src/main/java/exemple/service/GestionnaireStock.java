package exemple.service;

import exemple.modele.Produit;
import exemple.modele.Stock;

public class GestionnaireStock {

    public boolean verifierDisponibilite(Produit produit, int quantite) {
        Stock stock = produit.getStock();
        return stock.peutReserver(quantite);
    }

    public void reserverStock(Produit produit, int quantite) {
        if (verifierDisponibilite(produit, quantite)) {
            produit.reserverStock(quantite);
        }
    }

    public void libererStock(Produit produit, int quantite) {
        produit.libererStock(quantite);
    }

    public void consommerStock(Produit produit, int quantite) {
        Stock stock = produit.getStock();
        stock.consommer(quantite);

        if (stock.estEnRupture()) {
            declencherReapprovisionnement(produit);
        }
    }

    public void reapprovisionner(Produit produit, int quantite) {
        Stock stock = produit.getStock();
        stock.reapprovisionner(quantite);
    }

    private void declencherReapprovisionnement(Produit produit) {
        int quantiteReappro = calculerQuantiteReapprovisionnement(produit);
        reapprovisionner(produit, quantiteReappro);
    }

    private int calculerQuantiteReapprovisionnement(Produit produit) {
        return 50;
    }
}
