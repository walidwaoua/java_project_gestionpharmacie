import service.ProduitService;
import service.VenteService;
import model.Produit;
import model.Vente;
import utils.FilePaths;
import utils.ThreadManager;

public class App {
    public static void main(String[] args) {
        ProduitService ps = new ProduitService(FilePaths.PRODUITS);
        ps.chargerDepuisCSV();

        System.out.println("-- Produits chargés --");
        ps.afficherProduits();

        System.out.println("\n-- Recherche 'asp' --");
        ps.rechercherParNom("asp").forEach(System.out::println);

        System.out.println("\n-- Filtrer seuil <= 5 --");
        ps.filtrerBientotEnRupture(5).forEach(System.out::println);

        Thread t1 = new Thread(new ThreadManager("Sauvegarde automatique"));
        Thread t2 = new Thread(new ThreadManager("Synchro réseau (simulé)"));
        t1.start(); t2.start();

        VenteService vs = new VenteService(FilePaths.VENTES);
        Produit p = new Produit("P100","DemoProduit","Générique",10,12.5);
        ps.ajouter(p);
        ps.sauvegarderDansCSV();

        Vente v = new Vente("V1","P100",2,25.0);
        vs.enregistrer(v);

        System.out.println("Fin App");
    }
}
