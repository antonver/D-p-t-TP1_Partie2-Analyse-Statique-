plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"


val javafxVersion = "21.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jdt:org.eclipse.jdt.core:3.40.0")
    implementation("commons-io:commons-io:2.14.0")

    implementation("org.openjfx:javafx-controls:$javafxVersion")
    implementation("org.openjfx:javafx-graphics:$javafxVersion")
    implementation("org.openjfx:javafx-fxml:$javafxVersion")
    implementation("org.openjfx:javafx-swing:$javafxVersion")

    implementation("org.graphstream:gs-core:2.0")
    implementation("org.graphstream:gs-ui-javafx:2.0")
    implementation("org.graphstream:gs-ui-swing:2.0")

}


application {

    mainClass.set("graphe.MainGraphe")
}

javafx {
    version = javafxVersion
    modules("javafx.controls", "javafx.fxml", "javafx.graphics", "javafx.swing")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

// --- NOS NOUVELLES TÂCHES PERSONNALISÉES ---

// Tâche pour lancer l'analyseur de graphe en CONSOLE
tasks.register<JavaExec>("runGraphe") {
    group = "Execution"
    description = "Lance l'analyseur qui affiche le graphe d'appel (console)"

    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("graphe.MainGraphe")

    // Permet de passer des arguments via la ligne de commande avec --args
    if (project.hasProperty("args")) {
        args((project.property("args") as String).split(" "))
    }
}

// Tâche pour lancer l'interface graphique JavaFX du Graphe (Partie 2)
tasks.register<JavaExec>("runGrapheFX") {
    group = "Execution"
    description = "Lance l'interface graphique JavaFX pour l'analyseur de graphe"

    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("graphe.AppGrapheFX")

    // On configure dynamiquement les arguments JVM avant l'exécution
    doFirst {
        val modulePath = classpath.filter { it.name.startsWith("javafx-") }.joinToString(File.pathSeparator)
        jvmArgs = listOf(
            "--module-path", modulePath,
            "--add-modules", "javafx.controls,javafx.fxml,javafx.graphics,javafx.swing"
        )
    }
}

// Tâche pour lancer l'interface graphique JavaFX de l'Analyseur (Partie 1)
tasks.register<JavaExec>("runAnalyseurFX") {
    group = "Execution"
    description = "Lance l'interface graphique JavaFX pour l'analyseur de la Partie 1"

    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("lanceur.AppFX")
    // On configure dynamiquement les arguments JVM avant l'exécution
    doFirst {
        val modulePath = classpath.filter { it.name.startsWith("javafx-") }.joinToString(File.pathSeparator)
        jvmArgs = listOf(
            "--module-path", modulePath,
            "--add-modules", "javafx.controls,javafx.fxml,javafx.graphics,javafx.swing"
        )
    }
}
// Tâche pour lancer l'analyseur de métriques en console (Partie 1)
tasks.register<JavaExec>("runAnalyseurConsole") {
    group = "Execution"
    description = "Lance l'analyseur de métriques en console (Partie 1)"

    classpath = sourceSets.main.get().runtimeClasspath

    mainClass.set("lanceur.Main")

    if (project.hasProperty("args")) {
        args((project.property("args") as String).split(" "))
    }
}

// Tâche pour lancer l'analyseur de graphe en console (Partie 2)
tasks.register<JavaExec>("runGrapheConsole") {
    group = "Execution"
    description = "Lance l'analyseur de graphe d'appel en console (Partie 2)"

    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("graphe.MainGraphe")

    if (project.hasProperty("args")) {
        args((project.property("args") as String).split(" "))
    }
}