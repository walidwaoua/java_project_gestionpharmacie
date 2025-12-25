package ui;

import dao.UtilisateurDAO;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Admin;
import model.Pharmacien;
import model.Utilisateur;

public class UtilisateurView {

    public static BorderPane create(UtilisateurDAO utilisateurDAO) {
        BorderPane pane = new BorderPane();
        TableView<Utilisateur> table = buildTable(utilisateurDAO);
        VBox form = buildForm(utilisateurDAO, table);
        pane.setCenter(table);
        pane.setRight(form);
        return pane;
    }

    private static TableView<Utilisateur> buildTable(UtilisateurDAO utilisateurDAO) {
        TableView<Utilisateur> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(utilisateurDAO.obtenirTous()));

        TableColumn<Utilisateur, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getId()));

        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNom()));

        TableColumn<Utilisateur, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getRole()));

        table.getColumns().addAll(idCol, nomCol, roleCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private static VBox buildForm(UtilisateurDAO utilisateurDAO, TableView<Utilisateur> table) {
        TextField id = new TextField();
        TextField nom = new TextField();
        ComboBox<String> role = new ComboBox<>(FXCollections.observableArrayList("ADMIN", "PHARMACIEN", "AUTRE"));
        role.getSelectionModel().select("AUTRE");

        Button ajouter = new Button("Ajouter");
        ajouter.setOnAction(e -> {
            String r = role.getValue();
            if (r == null) { showAlert("Choisir un role"); return; }
            Utilisateur u = switch (r) {
                case "ADMIN" -> new Admin(id.getText(), nom.getText());
                case "PHARMACIEN" -> new Pharmacien(id.getText(), nom.getText());
                default -> new Utilisateur(id.getText(), nom.getText(), r);
            };
            utilisateurDAO.ajouter(u);
            table.setItems(FXCollections.observableArrayList(utilisateurDAO.obtenirTous()));
            id.clear(); nom.clear(); role.getSelectionModel().select("AUTRE");
        });

        Button supprimer = new Button("Supprimer selection");
        supprimer.setOnAction(e -> {
            Utilisateur sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                utilisateurDAO.supprimer(sel.getId());
                table.setItems(FXCollections.observableArrayList(utilisateurDAO.obtenirTous()));
            }
        });

        Button rafraichir = new Button("Rafraichir");
        rafraichir.setOnAction(e -> table.setItems(FXCollections.observableArrayList(utilisateurDAO.obtenirTous())));

        GridPane grid = new GridPane();
        grid.setHgap(6); grid.setVgap(6);
        grid.addRow(0, new Label("ID:"), id);
        grid.addRow(1, new Label("Nom:"), nom);
        grid.addRow(2, new Label("Role:"), role);

        return new VBox(8, grid, ajouter, supprimer, rafraichir);
    }

    private static void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.showAndWait();
    }
}

