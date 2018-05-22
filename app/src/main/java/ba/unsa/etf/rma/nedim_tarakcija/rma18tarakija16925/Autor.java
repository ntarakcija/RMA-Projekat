package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class Autor {
    String imeiPrezime;
    ArrayList<String> knjige;

    Autor(String imeiPrezime, String id) {
        this.imeiPrezime = imeiPrezime;
        knjige = new ArrayList<String>();
        knjige.add(id);
    }

    Autor(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    String getImeiPrezime() {
        return this.imeiPrezime;
    }

    void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    ArrayList<String> getKnjige() {
        return this.knjige;
    }

    void dodajKnjigu(String id) {
        for(int i = 0; i < knjige.size(); i++) {
            if(knjige.get(i).equals(id)) {
                break;
            }
        }
        knjige.add(id);
    }
}
