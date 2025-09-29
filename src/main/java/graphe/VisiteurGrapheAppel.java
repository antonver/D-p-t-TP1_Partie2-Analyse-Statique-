package graphe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

// Un nouveau visiteur, spécialisé pour trouver les appels de méthodes.
public class VisiteurGrapheAppel extends ASTVisitor {

    // Notre structure de données pour le graphe.
    // La clé est la méthode qui appelle, la valeur est une liste de méthodes appelées.
    private Map<String, Set<String>> grapheAppel = new HashMap<>();
    private String nomPackageActuel = "";
    private String nomClasseActuelle = "";
    private String nomMethodeActuelle = "";

    public Map<String, Set<String>> getGrapheAppel() {
        return grapheAppel;
    }

    // On commence par visiter le fichier pour trouver le nom du package.
    @Override
    public boolean visit(CompilationUnit node) {
        if (node.getPackage() != null) {
            nomPackageActuel = node.getPackage().getName().getFullyQualifiedName();
        }
        return super.visit(node);
    }

    // Ensuite, on trouve le nom de la classe.
    @Override
    public boolean visit(TypeDeclaration node) {
        // On ne veut pas les classes dans les méthodes (classes locales)
        if(node.isLocalTypeDeclaration()) {
            return false;
        }
        this.nomClasseActuelle = nomPackageActuel + "." + node.getName().getIdentifier();
        return super.visit(node);
    }

    // Puis on trouve le nom de la méthode qu'on visite. C'est la méthode "qui appelle".
    @Override
    public boolean visit(MethodDeclaration node) {
        // On construit le nom complet de la méthode
        this.nomMethodeActuelle = nomClasseActuelle + "." + node.getName().getIdentifier();
        return super.visit(node);
    }

    // C'est ici que la magie opère ! On a trouvé un appel de méthode.
    @Override
    public boolean visit(MethodInvocation node) {
        IMethodBinding binding = node.resolveMethodBinding();
        if (binding != null) {
            // On a trouvé la méthode appelée !
            String methodeAppelee = getNomCompletMethode(binding);
            // On ajoute l'appel au graphe.
            ajouterArc(nomMethodeActuelle, methodeAppelee);
        }
        return super.visit(node);
    }

    // Il faut aussi gérer les appels comme super.methode()
    @Override
    public boolean visit(SuperMethodInvocation node) {
        IMethodBinding binding = node.resolveMethodBinding();
        if (binding != null) {
            String methodeAppelee = getNomCompletMethode(binding);
            ajouterArc(nomMethodeActuelle, methodeAppelee);
        }
        return super.visit(node);
    }

    // Et les appels de constructeurs : new MaClasse()
    @Override
    public boolean visit(ClassInstanceCreation node) {
        IMethodBinding binding = node.resolveConstructorBinding();
        if (binding != null) {
            String constructeurAppele = getNomCompletMethode(binding);
            ajouterArc(nomMethodeActuelle, constructeurAppele);
        }
        return super.visit(node);
    }

    // Petite méthode pour avoir un nom de méthode propre, comme "com.package.Classe.methode"
    private String getNomCompletMethode(IMethodBinding binding) {
        ITypeBinding classe = binding.getDeclaringClass();
        // Pour les constructeurs, le nom est le même que la classe.
        String nomMethode = binding.isConstructor() ? classe.getName() : binding.getName();
        return classe.getQualifiedName() + "." + nomMethode;
    }

    // Méthode pour ajouter une connexion (un arc) dans notre graphe.
    private void ajouterArc(String depuis, String vers) {
        // On vérifie que les noms ne sont pas vides, pour être sûr.
        if (depuis == null || vers == null || depuis.isEmpty() || vers.isEmpty()) {
            return;
        }
        grapheAppel.putIfAbsent(depuis, new HashSet<>());
        grapheAppel.get(depuis).add(vers);
    }
}