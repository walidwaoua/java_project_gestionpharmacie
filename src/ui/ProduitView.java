package ui;

import dao.ProduitDAO;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Produit;

public class ProduitView {

    public static BorderPane create(ProduitDAO produitDAO) {
        BorderPane pane = new BorderPane();
        TableView<Produit> table = buildTable(produitDAO);

        VBox form = buildForm(produitDAO, table);
        form.setPadding(new Insets(10));
        form.setSpacing(8);

        pane.setCenter(table);
        pane.setRight(form);
        return pane;
    }

    private static TableView<Produit> buildTable(ProduitDAO produitDAO) {
        TableView<Produit> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(produitDAO.obtenirTous()));

        TableColumn<Produit, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getId()));

        TableColumn<Produit, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNom()));

        TableColumn<Produit, String> catCol = new TableColumn<>("Categorie");
        catCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCategorie()));

        TableColumn<Produit, Number> qteCol = new TableColumn<>("Quantite");
        qteCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getQuantite()));

        TableColumn<Produit, Number> prixCol = new TableColumn<>("Prix");
        prixCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getPrix()));

        table.getColumns().addAll(idCol, nomCol, catCol, qteCol, prixCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private static VBox buildForm(ProduitDAO produitDAO, TableView<Produit> table) {
        TextField id = new TextField();
        TextField nom = new TextField();
        TextField cat = new TextField();
        TextField qte = new TextField();
        TextField prix = new TextField();

        Button ajouter = new Button("Ajouter/Remplacer");
        ajouter.setOnAction(e -> {
            try {
                Produit p = new Produit(id.getText(), nom.getText(), cat.getText(), Integer.parseInt(qte.getText()), Double.parseDouble(prix.getText()));
                produitDAO.ajouter(p);
                table.setItems(FXCollections.observableArrayList(produitDAO.obtenirTous()));
                clear(id, nom, cat, qte, prix);
            } catch (NumberFormatException ex) {
                showAlert("Saisie invalide");
            }
        });

        Button supprimer = new Button("Supprimer");
        supprimer.setOnAction(e -> {
            Produit sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                produitDAO.supprimer(sel.getId());
                table.setItems(FXCollections.observableArrayList(produitDAO.obtenirTous()));
            }
        });

        Button rafraichir = new Button("Rafraichir");
        rafraichir.setOnAction(e -> table.setItems(FXCollections.observableArrayList(produitDAO.obtenirTous())));

        GridPane grid = new GridPane();
        grid.setHgap(6); grid.setVgap(6);
        grid.addRow(0, new Label("ID:"), id);
        grid.addRow(1, new Label("Nom:"), nom);
        grid.addRow(2, new Label("Categorie:"), cat);
        grid.addRow(3, new Label("Quantite:"), qte);
        grid.addRow(4, new Label("Prix:"), prix);

        VBox box = new VBox(8, grid, ajouter, supprimer, rafraichir);
        return box;
    }

    private static void clear(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    private static void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.showAndWait();
    }
}

