package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_knjige, container, false);

        buttonPovratak = (Button) v.findViewById(R.id.dPovratak);
        //prosiri = (TextView) v.findViewById(R.id.eProsiri);

        Biblioteka biblioteka = Biblioteka.getBiblioteku();
        ArrayList<Knjiga> knjige = biblioteka.getKnjige();
        ArrayList<Knjiga> filtriraneKnjige = new ArrayList<Knjiga>();

        if(getArguments().getBoolean("kategorijeAutori")) {
            for (int i = 0; i < knjige.size(); i++) {
                if (knjige.get(i).getKategorija().equals(getArguments().getString("kategorija")))
                    filtriraneKnjige.add(knjige.get(i));
            }
        }
        else {
            String[] autor = getArguments().getString("autor").split("\n");
            for (int i = 0; i < knjige.size(); i++) {
                for(int j = 0; j < knjige.get(i).getAutori().size(); j++)
                    if (knjige.get(i).getAutori().get(j).getImeiPrezime().equals(autor[0]))
                        filtriraneKnjige.add(knjige.get(i));
            }
        }

        ListView listaKnjiga = (ListView) v.findViewById(R.id.listaKnjiga);
        adapterKnjige = new KnjigaAdapter(getActivity(), R.layout.knjiga, filtriraneKnjige);
        listaKnjiga.setAdapter(adapterKnjige);

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
