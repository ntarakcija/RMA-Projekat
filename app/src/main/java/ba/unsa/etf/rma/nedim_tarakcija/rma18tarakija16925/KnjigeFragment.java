package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class KnjigeFragment extends android.app.Fragment {
    Button buttonPovratak;
    TextView prosiri;
    Button buttonPreporuci;
    ListAdapter adapterKnjige;
    BazaOpenHelper helper;
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_knjige, container, false);

        buttonPovratak = (Button) v.findViewById(R.id.dPovratak);
        helper = new BazaOpenHelper(getActivity());
        db = helper.getReadableDatabase();
        //prosiri = (TextView) v.findViewById(R.id.eProsiri);

        // Dohvatanje id kategorije iz baze
        int idKategorije = 0;
        String query = "select * from " + helper.TABLE_KATEGORIJA + " where " + helper.COLUMN_NAZIV +
                " = \"" + getArguments().getString("kategorija") + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            idKategorije = cursor.getInt(cursor.getColumnIndex(helper.COLUMN_ID));
        }

        Biblioteka biblioteka = Biblioteka.getBiblioteku();
        ArrayList<Knjiga> knjige = biblioteka.getKnjige();
        ArrayList<Knjiga> filtriraneKnjige = new ArrayList<Knjiga>();

        if(getArguments().getBoolean("kategorijeAutori")) {
            filtriraneKnjige = helper.knjigeKategorije(idKategorije);
        }
        else {
            filtriraneKnjige = helper.knjigeAutora(getArguments().getInt("autor"));
        }

        if(!filtriraneKnjige.isEmpty()) {
            ListView listaKnjiga = (ListView) v.findViewById(R.id.listaKnjiga);
            adapterKnjige = new KnjigaAdapter(getActivity(), R.layout.knjiga, filtriraneKnjige);
            listaKnjiga.setAdapter(adapterKnjige);
        }

        buttonPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                povratak();
            }
        });

        return v;
    }

    private void povratak() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

}
