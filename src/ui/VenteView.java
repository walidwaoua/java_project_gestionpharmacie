package ui;

import dao.ProduitDAO;
import dao.VenteDAO;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Produit;
import model.Vente;

import java.util.UUID;

public class VenteView {

    public static BorderPane create(ProduitDAO produitDAO, VenteDAO venteDAO) {
        BorderPane pane = new BorderPane();
        TableView<Vente> table = buildTable(venteDAO);
        VBox form = buildForm(produitDAO, venteDAO, table);
        form.setPadding(new Insets(10));
        form.setSpacing(8);

        pane.setCenter(table);
        pane.setRight(form);
        return pane;
    }

    private static TableView<Vente> buildTable(VenteDAO venteDAO) {
        TableView<Vente> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(venteDAO.obtenirTous()));

        TableColumn<Vente, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getId()));

        TableColumn<Vente, String> prodCol = new TableColumn<>("Produit");
        prodCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getProduitId()));

        TableColumn<Vente, Number> qteCol = new TableColumn<>("Quantite");
        qteCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getQuantite()));

        TableColumn<Vente, Number> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getTotal()));

        TableColumn<Vente, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDate().toString()));

        table.getColumns().addAll(idCol, prodCol, qteCol, totalCol, dateCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private static VBox buildForm(ProduitDAO produitDAO, VenteDAO venteDAO, TableView<Vente> table) {
        ComboBox<Produit> produits = new ComboBox<>();
        produits.setItems(FXCollections.observableArrayList(produitDAO.obtenirTous()));
        produits.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Produit p) { return p == null ? "" : p.getId() + " - " + p.getNom() + " (Stock:" + p.getQuantite() + ")"; }
            @Override public Produit fromString(String s) { return null; }
        });

        TextField qte = new TextField();
        Label totalLabel = new Label();
        Runnable refresh = () -> {
            produits.setItems(FXCollections.observableArrayList(produitDAO.obtenirTous()));
            table.setItems(FXCollections.observableArrayList(venteDAO.obtenirTous()));
            totalLabel.setText("Total: " + venteDAO.calculerTotalVentes());
        };
        refresh.run();

        Button vendre = new Button("Enregistrer vente");
        vendre.setOnAction(e -> {
            Produit p = produits.getValue();
            if (p == null) { showAlert("Choisir un produit"); return; }
            int quantite;
            try { quantite = Integer.parseInt(qte.getText()); } catch (NumberFormatException ex) { showAlert("Quantite invalide"); return; }
            if (quantite <= 0 || quantite > p.getQuantite()) { showAlert("Stock insuffisant"); return; }
            String venteId = "V_" + UUID.randomUUID();
            double total = p.getPrix() * quantite;
            Vente v = new Vente(venteId, p.getId(), quantite, total);
            if (venteDAO.enregistrer(v)) {
                p.setQuantite(p.getQuantite() - quantite);
                produitDAO.mettreAJour(p);
                refresh.run();
                qte.clear();
            }
        });

        Button rafraichir = new Button("Rafraichir");
        rafraichir.setOnAction(e -> refresh.run());

        GridPane grid = new GridPane();
        grid.setHgap(6); grid.setVgap(6);
        grid.addRow(0, new Label("Produit:"), produits);
        grid.addRow(1, new Label("Quantite:"), qte);

        VBox box = new VBox(8, grid, vendre, rafraichir, totalLabel);
        return box;
    }

    private static void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.showAndWait();
    }
}
