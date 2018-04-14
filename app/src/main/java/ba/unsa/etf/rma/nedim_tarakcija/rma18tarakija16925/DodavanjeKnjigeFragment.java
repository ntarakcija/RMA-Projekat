package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

        kategorije = getArguments().getStringArrayList("Kategorije");
        ArrayAdapter<String> adapterKategorije =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, kategorije);
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ListeFragment listeFragmentF = new ListeFragment();
        fragmentTransaction.replace(R.id.fragment, listeFragmentF);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void upisiKnjigu() {
        if(imeAutora.getText().toString().equals("") || nazivKnjige.getText().toString().equals("")) {
            Toast.makeText(getActivity(),"Niste unijeli sve potrebne podatke.", Toast.LENGTH_SHORT).show();
        }
        else {
            Biblioteka biblioteka = Biblioteka.getBiblioteku();
            biblioteka.dodajKnjigu(imeAutora.getText().toString(), nazivKnjige.getText().toString(), spinerKategorije.getSelectedItem().toString(), lokacijaSlike);
            Toast.makeText(getActivity(),"Dodali ste novu knjigu.", Toast.LENGTH_SHORT).show();
            imeAutora.setText("");
            nazivKnjige.setText("");
        }
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
