package analyseur;

import java.util.ArrayList;
import java.util.List;

// Une petite classe juste pour stocker les infos sur une seule classe java.
// C'est plus simple pour organiser les données.
public class InfosClasse {
    private String nom;
    private int nombreMethodes = 0;
    private int nombreAttributs = 0;

    // On garde aussi les détails de chaque méthode
    private List<InfosMethode> methodes = new ArrayList<>();

    public InfosClasse(String nom) {
        this.nom = nom;
    }

    public void incrementerMethodes() {
        this.nombreMethodes++;
    }

    public void addAttributs(int nombre) {
        this.nombreAttributs += nombre;
    }

    public void addMethode(InfosMethode methode) {
        this.methodes.add(methode);
        incrementerMethodes();
    }

    // Getters
    public String getNom() { return nom; }
    public int getNombreMethodes() { return nombreMethodes; }
    public int getNombreAttributs() { return nombreAttributs; }
    public List<InfosMethode> getMethodes() { return methodes; }

    // Une classe interne pour les méthodes, c'est pratique.
    public static class InfosMethode {
        private int lignesDeCode;
        private int nbParametres;

        public InfosMethode(int lignesDeCode, int nbParametres) {
            this.lignesDeCode = lignesDeCode;
            this.nbParametres = nbParametres;
        }

        public int getLignesDeCode() { return lignesDeCode; }
        public int getNbParametres() { return nbParametres; }
    }
}