package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.ArrayList;


public class Knjiga implements Parcelable {
    private String id;
    private String naziv;
    private ArrayList<Autor> autori;
    private String opis;
    private String datumObjavljivanja;
    private URL slika;
    private int brojStranica;
    String kategorija;
    Uri lokacijaSlike;
    Integer kategorijaId;
    int pregledana;

    private String nacinDodavanja;

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
    }

    public Knjiga(Knjiga knjiga) {
        this.id = knjiga.getId();
        this.naziv = knjiga.getNaziv();
        this.autori = knjiga.getAutori();
        this.opis = knjiga.getOpis();
        this.datumObjavljivanja = knjiga.getDatumObjavljivanja();
        this.slika = knjiga.getSlika();
        this.brojStranica = knjiga.getBrojStranica();
    }

    public Knjiga(String naziv) {
        this.naziv = naziv;
    }

    Knjiga(String kategorija, Uri lokacijaSlike) {
        this.kategorija = kategorija;
        this.lokacijaSlike = lokacijaSlike;
    }

    public Knjiga() {}

    void setId(String id) {
        this.id = id;
    }
    String getId() {
        return this.id;
    }

    void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    String getNaziv() {
        return this.naziv;
    }

    void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }
    ArrayList<Autor> getAutori() {
        return this.autori;
    }

    void setOpis(String opis) {
        this.opis = opis;
    }
    String getOpis() {
        return  this.opis;
    }

    void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }
    String getDatumObjavljivanja() {
        return  this.datumObjavljivanja;
    }

    void setSlika(URL slika) {
        this.slika = slika;
    }
    URL getSlika() {
        return  slika;
    }

    void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }
    int getBrojStranica() {
        return this.brojStranica;
    }

    void setNacinDodavanja(String nacinDodavanja) {
        this.nacinDodavanja = nacinDodavanja;
    }
    String getNacinDodavanja() {
        return this.nacinDodavanja;
    }

    public String getKategorija() {
        return kategorija;
    }
    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public Uri getLokacijaSlike() { return lokacijaSlike; }
    public void setLokacijaSlike(Uri lokacijaSlike) {
        this.lokacijaSlike = lokacijaSlike;
    }

    public int getKategorijaId() {
        return kategorijaId;
    }
    public void setKategorijaId(int kategorijaId) {
        this.kategorijaId = kategorijaId;
    }

    public int getPregledana() { return pregledana; }
    public void setPregledana(int pregledana) { this.pregledana = pregledana; }

    protected Knjiga(Parcel in) {
        kategorija = in.readString();
        lokacijaSlike = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kategorija);
        dest.writeParcelable(lokacijaSlike, flags);
    }
}
