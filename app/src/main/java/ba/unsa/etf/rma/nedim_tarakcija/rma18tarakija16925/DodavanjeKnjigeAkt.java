package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

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

public class DodavanjeKnjigeAkt extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinerKategorije;
    EditText imeAutora;
    EditText nazivKnjige;
    Button buttonNadjiSliku;
    Button buttonUpisiKnjigu;
    ImageView imgViewNaslovnaStrana;
    String kategorija;
    String lokacijaSlike;
    Uri ucitanaSlika;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);

        spinerKategorije = findViewById(R.id.sKategorijaKnjige);
        imeAutora = findViewById(R.id.imeAutora);
        nazivKnjige = findViewById(R.id.nazivKnjige);
        buttonNadjiSliku = findViewById(R.id.dNadjiSliku);
        buttonUpisiKnjigu = findViewById(R.id.dUpisiKnjigu);
        imgViewNaslovnaStrana = findViewById(R.id.naslovnaStr);

        ArrayAdapter<CharSequence> adapterKategorije = ArrayAdapter.createFromResource(this, R.array.kategorije, android.R.layout.simple_spinner_item);
        adapterKategorije.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerKategorije.setAdapter(adapterKategorije);
        spinerKategorije.setOnItemSelectedListener(this);

        buttonNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intentNaslovna = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentNaslovna, 2);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentUnosKnjige = new Intent();
        intentUnosKnjige.putExtra("imeAutora", imeAutora.getText());
        intentUnosKnjige.putExtra("nazivKnjige", nazivKnjige.getText());
        intentUnosKnjige.putExtra("kategorija", kategorija);
        intentUnosKnjige.putExtra("uri", ucitanaSlika.toString());
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
