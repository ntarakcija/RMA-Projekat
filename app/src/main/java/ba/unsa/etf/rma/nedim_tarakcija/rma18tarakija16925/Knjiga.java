package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


public class Knjiga implements Parcelable {
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

    protected Knjiga(Parcel in) {
        imeAutora = in.readString();
        nazivKnjige = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imeAutora);
        dest.writeString(nazivKnjige);
        dest.writeString(kategorija);
        dest.writeParcelable(lokacijaSlike, flags);
    }
}
