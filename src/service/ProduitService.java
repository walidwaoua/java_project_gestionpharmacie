package service;

import model.Produit;
import utils.CSVUtils;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ProduitService {
    private final List<Produit> produits = new ArrayList<>();
    private final String path;

    public ProduitService(String csvPath) {
        this.path = csvPath;
    }

    public void chargerDepuisCSV() {
        try {
            List<String[]> rows = CSVUtils.readCSV(path);
            for (String[] r : rows) {
                if (r.length < 5) continue;
                produits.add(new Produit(r[0], r[1], r[2], Integer.parseInt(r[3]), Double.parseDouble(r[4])));
            }
        } catch (IOException e) {
            System.out.println("Erreur chargement produits : " + e.getMessage());
        }
    }

    public void sauvegarderDansCSV() {
        List<String[]> rows = produits.stream()
                .map(p -> new String[]{p.getId(), p.getNom(), p.getCategorie(), String.valueOf(p.getQuantite()), String.valueOf(p.getPrix())})
                .collect(Collectors.toList());
        try {
            CSVUtils.writeCSV(path, rows);
        } catch (IOException e) {
            System.out.println("Erreur sauvegarde produits : " + e.getMessage());
        }
    }

    public List<Produit> rechercherParNom(String nom) {
        return produits.stream()
                .filter(p -> p.getNom().toLowerCase().contains(nom.toLowerCase()))
                .toList();
    }

    public List<Produit> filtrerBientotEnRupture(int seuil) {
        return produits.stream()
                .filter(p -> p.getQuantite() <= seuil)
                .sorted(Comparator.comparingInt(Produit::getQuantite))
                .toList();
    }

    public void afficherProduits() {
        produits.forEach(System.out::println);
    }

    public void ajouter(Produit p) { produits.add(p); }

    public Optional<Produit> findById(String id) {
        return produits.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public void supprimer(String id) {
        produits.removeIf(p -> p.getId().equals(id));
    }
}
