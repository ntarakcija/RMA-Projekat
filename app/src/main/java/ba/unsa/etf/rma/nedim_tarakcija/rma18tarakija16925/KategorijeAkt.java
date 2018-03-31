package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity {

    Button buttonDodajKnjigu;
    ListView listListaKategorija;
    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);

        buttonDodajKnjigu = (Button) findViewById(R.id.dDodajKnjigu);
        listListaKategorija = (ListView) findViewById(R.id.listaKategorija);

        ArrayAdapter<CharSequence> adapterKategorije = ArrayAdapter.createFromResource(this, R.array.kategorije, android.R.layout.simple_list_item_1);
        listListaKategorija.setAdapter(adapterKategorije);

        buttonDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(getApplicationContext(), DodavanjeKnjigeAkt.class), 1);
                Intent dodavanjeKnjige = new Intent(KategorijeAkt.this, DodavanjeKnjigeAkt.class);
                startActivityForResult(dodavanjeKnjige, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            String imeAutora = data.getStringExtra("imeAutora");
            String nazivKnjige = data.getStringExtra("nazivKnjige");
            String kategorija = data.getStringExtra("kategorija");
            Uri ucitanaSlika = Uri.parse(data.getStringExtra("uri"));

            Knjiga novaKnjiga = new Knjiga(imeAutora, nazivKnjige, kategorija, ucitanaSlika);
            knjige.add(novaKnjiga);

            Toast.makeText(getBaseContext(),"Knjiga unesena.", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == 1 && resultCode == RESULT_CANCELED){
            Toast.makeText(getBaseContext(),"Unos knjige poništen.", Toast.LENGTH_SHORT).show();
        }
        else {

        }
    }
}
