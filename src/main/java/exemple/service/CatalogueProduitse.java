package exemple.service;

import exemple.modele.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CatalogueProduitse {
    private Map<String, Produit> produits;
    private List<Categorie> categories;

    public CatalogueProduitse() {
        this.produits = new HashMap<>();
        this.categories = new ArrayList<>();

        initialiserCategories();
    }

    public void initialiserProduits() {
        creerProduitsParDefaut();
    }

    private void initialiserCategories() {
        categories.add(new Categorie("Electronique", 0.20, "Appareils électroniques"));
        categories.add(new Categorie("Vetements", 0.15, "Vêtements et accessoires"));
        categories.add(new Categorie("Alimentation", 0.05, "Produits alimentaires"));
        categories.add(new Categorie("Maison", 0.10, "Articles pour la maison"));
    }

    private void creerProduitsParDefaut() {
        Categorie electronique = rechercherCategorie("Electronique");
        Categorie vetements = rechercherCategorie("Vetements");
        Categorie alimentation = rechercherCategorie("Alimentation");

        ajouterProduit(new Produit("PROD-001", "Smartphone", 599.99, electronique));
        ajouterProduit(new Produit("PROD-002", "T-shirt", 29.99, vetements));
        ajouterProduit(new Produit("PROD-003", "Café Premium", 15.99, alimentation));
        ajouterProduit(new Produit("PROD-004", "Ordinateur Portable", 899.99, electronique));
        ajouterProduit(new Produit("PROD-005", "Jean", 49.99, vetements));
    }

    public void ajouterProduit(Produit produit) {
        produits.put(produit.getId(), produit);
        configurerStock(produit);
    }

    public Produit rechercherProduit(String id) {
        return produits.get(id);
    }

    public List<Produit> rechercherParCategorie(String nomCategorie) {
        List<Produit> resultats = new ArrayList<>();
        for (Produit produit : produits.values()) {
            if (produit.getCategorie().getNom().equals(nomCategorie)) {
                resultats.add(produit);
            }
        }
        return resultats;
    }

    public List<Produit> obtenirProduitsDisponibles() {
        List<Produit> disponibles = new ArrayList<>();
        for (Produit produit : produits.values()) {
            if (produit.estDisponible()) {
                disponibles.add(produit);
            }
        }
        return disponibles;
    }

    private Categorie rechercherCategorie(String nom) {
        for (Categorie categorie : categories) {
            if (categorie.getNom().equals(nom)) {
                return categorie;
            }
        }
        return categories.get(0);
    }

    private void configurerStock(Produit produit) {
        Stock stock = produit.getStock();
        stock.reapprovisionner(50);
    }

    public List<Produit> obtenirTousProduits() {
        return new ArrayList<>(produits.values());
    }
}
