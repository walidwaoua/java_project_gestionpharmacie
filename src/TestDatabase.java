import database.DatabaseManager;
import dao.*;
import model.*;

public class TestDatabase {

    public static void main(String[] args) {
        afficherBanniere();

        // 1. Initialiser la base de données
        System.out.println("\n" + "=".repeat(80));
        System.out.println("1️⃣  INITIALISATION DE LA BASE DE DONNÉES");
        System.out.println("=".repeat(80));
        DatabaseManager.initialiserBDD();

        // Tester la connexion
        if (DatabaseManager.testerConnexion()) {
            System.out.println("✓ Test de connexion réussi");
        } else {
            System.out.println("❌ Test de connexion échoué");
            return;
        }

        // 2. Tester les opérations CRUD sur les Produits
        System.out.println("\n" + "=".repeat(80));
        System.out.println("2️⃣  TEST DES OPÉRATIONS CRUD - PRODUITS");
        System.out.println("=".repeat(80));
        testerCRUDProduits();

        // 3. Tester les opérations CRUD sur les Utilisateurs
        System.out.println("\n" + "=".repeat(80));
        System.out.println("3️⃣  TEST DES OPÉRATIONS CRUD - UTILISATEURS");
        System.out.println("=".repeat(80));
        testerCRUDUtilisateurs();

        // 4. Tester les opérations CRUD sur les Ventes
        System.out.println("\n" + "=".repeat(80));
        System.out.println("4️⃣  TEST DES OPÉRATIONS CRUD - VENTES");
        System.out.println("=".repeat(80));
        testerCRUDVentes();

        // 5. Tester la synchronisation
        System.out.println("\n" + "=".repeat(80));
        System.out.println("5️⃣  TEST DE SYNCHRONISATION (THREADS)");
        System.out.println("=".repeat(80));
        testerSynchronisation();

        // Fermer la connexion
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🏁 FIN DES TESTS");
        System.out.println("=".repeat(80));
        DatabaseManager.fermerConnexion();
    }

    private static void testerCRUDProduits() {
        ProduitDAO dao = new ProduitDAO();

        // CREATE
        System.out.println("\n➕ Test CREATE:");
        Produit p1 = new Produit("TEST1", "Paracetamol Test", "Générique", 50, 2.5);
        Produit p2 = new Produit("TEST2", "Aspirine Test", "Générique", 30, 1.8);
        dao.ajouter(p1);
        dao.ajouter(p2);

        // READ
        System.out.println("\n📖 Test READ:");
        Produit p = dao.obtenirParId("TEST1");
        System.out.println("Produit trouvé: " + (p != null ? p : "Non trouvé"));

        System.out.println("\n📋 Tous les produits:");
        dao.obtenirTous().forEach(System.out::println);

        // UPDATE
        System.out.println("\n✏️  Test UPDATE:");
        p1.setQuantite(75);
        dao.mettreAJour(p1);

        // Recherche
        System.out.println("\n🔍 Test RECHERCHE (nom contenant 'para'):");
        dao.rechercherParNom("para").forEach(System.out::println);

        System.out.println("\n⚠️  Produits en rupture (seuil <= 35):");
        dao.obtenirProduitsEnRupture(35).forEach(System.out::println);

        // DELETE
        System.out.println("\n🗑️  Test DELETE:");
        dao.supprimer("TEST2");
        System.out.println("Produits restants:");
        dao.obtenirTous().forEach(System.out::println);
    }

    private static void testerCRUDUtilisateurs() {
        UtilisateurDAO dao = new UtilisateurDAO();

        // CREATE
        System.out.println("\n➕ Test CREATE:");
        Admin admin = new Admin("ADMIN_TEST", "Admin Test");
        Pharmacien pharma = new Pharmacien("PHARMA_TEST", "Pharmacien Test");
        dao.ajouter(admin);
        dao.ajouter(pharma);

        // READ
        System.out.println("\n📖 Test READ:");
        Utilisateur u = dao.obtenirParId("ADMIN_TEST");
        System.out.println("Utilisateur trouvé: " + (u != null ? u : "Non trouvé"));

        System.out.println("\n📋 Tous les utilisateurs:");
        dao.obtenirTous().forEach(System.out::println);

        // Recherche par rôle
        System.out.println("\n🔍 Test RECHERCHE par rôle (ADMIN):");
        dao.obtenirParRole("ADMIN").forEach(System.out::println);

        // DELETE
        System.out.println("\n🗑️  Test DELETE:");
        dao.supprimer("PHARMA_TEST");
        System.out.println("Utilisateurs restants:");
        dao.obtenirTous().forEach(System.out::println);
    }

    private static void testerCRUDVentes() {
        VenteDAO dao = new VenteDAO();

        // CREATE
        System.out.println("\n➕ Test CREATE:");
        Vente v1 = new Vente("VENTE_TEST1", "TEST1", 5, 12.5);
        Vente v2 = new Vente("VENTE_TEST2", "TEST1", 3, 7.5);
        dao.enregistrer(v1);
        dao.enregistrer(v2);

        // READ
        System.out.println("\n📖 Test READ:");
        Vente v = dao.obtenirParId("VENTE_TEST1");
        System.out.println("Vente trouvée: " + (v != null ? v : "Non trouvée"));

        System.out.println("\n📋 Toutes les ventes:");
        dao.obtenirTous().forEach(System.out::println);

        // Ventes par produit
        System.out.println("\n🔍 Test RECHERCHE ventes pour produit TEST1:");
        dao.obtenirParProduit("TEST1").forEach(System.out::println);

        // Total des ventes
        System.out.println("\n💰 Total des ventes: " + dao.calculerTotalVentes() + " MAD");

        // DELETE
        System.out.println("\n🗑️  Test DELETE:");
        dao.supprimer("VENTE_TEST2");
        System.out.println("Ventes restantes:");
        dao.obtenirTous().forEach(System.out::println);
    }

    private static void testerSynchronisation() {
        ProduitDAO dao = new ProduitDAO();

        System.out.println("\n🔄 Lancement de 3 threads pour tester la synchronisation...");

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

        System.out.println("\n✓ Threads terminés. Vérification des produits synchronisés:");
        dao.rechercherParNom("Thread").forEach(System.out::println);
    }

    private static void afficherBanniere() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                ║");
        System.out.println("║                    🏥  SYSTÈME DE GESTION DE PHARMACIE  💊                     ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                           ─────────────────────────                            ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                       🗄️  TEST BASE DE DONNÉES - M3 🗄️                        ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                           ─────────────────────────                            ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                    📋 Milestone 3 - Tests CRUD & Synchro                       ║");
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }
}