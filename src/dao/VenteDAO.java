package dao;

import model.Vente;
import database.DatabaseManager;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO {

    // CREATE - Enregistrer une vente
    public synchronized boolean enregistrer(Vente v) {
        String sql = "INSERT INTO ventes (id, produit_id, quantite, total, date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, v.getId());
            pstmt.setString(2, v.getProduitId());
            pstmt.setInt(3, v.getQuantite());
            pstmt.setDouble(4, v.getTotal());
            pstmt.setString(5, v.getDate().toString());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Vente enregistrée: " + v.getId());
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur enregistrement vente: " + e.getMessage());
            return false;
        }
    }

    // READ - Obtenir une vente par ID
    public Vente obtenirParId(String id) {
        String sql = "SELECT * FROM ventes WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Vente(
                        rs.getString("id"),
                        rs.getString("produit_id"),
                        rs.getInt("quantite"),
                        rs.getDouble("total"),
                        LocalDateTime.parse(rs.getString("date"))
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lecture vente: " + e.getMessage());
        }
        return null;
    }

    // READ - Obtenir toutes les ventes
    public List<Vente> obtenirTous() {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM ventes ORDER BY date DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ventes.add(new Vente(
                        rs.getString("id"),
                        rs.getString("produit_id"),
                        rs.getInt("quantite"),
                        rs.getDouble("total"),
                        LocalDateTime.parse(rs.getString("date"))
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lecture ventes: " + e.getMessage());
        }
        return ventes;
    }

    // READ - Obtenir ventes par produit
    public List<Vente> obtenirParProduit(String produitId) {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM ventes WHERE produit_id = ? ORDER BY date DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produitId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ventes.add(new Vente(
                        rs.getString("id"),
                        rs.getString("produit_id"),
                        rs.getInt("quantite"),
                        rs.getDouble("total"),
                        LocalDateTime.parse(rs.getString("date"))
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lecture ventes par produit: " + e.getMessage());
        }
        return ventes;
    }

    // DELETE - Supprimer une vente
    public synchronized boolean supprimer(String id) {
        String sql = "DELETE FROM ventes WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Vente supprimée: " + id);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression vente: " + e.getMessage());
            return false;
        }
    }

    // Calculer le total des ventes
    public double calculerTotalVentes() {
        String sql = "SELECT SUM(total) as total FROM ventes";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur calcul total: " + e.getMessage());
        }
        return 0.0;
    }
}