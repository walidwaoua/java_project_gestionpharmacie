package service;

import model.Vente;
import utils.CSVUtils;
import java.io.IOException;
import java.util.*;

public class VenteService {
    private final String path;
    private final List<Vente> ventes = new ArrayList<>();

    public VenteService(String path) { this.path = path; }

    public void enregistrer(Vente v) {
        ventes.add(v);
        try {
            CSVUtils.appendCSV(path, v.toCSV().split(","));
        } catch (IOException e) {
            System.out.println("Erreur écriture vente: " + e.getMessage());
        }
    }
}

