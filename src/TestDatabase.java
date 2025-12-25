import database.DatabaseManager;
import dao.*;
import model.*;

public class TestDatabase {

    public static void main(String[] args) {
        afficherBanniere();

        // Nettoyer les données pour éviter les collisions PK lors des reruns
        DatabaseManager.initialiserBDD();
        DatabaseManager.purgerDonneesTest();

        // 1. Initialiser la base de données
        System.out.println("\n" + "=".repeat(80));
        System.out.println("1)  INITIALISATION DE LA BASE DE DONNÉES");
        System.out.println("=".repeat(80));
        DatabaseManager.initialiserBDD();

        // Tester la connexion
        if (DatabaseManager.testerConnexion()) {
            System.out.println("Test de connexion réussi");
        } else {
            System.out.println("Test de connexion échoué");
            return;
        }

        // 2. Tester les opérations CRUD sur les Produits
        System.out.println("\n" + "=".repeat(80));
        System.out.println("2)  TEST DES OPÉRATIONS CRUD - PRODUITS");
        System.out.println("=".repeat(80));
        testerCRUDProduits();

        // 3. Tester les opérations CRUD sur les Utilisateurs
        System.out.println("\n" + "=".repeat(80));
        System.out.println("3)  TEST DES OPÉRATIONS CRUD - UTILISATEURS");
        System.out.println("=".repeat(80));
        testerCRUDUtilisateurs();

        // 4. Tester les opérations CRUD sur les Ventes
        System.out.println("\n" + "=".repeat(80));
        System.out.println("4)  TEST DES OPÉRATIONS CRUD - VENTES");
        System.out.println("=".repeat(80));
        testerCRUDVentes();

        // 5. Tester la synchronisation
        System.out.println("\n" + "=".repeat(80));
        System.out.println("5)  TEST DE SYNCHRONISATION (THREADS)");
        System.out.println("=".repeat(80));
        testerSynchronisation();

        // Fermer la connexion
        System.out.println("\n" + "=".repeat(80));
        System.out.println("FIN DES TESTS");
        System.out.println("=".repeat(80));
        DatabaseManager.fermerConnexion();
    }

    private static void testerCRUDProduits() {
        ProduitDAO dao = new ProduitDAO();

        // CREATE
        System.out.println("\nTest CREATE:");
        String suffix = String.valueOf(System.currentTimeMillis());
        Produit p1 = new Produit("TEST1_" + suffix, "Paracetamol Test", "Générique", 50, 2.5);
        Produit p2 = new Produit("TEST2_" + suffix, "Aspirine Test", "Générique", 30, 1.8);
        dao.ajouter(p1);
        dao.ajouter(p2);

        // READ
        System.out.println("\nTest READ:");
        Produit p = dao.obtenirParId(p1.getId());
        System.out.println("Produit trouvé: " + (p != null ? p : "Non trouvé"));

        System.out.println("\nTous les produits:");
        dao.obtenirTous().forEach(System.out::println);

        // UPDATE
        System.out.println("\nTest UPDATE:");
        p1.setQuantite(75);
        dao.mettreAJour(p1);

        // Recherche
        System.out.println("\nTest RECHERCHE (nom contenant 'para'):");
        dao.rechercherParNom("para").forEach(System.out::println);

        System.out.println("\nProduits en rupture (seuil <= 35):");
        dao.obtenirProduitsEnRupture(35).forEach(System.out::println);

        // DELETE
        System.out.println("\nTest DELETE:");
        dao.supprimer(p2.getId());
        System.out.println("Produits restants:");
        dao.obtenirTous().forEach(System.out::println);
    }

    private static void testerCRUDUtilisateurs() {
        UtilisateurDAO dao = new UtilisateurDAO();

        // CREATE
        System.out.println("\nTest CREATE:");
        String suffix = String.valueOf(System.currentTimeMillis());
        Admin admin = new Admin("ADMIN_TEST_" + suffix, "Admin Test");
        Pharmacien pharma = new Pharmacien("PHARMA_TEST_" + suffix, "Pharmacien Test");
        dao.ajouter(admin);
        dao.ajouter(pharma);

        // READ
        System.out.println("\nTest READ:");
        Utilisateur u = dao.obtenirParId(admin.getId());
        System.out.println("Utilisateur trouvé: " + (u != null ? u : "Non trouvé"));

        System.out.println("\nTous les utilisateurs:");
        dao.obtenirTous().forEach(System.out::println);

        // Recherche par rôle
        System.out.println("\nTest RECHERCHE par rôle (ADMIN):");
        dao.obtenirParRole("ADMIN").forEach(System.out::println);

        // DELETE
        System.out.println("\nTest DELETE:");
        dao.supprimer(pharma.getId());
        System.out.println("Utilisateurs restants:");
        dao.obtenirTous().forEach(System.out::println);
    }

    private static void testerCRUDVentes() {
        VenteDAO dao = new VenteDAO();

        // CREATE
        System.out.println("\nTest CREATE:");
        String suffix = String.valueOf(System.currentTimeMillis());
        String produitId = "VENTE_PROD_" + suffix;
        ProduitDAO produitDAO = new ProduitDAO();
        produitDAO.ajouter(new Produit(produitId, "Produit Vente", "Test", 40, 9.9));

        Vente v1 = new Vente("VENTE_TEST1_" + suffix, produitId, 5, 12.5);
        Vente v2 = new Vente("VENTE_TEST2_" + suffix, produitId, 3, 7.5);
        dao.enregistrer(v1);
        dao.enregistrer(v2);

        // READ
        System.out.println("\nTest READ:");
        Vente v = dao.obtenirParId(v1.getId());
        System.out.println("Vente trouvée: " + (v != null ? v : "Non trouvée"));

        System.out.println("\nToutes les ventes:");
        dao.obtenirTous().forEach(System.out::println);

        // Ventes par produit
        System.out.println("\nTest RECHERCHE ventes pour produit " + v1.getProduitId() + ":");
        dao.obtenirParProduit(v1.getProduitId()).forEach(System.out::println);

        // DELETE
        System.out.println("\nTest DELETE:");
        dao.supprimer(v2.getId());
        System.out.println("Ventes restantes:");
        dao.obtenirTous().forEach(System.out::println);
    }

    private static void testerSynchronisation() {
        ProduitDAO dao = new ProduitDAO();

        System.out.println("\n  Lancement de 3 threads pour tester la synchronisation...");

        Thread t1 = new Thread(() -> {
            Produit p = new Produit("SYNC1", "Produit Thread 1", "Test", 10, 5.0);
            dao.ajouter(p);
        });

        Thread t2 = new Thread(() -> {
            Produit p = new Produit("SYNC2", "Produit Thread 2", "Test", 20, 10.0);
            dao.ajouter(p);
        });

        Thread t3 = new Thread(() -> {
            Produit p = new Produit("SYNC3", "Produit Thread 3", "Test", 30, 15.0);
            dao.ajouter(p);
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.err.println("Erreur threads: " + e.getMessage());
        }

        System.out.println("\nThreads terminés. Vérification des produits synchronisés:");
        dao.rechercherParNom("Thread").forEach(System.out::println);
    }

    private static void afficherBanniere() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                ║");
        System.out.println("║                        SYSTÈME DE GESTION DE PHARMACIE                         ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                           ─────────────────────────                            ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                          TEST BASE DE DONNÉES - M3                             ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                           ─────────────────────────                            ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                      Milestone 3 - Tests CRUD & Synchro                       ║");
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }
}