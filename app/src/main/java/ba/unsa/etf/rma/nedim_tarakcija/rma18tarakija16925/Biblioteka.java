package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class Biblioteka {
    private static Biblioteka biblioteka = new Biblioteka();

    ArrayList<Knjiga> knjige;

    private Biblioteka() {
        knjige = new ArrayList<Knjiga>();
    }

    public static Biblioteka getBiblioteku() {
        return biblioteka;
    }

    public ArrayList<Knjiga> getKnjige() {
        return knjige;
    }

    public void dodajKnjigu(String imeAutora, String nazivKjige, String kategorija, Uri slika) {
        knjige.add(new Knjiga(imeAutora, nazivKjige, kategorija, slika));
    }
}
