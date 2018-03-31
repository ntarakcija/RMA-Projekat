package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);

        Button buttonDodajKnjigu = findViewById(R.id.dDodajKnjigu);
        ListView listListaKategorija = findViewById(R.id.listaKategorija);

        final ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

        ArrayAdapter<CharSequence> adapterKategorije = ArrayAdapter.createFromResource(this, R.array.kategorije, android.R.layout.simple_list_item_1);
        listListaKategorija.setAdapter(adapterKategorije);

        buttonDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), DodavanjeKnjigeAkt.class), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            String returnValue = data.getStringExtra("imeAutora");
            String nazivKnjige = data.getStringExtra("nazivKnjige");
            String kategorija = data.getStringExtra("kategorija");
            Uri ucitanaSlika = Uri.parse(data.getStringExtra("uri"));
        }

    }
}
