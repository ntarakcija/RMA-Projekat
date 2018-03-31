package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DodavanjeKnjigeAkt extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinerKategorije;
    EditText imeAutora;
    EditText nazivKnjige;
    Button buttonNadjiSliku;
    Button buttonUpisiKnjigu;
    Button buttonPonisti;
    ImageView imgViewNaslovnaStrana;
    String kategorija;
    String lokacijaSlike;
    Uri ucitanaSlika;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);

        spinerKategorije = (Spinner) findViewById(R.id.sKategorijaKnjige);
        imeAutora = (EditText) findViewById(R.id.imeAutora);
        nazivKnjige = (EditText) findViewById(R.id.nazivKnjige);
        buttonNadjiSliku = (Button) findViewById(R.id.dNadjiSliku);
        buttonUpisiKnjigu = (Button) findViewById(R.id.dUpisiKnjigu);
        buttonPonisti = (Button) findViewById(R.id.dPonisti);
        imgViewNaslovnaStrana = (ImageView) findViewById(R.id.naslovnaStr);
        ArrayList<String> kategorije = (ArrayList<String>) getIntent().getSerializableExtra("kategorije");

        //ArrayAdapter<String> adapterKategorije = ArrayAdapter.createFromResource(this, R.array.kategorije, android.R.layout.simple_spinner_item);
        //adapterKategorije.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterKategorije =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kategorije);
        spinerKategorije.setAdapter(adapterKategorije);
        spinerKategorije.setOnItemSelectedListener(this);

        buttonNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNaslovna = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentNaslovna, 2);
            }
        });

        buttonUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imeAutora.getText().toString().equals("") || nazivKnjige.getText().toString().equals("") || Uri.EMPTY.equals(ucitanaSlika)) {
                    Toast.makeText(getBaseContext(),"Niste unijeli sve potrebne podatke.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intentUcitajKnjigu = new Intent();
                    intentUcitajKnjigu.putExtra("imeAutora", imeAutora.getText().toString());
                    intentUcitajKnjigu.putExtra("nazivKnjige", nazivKnjige.getText().toString());
                    intentUcitajKnjigu.putExtra("kategorija", kategorija);
                    intentUcitajKnjigu.putExtra("uri", ucitanaSlika.toString());
                    setResult(RESULT_OK, intentUcitajKnjigu);
                    finish();
                }
            }
        });

        buttonPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPonisti = new Intent();
                setResult(RESULT_CANCELED, intentPonisti);
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        kategorija = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            ucitanaSlika = data.getData();
            imgViewNaslovnaStrana.setImageURI(ucitanaSlika);
        }
    }

}
