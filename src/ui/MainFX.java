package ui;

import dao.ProduitDAO;
import dao.UtilisateurDAO;
import dao.VenteDAO;
import database.DatabaseManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainFX extends Application {

    private final ProduitDAO produitDAO = new ProduitDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final VenteDAO venteDAO = new VenteDAO();

    private BorderPane root;

    @Override
    public void start(Stage stage) {
        DatabaseManager.initialiserBDD();

        root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(buildTopBar(stage));
        root.setCenter(new Label("Bienvenue dans la gestion de pharmacie"));

        stage.setTitle("Pharmacie - JavaFX");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    private ToolBar buildTopBar(Stage stage) {
        Button produitsBtn = new Button("Produits");
        produitsBtn.setOnAction(e -> root.setCenter(ProduitView.create(produitDAO)));

        Button ventesBtn = new Button("Ventes");
        ventesBtn.setOnAction(e -> root.setCenter(VenteView.create(produitDAO, venteDAO)));

        Button usersBtn = new Button("Utilisateurs");
        usersBtn.setOnAction(e -> root.setCenter(UtilisateurView.create(utilisateurDAO)));

        Button quitterBtn = new Button("Quitter");
        quitterBtn.setOnAction(e -> stage.close());

        HBox spacer = new HBox();
        spacer.setMinWidth(20);

        return new ToolBar(produitsBtn, ventesBtn, usersBtn, spacer, quitterBtn);
    }

    @Override
    public void stop() {
        DatabaseManager.fermerConnexion();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

