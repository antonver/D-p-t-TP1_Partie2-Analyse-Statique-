package exemple.utils;

import java.util.Properties;
import java.io.InputStream;

public class ConfigurationApplication {
    private Properties proprietes;

    public ConfigurationApplication() {
        this.proprietes = new Properties();
    }

    public void chargerConfiguration() {
        System.out.println("Chargement de la configuration...");
        definirValeursParsDefaut();
    }

    private void definirValeursParsDefaut() {
        proprietes.setProperty("app.nom", "Commerce Application");
        proprietes.setProperty("app.version", "1.0.0");
        proprietes.setProperty("db.timeout", "30");
        proprietes.setProperty("email.smtp.server", "smtp.example.com");
        proprietes.setProperty("stock.seuil.minimum", "10");
    }

    public String obtenirPropriete(String cle) {
        return proprietes.getProperty(cle);
    }

    public int obtenirProprieteEntier(String cle) {
        String valeur = obtenirPropriete(cle);
        return valeur != null ? Integer.parseInt(valeur) : 0;
    }

    public boolean obtenirProprieteBoolean(String cle) {
        String valeur = obtenirPropriete(cle);
        return valeur != null && Boolean.parseBoolean(valeur);
    }

    public void definirPropriete(String cle, String valeur) {
        proprietes.setProperty(cle, valeur);
    }

    public void sauvegarderConfiguration() {
        System.out.println("Sauvegarde de la configuration...");
    }
}
