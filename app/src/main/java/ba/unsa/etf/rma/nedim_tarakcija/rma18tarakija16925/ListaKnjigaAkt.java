package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaKnjigaAkt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);

        ArrayList<Knjiga> knjige = (ArrayList<Knjiga>) getIntent().getSerializableExtra("knjige");
        String kategorija = getIntent().getStringExtra("kategorija");

        ArrayList<Knjiga> filtriraneKnjige = new ArrayList<Knjiga>();

        for(int i = 0; i < knjige.size(); i++) {
            if(knjige.get(i).getKategorija().equals(kategorija)) filtriraneKnjige.add(knjige.get(i));
        }

        final ListView listaKnjiga = (ListView) findViewById(R.id.listaKnjiga);
        ListAdapter adapterKnjige = new KnjigaAdapter(this, R.layout.knjiga, filtriraneKnjige);
        listaKnjiga.setAdapter(adapterKnjige);

        Button buttonPovratak = (Button) findViewById(R.id.dPovratak);
        buttonPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent povratak = new Intent(ListaKnjigaAkt.this, KategorijeAkt.class);
                startActivity(povratak);
            }
        });

        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listaKnjiga.setSelected(true);
            }
        });
    }
}
