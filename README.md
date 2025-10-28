# TP2 - Analyse de Couplage et Identification de Modules

## Description

Ce projet implémente trois exercices pour analyser le code Java :

1. **Exercice 1** : Calcul du graphe de couplage entre classes
2. **Exercice 2** : Clustering hiérarchique et identification de modules
3. **Exercice 3** : Analyse automatique avec la bibliothèque Spoon

## Prérequis

- Java JDK 11 ou plus récent
- Git

## Installation

### Cloner le projet

```bash
git clone https://github.com/antonver/D-p-t-TP1_Partie2-Analyse-Statique-.git
cd D-p-t-TP1_Partie2-Analyse-Statique-
```

### Compiler

Sur macOS/Linux :
```bash
chmod +x gradlew
./gradlew build
```

Sur Windows :
```bash
gradlew.bat build
```

## Utilisation

### Analyser le projet exemple

```bash
./gradlew runTP2
```

### Analyser un autre projet

```bash
./gradlew runTP2 --args="/chemin/complet/vers/projet/src"
```

### Exercice 3 avec Spoon

```bash
./gradlew runTP2Spoon
```

## Commandes disponibles

### TP2

- `./gradlew runTP2` - Exercices 1 et 2 (avec Eclipse JDT)
- `./gradlew runTP2Spoon` - Exercice 3 (analyse avec Spoon)

### TP1 (disponibles aussi)

- `./gradlew runAnalyseurFX` - Interface graphique pour l'analyseur de métriques
- `./gradlew runGrapheFX` - Interface graphique pour le graphe d'appel
- `./gradlew runAnalyseurConsole --args="/chemin"` - Analyseur en console
- `./gradlew runGrapheConsole --args="/chemin"` - Graphe d'appel en console

## Structure du projet

```
src/main/java/
├── couplage/              # Exercice 1
├── clustering/            # Exercice 2
├── spoon/                 # Exercice 3
├── tp2/                   # Programmes principaux
├── graphe/                # TP1 - Graphe d'appel
├── analyseur/             # TP1 - Analyse de métriques
└── exemple/               # Projet exemple
```

## Configuration

Pour modifier le seuil de couplage (CP), éditez `src/main/java/tp2/MainTP2.java` :

```java
double seuilCP = 0.01;  // Valeur par défaut
```

## Dépendances

- Eclipse JDT Core 3.38.0
- Spoon 11.1.0
- Apache Commons IO 2.14.0
- JavaFX 21.0.1
- GraphStream 2.0
