package dao;

import model.Utilisateur;
import model.Admin;
import model.Pharmacien;
import database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    // CREATE - Ajouter un utilisateur
    public synchronized boolean ajouter(Utilisateur u) {
        String sql = "INSERT INTO utilisateurs (id, nom, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getNom());
            pstmt.setString(3, u.getRole());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Utilisateur ajoute: " + u.getNom());
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur ajout utilisateur: " + e.getMessage());
            return false;
        }
    }

    // READ - Obtenir un utilisateur par ID
    public Utilisateur obtenirParId(String id) {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String nom = rs.getString("nom");

                if ("ADMIN".equals(role)) {
                    return new Admin(id, nom);
                } else if ("PHARMACIEN".equals(role)) {
                    return new Pharmacien(id, nom);
                } else {
                    return new Utilisateur(id, nom, role);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lecture utilisateur: " + e.getMessage());
        }
        return null;
    }

    // READ - Obtenir tous les utilisateurs
    public List<Utilisateur> obtenirTous() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String nom = rs.getString("nom");
                String role = rs.getString("role");

                if ("ADMIN".equals(role)) {
                    utilisateurs.add(new Admin(id, nom));
                } else if ("PHARMACIEN".equals(role)) {
                    utilisateurs.add(new Pharmacien(id, nom));
                } else {
                    utilisateurs.add(new Utilisateur(id, nom, role));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lecture utilisateurs: " + e.getMessage());
        }
        return utilisateurs;
    }

    // UPDATE - Mettre a jour un utilisateur
    public synchronized boolean mettreAJour(Utilisateur u) {
        String sql = "UPDATE utilisateurs SET nom = ?, role = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, u.getNom());
            pstmt.setString(2, u.getRole());
            pstmt.setString(3, u.getId());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Utilisateur mis a jour: " + u.getNom());
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur mise a jour utilisateur: " + e.getMessage());
            return false;
        }
    }

    // DELETE - Supprimer un utilisateur
    public synchronized boolean supprimer(String id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Utilisateur supprime: " + id);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur suppression utilisateur: " + e.getMessage());
            return false;
        }
    }

    // Rechercher par nom
    public List<Utilisateur> rechercherParNom(String nom) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE nom LIKE ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String nomUser = rs.getString("nom");
                String role = rs.getString("role");

                if ("ADMIN".equals(role)) {
                    utilisateurs.add(new Admin(id, nomUser));
                } else if ("PHARMACIEN".equals(role)) {
                    utilisateurs.add(new Pharmacien(id, nomUser));
                } else {
                    utilisateurs.add(new Utilisateur(id, nomUser, role));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche utilisateur: " + e.getMessage());
        }
        return utilisateurs;
    }

    // Obtenir par role
    public List<Utilisateur> obtenirParRole(String role) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE role = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String nom = rs.getString("nom");

                if ("ADMIN".equals(role)) {
                    utilisateurs.add(new Admin(id, nom));
                } else if ("PHARMACIEN".equals(role)) {
                    utilisateurs.add(new Pharmacien(id, nom));
                } else {
                    utilisateurs.add(new Utilisateur(id, nom, role));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche par role: " + e.getMessage());
        }
        return utilisateurs;
    }
}

