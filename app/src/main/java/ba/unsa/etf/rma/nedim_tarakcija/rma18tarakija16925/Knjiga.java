package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.net.Uri;

public class Knjiga {
    String imeAutora;
    String nazivKnjige;
    String kategorija;
    Uri lokacijaSlike;

    public Knjiga(String imeAutora, String nazivKnjige, String kategorija, Uri lokacijaSlike) {
        this.imeAutora = imeAutora;
        this.nazivKnjige = nazivKnjige;
        this.kategorija = kategorija;
        this.lokacijaSlike = lokacijaSlike;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    public void setNazivKnjige(String nazivKnjige) {
        this.nazivKnjige = nazivKnjige;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public void setLokacijaSlike(Uri lokacijaSlike) {
        this.lokacijaSlike = lokacijaSlike;
    }

    public Uri getLokacijaSlike() {
        return lokacijaSlike;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public String getNazivKnjige() {
        return nazivKnjige;
    }

    public String getKategorija() {
        return kategorija;
    }
}
