package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaKnjigaAkt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);

        //Toast.makeText(getBaseContext(), "Ušao uredu.", Toast.LENGTH_SHORT).show();
        //ArrayList<Knjiga> knjige;
        //knjige = (ArrayList) getIntent().getParcelableArrayListExtra("knjige");
        ArrayList<Knjiga> knjige = (ArrayList<Knjiga>) getIntent().getSerializableExtra("knjige");

        if(knjige != null && !knjige.isEmpty()) {
            if (knjige.get(0).getImeAutora().equals("Nedim")) {
                Toast.makeText(getBaseContext(), "Sve uredu.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getBaseContext(), "Prazan - Sve uredu.", Toast.LENGTH_SHORT).show();
        }

    }
}
