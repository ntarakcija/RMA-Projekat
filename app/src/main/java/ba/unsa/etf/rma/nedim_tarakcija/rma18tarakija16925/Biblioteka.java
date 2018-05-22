package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URL;
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

    public void dodajKnjigu(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        knjige.add(new Knjiga(id, naziv, autori, opis, datumObjavljivanja, slika, brojStranica));
    }

    public void dodajKnjigu(String imeAutora, String nazivKnjige, String kategorija, Uri lokacijaSlike) {
        knjige.add(new Knjiga(imeAutora, nazivKnjige, kategorija, lokacijaSlike));
    }

    public void dodajKnjigu(Knjiga knjiga) {
        knjige.add(knjiga);
    }
}
