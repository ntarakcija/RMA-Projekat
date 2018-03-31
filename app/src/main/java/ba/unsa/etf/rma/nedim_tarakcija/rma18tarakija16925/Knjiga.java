package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

public class Knjiga {
    String imeAutora;
    String nazivKnjige;
    String kategorija;

    public Knjiga(String imeAutora, String nazivKnjige, String kategorija) {
        this.imeAutora = imeAutora;
        this.nazivKnjige = nazivKnjige;
        this.kategorija = kategorija;
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
