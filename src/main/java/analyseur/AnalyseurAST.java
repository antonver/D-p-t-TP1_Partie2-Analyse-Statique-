package analyseur;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.*;

// C'est notre visiteur. Il va parcourir l'arbre syntaxique (AST).
// Chaque fois qu'il trouve un noeud intéressant (classe, méthode...), il fait quelque chose.
public class AnalyseurAST extends ASTVisitor {

    private InfosProjet infosProjet;
    private CompilationUnit compilationUnit; // Pour avoir accès à tout le fichier
    private String nomClasseActuelle;

    public AnalyseurAST(InfosProjet infosProjet, CompilationUnit compilationUnit) {
        this.infosProjet = infosProjet;
        this.compilationUnit = compilationUnit;
    }

    // Cette méthode est appelée quand le visiteur entre dans un fichier .java
    @Override
    public boolean visit(CompilationUnit node) {
        // On récupère le nombre total de lignes du fichier.
        int lignes = node.getLineNumber(node.getLength() - 1);
        infosProjet.addLignesDeCode(lignes);

        PackageDeclaration packageNode = node.getPackage();
        if (packageNode != null) {
            // On ajoute le nom du package à notre liste, s'il n'est pas déjà là.
            infosProjet.addPackage(packageNode.getName().getFullyQualifiedName());
        }
        return super.visit(node);
    }

    // Cette méthode est appelée pour chaque classe trouvée.
    @Override
    public boolean visit(TypeDeclaration node) {
        // On vérifie si ce n'est pas une interface, on veut seulement les classes.
        if (!node.isInterface()) {
            this.nomClasseActuelle = node.getName().getFullyQualifiedName();
            infosProjet.addClasse(this.nomClasseActuelle);
        }
        return super.visit(node);
    }

    // Cette méthode est appelée pour chaque méthode dans une classe.
    @Override
    public boolean visit(MethodDeclaration node) {
        if (this.nomClasseActuelle != null) {
            // On calcule le nombre de lignes dans la méthode.
            int startLine = compilationUnit.getLineNumber(node.getStartPosition());
            int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength());
            int methodLines = endLine - startLine;

            // On récupère le nombre de paramètres.
            int parametres = node.parameters().size();

            // On stocke ces informations.
            infosProjet.addMethode(this.nomClasseActuelle, methodLines, parametres);
        }
        return super.visit(node);
    }

    // Et finalement, pour les attributs de la classe.
    @Override
    public boolean visit(FieldDeclaration node) {
        if (this.nomClasseActuelle != null) {
            // Chaque déclaration peut avoir plusieurs variables (ex: int i, j;).
            int nombreAttributs = node.fragments().size();
            infosProjet.addAttributs(this.nomClasseActuelle, nombreAttributs);
        }
        return super.visit(node);
    }
}