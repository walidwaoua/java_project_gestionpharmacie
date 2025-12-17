package model;

import java.time.LocalDateTime;

public class Vente {
    private String id;
    private String produitId;
    private int quantite;
    private double total;
    private LocalDateTime date;

    // Constructeur avec date générée automatiquement
    public Vente(String id, String produitId, int quantite, double total) {
        this.id = id;
        this.produitId = produitId;
        this.quantite = quantite;
        this.total = total;
        this.date = LocalDateTime.now();
    }

    // Constructeur avec date personnalisée (pour la lecture depuis la BDD)
    public Vente(String id, String produitId, int quantite, double total, LocalDateTime date) {
        this.id = id;
        this.produitId = produitId;
        this.quantite = quantite;
        this.total = total;
        this.date = date;
    }

    // Getters
    public String getId() { return id; }
    public String getProduitId() { return produitId; }
    public int getQuantite() { return quantite; }
    public double getTotal() { return total; }
    public LocalDateTime getDate() { return date; }

    // Setters
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setTotal(double total) { this.total = total; }

    public String toCSV() {
        return id + "," + produitId + "," + quantite + "," + total + "," + date.toString();
    }

    @Override
    public String toString() {
        return "Vente{" + id + ", produit=" + produitId + ", q=" + quantite + ", total=" + total + ", date=" + date + "}";
    }
}