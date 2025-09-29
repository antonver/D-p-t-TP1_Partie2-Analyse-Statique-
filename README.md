# **Analyseur Statique de Code Java**

Ce projet est un outil d'analyse de code source pour des applications écrites en Java. Il permet de calculer des métriques de code et de visualiser le graphe d'appel des méthodes. L'application est disponible en version console et en version avec interface graphique (GUI) utilisant JavaFX.

-----

## **Prérequis**

Avant de commencer, assurez-vous d'avoir les outils suivants installés sur votre machine :

* **JDK (Java Development Kit)** : Version 11 ou supérieure.
* **Gradle** : Version 7.0 ou supérieure (ou utilisez le Gradle Wrapper inclus dans le projet).
* **Git** : Pour cloner le dépôt.

-----

## **Installation**

Suivez ces étapes simples pour installer et construire le projet.

1.  **Clonez le dépôt**
    Ouvrez un terminal et exécutez la commande suivante pour télécharger le code source :

    ```bash
    git clone <URL_DU_DEPOT_GIT>
    cd <NOM_DU_DOSSIER_PROJET>
    ```

2.  **Construisez le projet avec Gradle**
    Le projet inclut un Gradle Wrapper, ce qui signifie que vous n'avez pas besoin d'installer Gradle manuellement. Pour compiler le code et télécharger toutes les dépendances, exécutez :

    * Sur macOS/Linux :
      ```bash
      ./gradlew build
      ```
    * Sur Windows :
      ```bash
      gradlew.bat build
      ```

    Cette commande va télécharger les librairies nécessaires et compiler tous les fichiers `.java`. Si tout se passe bien, vous devriez voir un message `BUILD SUCCESSFUL`.

-----

## **Utilisation**

Le projet est divisé en deux modules principaux, chacun avec une version console et une version graphique.

### **Module 1 : Analyseur de Métriques**

Ce module analyse un projet et affiche des statistiques (nombre de classes, de méthodes, lignes de code, etc.).

* **Lancer la version graphique (GUI) **
  C'est la méthode la plus simple. Exécutez la commande suivante pour ouvrir l'application avec une interface utilisateur. Vous pourrez y choisir un dossier à analyser.

  ```bash
  ./gradlew runAnalyseurFX
  ```

* **Lancer la version console 🖥️**
  Cette version affiche les résultats directement dans le terminal. Vous devez fournir le chemin vers le projet que vous voulez analyser.

  ```bash
  ./gradlew runAnalyseurConsole --args="/chemin/complet/vers/le/projet"
  ```

  **Exemple :**
  `./gradlew runAnalyseurConsole --args="/Users/monUtilisateur/documents/monProjetJava"`

### **Module 2 : Visualiseur du Graphe d'Appel**

Ce module analyse les appels de méthodes et affiche un graphe interactif des dépendances.

* **Lancer la version graphique (GUI)**
  Ouvre une fenêtre où vous pouvez sélectionner un projet. Le graphe d'appel sera ensuite affiché dans cette même fenêtre.

  ```bash
  ./gradlew runGrapheFX
  ```

* **Lancer la version console️**
  Cette commande analyse le projet et ouvre une fenêtre séparée (gérée par GraphStream) pour afficher le graphe.

  ```bash
  ./gradlew runGrapheConsole --args="/chemin/complet/vers/le/projet"
  ```

  **Exemple :**
  `./gradlew runGrapheConsole --args="C:\Projets\MonAppli"`

-----

## **Dépendances**

Le projet utilise les librairies externes suivantes, qui sont gérées automatiquement par Gradle :

* **Eclipse JDT Core** : Pour l'analyse syntaxique du code Java.
* **Apache Commons IO** : Pour simplifier la lecture des fichiers.
* **JavaFX** : Pour les interfaces graphiques.
* **GraphStream** : Pour la modélisation et la visualisation des graphes.