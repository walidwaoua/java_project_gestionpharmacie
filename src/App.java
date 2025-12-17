import service.ProduitService;
import service.VenteService;
import model.Produit;
import model.Vente;
import utils.FilePaths;
import utils.ThreadManager;

public class App {

    public static void main(String[] args) {
        afficherBanniere();

        System.out.println("=".repeat(80));
        System.out.println("DEMARRAGE DU SYSTEME DE GESTION DE PHARMACIE");
        System.out.println("=".repeat(80));
        System.out.println();

        // Initialisation des services
        ProduitService ps = new ProduitService(FilePaths.PRODUITS);
        VenteService vs = new VenteService(FilePaths.VENTES);

        // Chargement des données
        System.out.println("Chargement des donnees depuis les fichiers CSV...");
        ps.chargerDepuisCSV();
        System.out.println("Donnees chargees avec succes!\n");

        // Affichage des produits
        System.out.println("-".repeat(80));
        System.out.println("LISTE DES PRODUITS EN STOCK");
        System.out.println("-".repeat(80));
        ps.afficherProduits();
        System.out.println();

        // Recherche de produits
        System.out.println("-".repeat(80));
        System.out.println("RECHERCHE DE PRODUITS (mot-cle: 'asp')");
        System.out.println("-".repeat(80));
        ps.rechercherParNom("asp").forEach(System.out::println);
        System.out.println();

        // Filtrage des produits en rupture
        System.out.println("-".repeat(80));
        System.out.println("PRODUITS BIENTOT EN RUPTURE (seuil <= 5)");
        System.out.println("-".repeat(80));
        ps.filtrerBientotEnRupture(5).forEach(System.out::println);
        System.out.println();

        // Demarrage des threads
        System.out.println("-".repeat(80));
        System.out.println("DEMARRAGE DES TACHES EN ARRIERE-PLAN");
        System.out.println("-".repeat(80));
        Thread t1 = new Thread(new ThreadManager("Sauvegarde automatique"));
        Thread t2 = new Thread(new ThreadManager("Synchro reseau (simule)"));
        t1.start();
        t2.start();
        System.out.println();

        // Ajout d'un nouveau produit
        System.out.println("-".repeat(80));
        System.out.println("AJOUT D'UN NOUVEAU PRODUIT");
        System.out.println("-".repeat(80));
        Produit p = new Produit("P100", "DemoProduit", "Generique", 10, 12.5);
        ps.ajouter(p);
        System.out.println("Produit ajoute: " + p);
        ps.sauvegarderDansCSV();
        System.out.println("Donnees sauvegardees dans le fichier CSV\n");

        // Enregistrement d'une vente
        System.out.println("-".repeat(80));
        System.out.println("ENREGISTREMENT D'UNE VENTE");
        System.out.println("-".repeat(80));
        Vente v = new Vente("V1", "P100", 2, 25.0);
        vs.enregistrer(v);
        System.out.println("Vente enregistree: " + v);
        System.out.println();

        // Attente de la fin des threads
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.err.println("Erreur d'attente des threads: " + e.getMessage());
        }

        // Fin du programme
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SYSTEME ARRETE AVEC SUCCES");
        System.out.println("=".repeat(80));
    }

    private static void afficherBanniere() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                ║");
        System.out.println("║                    SYSTÈME DE GESTION DE PHARMACIE                             ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                           ─────────────────────────                            ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                              MILESTONE 2                                      ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                           ─────────────────────────                            ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                    Realise par: Walid Waoua Ayman Elkhaddar                    ║");
        System.out.println("║                    Mohammed Redda karrach Marouane sabiq Ouhmid hanane         ║");
        System.out.println("║                    Date: Decembre 2024                                        ║");
        System.out.println("║                    Projet Java - Gestion de Pharmacie                         ║");
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }
}