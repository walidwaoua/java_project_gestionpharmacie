package database;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:pharmacie.db";
    private static Connection connection = null;

    // Créer et configurer la base de données
    public static void initialiserBDD() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("✓ Connexion à la base de données établie");
            creerTables();
        } catch (SQLException e) {
            System.err.println("❌ Erreur connexion BDD: " + e.getMessage());
        }
    }

    // Créer les tables
    private static void creerTables() {
        String[] tables = {
                // Table Produits
                """
            CREATE TABLE IF NOT EXISTS produits (
                id TEXT PRIMARY KEY,
                nom TEXT NOT NULL,
                categorie TEXT NOT NULL,
                quantite INTEGER NOT NULL,
                prix REAL NOT NULL
            )
            """,

                // Table Utilisateurs
                """
            CREATE TABLE IF NOT EXISTS utilisateurs (
                id TEXT PRIMARY KEY,
                nom TEXT NOT NULL,
                role TEXT NOT NULL
            )
            """,

                // Table Ventes
                """
            CREATE TABLE IF NOT EXISTS ventes (
                id TEXT PRIMARY KEY,
                produit_id TEXT NOT NULL,
                quantite INTEGER NOT NULL,
                total REAL NOT NULL,
                date TEXT NOT NULL,
                FOREIGN KEY (produit_id) REFERENCES produits(id)
            )
            """
        };

        try (Statement stmt = connection.createStatement()) {
            for (String sql : tables) {
                stmt.execute(sql);
            }
            System.out.println("✓ Tables créées avec succès");
        } catch (SQLException e) {
            System.err.println("❌ Erreur création tables: " + e.getMessage());
        }
    }

    // Obtenir la connexion
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération connexion: " + e.getMessage());
        }
        return connection;
    }

    // Fermer la connexion
    public static void fermerConnexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Connexion à la base de données fermée");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur fermeture connexion: " + e.getMessage());
        }
    }

    // Tester la connexion
    public static boolean testerConnexion() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}