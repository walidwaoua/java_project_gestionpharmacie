package dao;

import model.Produit;
import database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {

    // CREATE - Ajouter un produit
    public synchronized boolean ajouter(Produit p) {
        String sql = "INSERT INTO produits (id, nom, categorie, quantite, prix) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getId());
            pstmt.setString(2, p.getNom());
            pstmt.setString(3, p.getCategorie());
            pstmt.setInt(4, p.getQuantite());
            pstmt.setDouble(5, p.getPrix());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Produit ajouté: " + p.getNom());
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout produit: " + e.getMessage());
            return false;
        }
    }

    // READ - Obtenir un produit par ID
    public Produit obtenirParId(String id) {
        String sql = "SELECT * FROM produits WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Produit(
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lecture produit: " + e.getMessage());
        }
        return null;
    }

    // READ - Obtenir tous les produits
    public List<Produit> obtenirTous() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produits";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produits.add(new Produit(
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lecture produits: " + e.getMessage());
        }
        return produits;
    }

    // UPDATE - Mettre à jour un produit
    public synchronized boolean mettreAJour(Produit p) {
        String sql = "UPDATE produits SET nom = ?, categorie = ?, quantite = ?, prix = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getNom());
            pstmt.setString(2, p.getCategorie());
            pstmt.setInt(3, p.getQuantite());
            pstmt.setDouble(4, p.getPrix());
            pstmt.setString(5, p.getId());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Produit mis à jour: " + p.getNom());
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur mise à jour produit: " + e.getMessage());
            return false;
        }
    }

    // DELETE - Supprimer un produit
    public synchronized boolean supprimer(String id) {
        String sql = "DELETE FROM produits WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Produit supprimé: " + id);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression produit: " + e.getMessage());
            return false;
        }
    }

    // Rechercher par nom
    public List<Produit> rechercherParNom(String nom) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produits WHERE nom LIKE ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                produits.add(new Produit(
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur recherche produit: " + e.getMessage());
        }
        return produits;
    }

    // Obtenir produits en rupture
    public List<Produit> obtenirProduitsEnRupture(int seuil) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produits WHERE quantite <= ? ORDER BY quantite ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, seuil);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                produits.add(new Produit(
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur recherche rupture: " + e.getMessage());
        }
        return produits;
    }
}