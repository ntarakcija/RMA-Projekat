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
import android.widget.Toast;

import java.util.ArrayList;

public class KnjigeFragment extends android.app.Fragment {
    Button buttonPovratak;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_knjige, container, false);

        buttonPovratak = (Button) v.findViewById(R.id.dPovratak);

        Biblioteka biblioteka = Biblioteka.getBiblioteku();
        ArrayList<Knjiga> knjige = biblioteka.getKnjige();
        ArrayList<Knjiga> filtriraneKnjige = new ArrayList<Knjiga>();

        for(int i = 0; i < knjige.size(); i++) {
            if(knjige.get(i).getKategorija().equals(getArguments().getString("Kategorija")))
                filtriraneKnjige.add(knjige.get(i));
        }

        ListView listaKnjiga = (ListView) v.findViewById(R.id.listaKnjiga);
        ListAdapter adapterKnjige = new KnjigaAdapter(getActivity(), R.layout.knjiga, filtriraneKnjige);
        listaKnjiga.setAdapter(adapterKnjige);

        //ArrayList<String> naziviKnjiga = new ArrayList<String>();
        //for(int i = 0; i < knjige.size(); i++)
          //  naziviKnjiga.add(knjige.get(i).getNaziv());

        //ArrayAdapter<String> adapterRezultat = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, naziviKnjiga);
        //listaKnjiga.setAdapter(adapterRezultat);


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
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //ListeFragment listeF = new ListeFragment();
        fragmentManager.popBackStack();
        //fragmentTransaction.replace(R.id.fragment, listeF);
        //fragmentTransaction.addToBackStack(null);
        //fragmentTransaction.commit();

    }

}
