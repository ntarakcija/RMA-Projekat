package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class DodavanjeKnjigeFragment extends android.app.Fragment {
    Spinner spinerKategorije;
    EditText imeAutora;
    EditText nazivKnjige;
    Button buttonNadjiSliku;
    Button buttonUpisiKnjigu;
    Button buttonPonisti;
    ImageView imgViewNaslovnaStrana;
    String kategorija;
    ArrayList<String> kategorije;
    Bitmap slika;
    Uri lokacijaSlike;
    BazaOpenHelper helper;
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dodavanje_knjige, container, false);

        spinerKategorije = (Spinner) v.findViewById(R.id.sKategorijaKnjige);
        imeAutora = (EditText) v.findViewById(R.id.imeAutora);
        nazivKnjige = (EditText) v.findViewById(R.id.nazivKnjige);
        buttonNadjiSliku = (Button) v.findViewById(R.id.dNadjiSliku);
        buttonUpisiKnjigu = (Button) v.findViewById(R.id.dUpisiKnjigu);
        buttonPonisti = (Button) v.findViewById(R.id.dPonisti);
        imgViewNaslovnaStrana = (ImageView) v.findViewById(R.id.naslovnaStr);

        // Dohvatanje kategorija iz baze
        helper = new BazaOpenHelper(getActivity());
        db = helper.getReadableDatabase();

        kategorije = new ArrayList<>();
        String query = "select * from " + helper.TABLE_KATEGORIJA;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            kategorije.add(cursor.getString(cursor.getColumnIndex(BazaOpenHelper.COLUMN_NAZIV)));
        }

        ArrayAdapter<String> adapterKategorije =  new ArrayAdapter<String>(getActivity(), R.layout.kategorija, kategorije);
        spinerKategorije.setAdapter(adapterKategorije);

        buttonPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ponisti();
            }
        });

        buttonUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upisiKnjigu();
            }
        });

        buttonNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNaslovna = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentNaslovna, 2);
            }
        });

        return v;
    }


    private void ponisti() {
        FragmentManager fragmentManager = getFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //ListeFragment listeFragmentF = new ListeFragment();
        //fragmentTransaction.replace(R.id.fragment, listeFragmentF);
        fragmentManager.popBackStack();
        //fragmentTransaction.addToBackStack(null);
        //fragmentTransaction.commit();
    }

    private void upisiKnjigu() {
        /*
        if(imeAutora.getText().toString().equals("") || nazivKnjige.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.nedovoljnoPodataka), Toast.LENGTH_SHORT).show();
        }
        else {
            Biblioteka b = Biblioteka.getBiblioteku();
            if(b.getKategorije().isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.kategorijePrazne), Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<Autor> autori = new ArrayList<Autor>();
            autori.add(new Autor(imeAutora.getText().toString()));
            Knjiga knjiga = new Knjiga();
            knjiga.setId("");
            knjiga.setNaziv(nazivKnjige.getText().toString());
            knjiga.setAutori(autori);
            knjiga.setOpis("");
            knjiga.setDatumObjavljivanja("");
            knjiga.setLokacijaSlike(lokacijaSlike);
            knjiga.setKategorija(spinerKategorije.getSelectedItem().toString());
            knjiga.setNacinDodavanja("ofline");

            b.dodajKnjigu(knjiga);
            Toast.makeText(getActivity(), getString(R.string.dodanaNovaKnjiga),Toast.LENGTH_SHORT).show();
            imeAutora.setText("");
            nazivKnjige.setText("");
        }
        */

        Toast.makeText(getActivity(), getString(R.string.dodavanjeOfflineError), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            lokacijaSlike = data.getData();
            imgViewNaslovnaStrana.setImageURI(lokacijaSlike);
        }
    }
}
