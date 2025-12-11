package model;

public class Utilisateur {
    private String id;
    private String nom;
    private String role;

    public Utilisateur(String id, String nom, String role) {
        this.id = id;
        this.nom = nom;
        this.role = role;
    }

    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return id + "," + nom + "," + role;
    }
}
