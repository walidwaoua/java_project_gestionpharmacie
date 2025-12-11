package model;

import java.util.*;

public class Stock {
    private final Map<String, Produit> map = new HashMap<>();

    public void add(Produit p) { map.put(p.getId(), p); }

    public Produit get(String id) { return map.get(id); }

    public Collection<Produit> getAll() { return map.values(); }

    public void decrease(String id, int q) {
        Produit p = map.get(id);
        if (p != null) {
            p.setQuantite(Math.max(0, p.getQuantite() - q));
        }
    }
}
