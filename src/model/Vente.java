package model;

import java.time.LocalDateTime;

public class Vente {
    private String id;
    private String produitId;
    private int quantite;
    private double total;
    private LocalDateTime date;

    public Vente(String id, String produitId, int quantite, double total) {
        this.id = id;
        this.produitId = produitId;
        this.quantite = quantite;
        this.total = total;
        this.date = LocalDateTime.now();
    }

    public String toCSV() {
        return id + "," + produitId + "," + quantite + "," + total + "," + date.toString();
    }

    @Override
    public String toString() {
        return "Vente{" + id + ", produit=" + produitId + ", q=" + quantite + ", total=" + total + ", date=" + date + "}";
    }
}
