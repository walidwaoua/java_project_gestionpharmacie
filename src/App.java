import database.DatabaseManager;
import dao.ProduitDAO;
import dao.UtilisateurDAO;
import dao.VenteDAO;
import model.Admin;
import model.Pharmacien;
import model.Produit;
import model.Vente;
import service.ProduitService;
import utils.FilePaths;

import java.util.Scanner;
import java.util.UUID;

public class App {

    public static void main(String[] args) {
        afficherBanniere();

        // Initialiser les tables (sans supprimer les données CSV existantes côté fichiers)
        DatabaseManager.initialiserBDD();

        ProduitDAO produitDAO = new ProduitDAO();
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        VenteDAO venteDAO = new VenteDAO();

        // Charger les produits CSV dans SQLite (remplace si même id)
        System.out.println("Chargement des produits depuis le CSV vers SQLite...");
        int charges = importerProduitsDepuisCSV(produitDAO);
        System.out.println("Produits charges: " + charges);

        // Boucle de menu interactive
        try (Scanner scanner = new Scanner(System.in)) {
            boolean quitter = false;
            while (!quitter) {
                afficherMenu();
                String choix = scanner.nextLine().trim();
                switch (choix) {
                    case "1" -> listerProduits(produitDAO);
                    case "2" -> ajouterProduit(scanner, produitDAO);
                    case "3" -> enregistrerVente(scanner, produitDAO, venteDAO);
                    case "4" -> listerVentes(venteDAO);
                    case "5" -> ajouterUtilisateur(scanner, utilisateurDAO);
                    case "6" -> listerUtilisateurs(utilisateurDAO);
                    case "0" -> quitter = true;
                    default -> System.out.println("Choix invalide");
                }
            }
        }

        DatabaseManager.fermerConnexion();
        System.out.println("\nAu revoir.");
    }

    private static void afficherMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1) Lister les produits");
        System.out.println("2) Ajouter un produit");
        System.out.println("3) Enregistrer une vente");
        System.out.println("4) Lister les ventes");
        System.out.println("5) Ajouter un utilisateur");
        System.out.println("6) Lister les utilisateurs");
        System.out.println("0) Quitter");
        System.out.print("Votre choix: ");
    }

    private static void listerProduits(ProduitDAO produitDAO) {
        System.out.println("\nProduits disponibles:");
        produitDAO.obtenirTous().forEach(System.out::println);
    }

    private static void ajouterProduit(Scanner scanner, ProduitDAO produitDAO) {
        System.out.print("Id produit: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nom: ");
        String nom = scanner.nextLine().trim();
        System.out.print("Categorie: ");
        String cat = scanner.nextLine().trim();
        System.out.print("Quantite: ");
        int qte = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Prix: ");
        double prix = Double.parseDouble(scanner.nextLine().trim());
        produitDAO.ajouter(new Produit(id, nom, cat, qte, prix));
    }

    private static void enregistrerVente(Scanner scanner, ProduitDAO produitDAO, VenteDAO venteDAO) {
        System.out.print("Id produit: ");
        String prodId = scanner.nextLine().trim();
        var prod = produitDAO.obtenirParId(prodId);
        if (prod == null) {
            System.out.println("Produit introuvable");
            return;
        }
        System.out.print("Quantite vendue: ");
        int qte = Integer.parseInt(scanner.nextLine().trim());
        if (qte > prod.getQuantite()) {
            System.out.println("Stock insuffisant");
            return;
        }
        // UUID limite les collisions d'ID en cas de relance/tests répétés
        String venteId = "V_" + UUID.randomUUID();
        Vente v = new Vente(venteId, prodId, qte, prod.getPrix() * qte);
        if (venteDAO.enregistrer(v)) {
            prod.setQuantite(prod.getQuantite() - qte);
            produitDAO.mettreAJour(prod);
            System.out.println("Vente enregistree");
        }
    }

    private static void listerVentes(VenteDAO venteDAO) {
        System.out.println("\nVentes:");
        venteDAO.obtenirTous().forEach(System.out::println);
        System.out.println("Total ventes: " + venteDAO.calculerTotalVentes());
    }

    private static void ajouterUtilisateur(Scanner scanner, UtilisateurDAO utilisateurDAO) {
        System.out.print("Id utilisateur: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nom: ");
        String nom = scanner.nextLine().trim();
        System.out.print("Role (ADMIN/PHARMACIEN/autre): ");
        String role = scanner.nextLine().trim().toUpperCase();
        switch (role) {
            case "ADMIN" -> utilisateurDAO.ajouter(new Admin(id, nom));
            case "PHARMACIEN" -> utilisateurDAO.ajouter(new Pharmacien(id, nom));
            default -> utilisateurDAO.ajouter(new model.Utilisateur(id, nom, role));
        }
    }

    private static void listerUtilisateurs(UtilisateurDAO utilisateurDAO) {
        System.out.println("\nUtilisateurs:");
        utilisateurDAO.obtenirTous().forEach(System.out::println);
    }

    private static int importerProduitsDepuisCSV(ProduitDAO produitDAO) {
        ProduitService ps = new ProduitService(FilePaths.PRODUITS);
        ps.chargerDepuisCSV();
        int count = 0;
        for (Produit p : ps.getProduits()) {
            if (produitDAO.ajouter(p)) {
                count++;
            }
        }
        return count;
    }

    private static void afficherBanniere() {
        System.out.println("\nPharmacie - Console interactive");
        System.out.println("-------------------------------");
    }
}

