package model;

public class Produit {
    private String id;
    private String nom;
    private String categorie;
    private int quantite;
    private double prix;

    public Produit(String id, String nom, String categorie, int quantite, double prix) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.quantite = quantite;
        this.prix = prix;
    }

    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getCategorie() { return categorie; }
    public int getQuantite() { return quantite; }
    public double getPrix() { return prix; }

    public void setQuantite(int q) { this.quantite = q; }

    @Override
    public String toString() {
        return id + "," + nom + "," + categorie + "," + quantite + "," + prix;
    }
}
