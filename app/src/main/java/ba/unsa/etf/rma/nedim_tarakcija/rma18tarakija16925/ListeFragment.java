package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManagerNonConfig;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;
import java.util.function.BinaryOperator;

public class ListeFragment extends android.app.Fragment {

    EditText tekstPretraga;
    Button buttonPretraga;
    Button buttonDodajKategoriju;
    Button buttonDodajKnjigu;
    Button buttonKategorije;
    Button buttonAutori;
    ListView listLista;
    ArrayAdapter<String> adapterKategorije;
    Button buttonDodajOnline;
    ArrayList<String> kat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_liste, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tekstPretraga = (EditText) getView().findViewById(R.id.tekstPretraga);
        buttonPretraga = (Button) getView().findViewById(R.id.dPretraga);
        buttonDodajKategoriju = (Button) getView().findViewById(R.id.dDodajKategoriju);
        buttonDodajKnjigu = (Button) getView().findViewById(R.id.dDodajKnjigu);
        buttonKategorije = (Button) getView().findViewById(R.id.dKategorije);
        buttonAutori = (Button) getView().findViewById(R.id.dAutori);
        listLista = (ListView) getView().findViewById(R.id.listaKategorija);
        buttonDodajOnline = (Button) getView().findViewById(R.id.dDodajOnline);

        Biblioteka b = Biblioteka.getBiblioteku();
        ArrayList<String> kategorije = b.getKategorije();
        adapterKategorije = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
        listLista.setAdapter(adapterKategorije);

        buttonKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziKategorije();
            }
        });

        buttonAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziAutore();
            }
        });

        buttonDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajKnjigu();
            }
        });

        listLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listLista.setSelected(true);
                prikaziKnjige(position);
            }
        });

        tekstPretraga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Biblioteka b = Biblioteka.getBiblioteku();
                ArrayList<String> kategorije = b.getKategorije();
                if(!kategorije.isEmpty()) {
                    adapterKategorije =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
                    listLista.setAdapter(adapterKategorije);
                    adapterKategorije.getFilter().filter(s);
                }
            }
        });


        buttonDodajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajOnline();
            }
        });

        buttonDodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajKategoriju();
            }
        });
    }

    public void prikaziKategorije() {
        //ArrayList<String> kategorije = getArguments().getStringArrayList("Kategorije");
        Biblioteka b = Biblioteka.getBiblioteku();
        ArrayList<String> kategorije = b.getKategorije();
        adapterKategorije = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
        listLista.setAdapter(adapterKategorije);
        tekstPretraga.setVisibility(View.VISIBLE);
        buttonPretraga.setVisibility(View.VISIBLE);
        buttonDodajKategoriju.setVisibility(View.VISIBLE);
    }

    public void prikaziAutore() {
        Biblioteka b = Biblioteka.getBiblioteku();
        ArrayList<Knjiga> knjige = b.getKnjige();
        ArrayList<String> sviAutori = new ArrayList<>();
        ArrayList<String> prikaz = new ArrayList<>();

        for(int i = 0; i < knjige.size(); i++) {
            ArrayList<Autor> autori = knjige.get(i).getAutori();
            for(int j = 0; j < autori.size(); j++) {
                if(!sviAutori.contains(autori.get(j).getImeiPrezime()))
                    sviAutori.add(autori.get(j).getImeiPrezime());
            }
        }

        ArrayList<Integer> brojKnjiga = new ArrayList<>();
        for(int i = 0; i < sviAutori.size(); i++) {
            int brojac = 0;
            for(int j = 0; j < knjige.size(); j++)
                for(int k = 0; k < knjige.get(j).getAutori().size(); k++)
                    if(knjige.get(j).getAutori().get(k).getImeiPrezime().equals(sviAutori.get(i)))
                        brojac++;
            brojKnjiga.add(brojac);
        }

        for(int i = 0; i < sviAutori.size(); i++) {
            prikaz.add(sviAutori.get(i) + "\n" + "Broj knjiga: " + Integer.toString(brojKnjiga.get(i)));
        }

        ArrayAdapter<String> adapterAutori = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, prikaz);
        listLista.setAdapter(adapterAutori);

        tekstPretraga.setVisibility(View.GONE);
        buttonPretraga.setVisibility(View.GONE);
        buttonDodajKategoriju.setVisibility(View.GONE);
    }

    private void dodajKnjigu() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DodavanjeKnjigeFragment dodavanjeKnjigeF = new DodavanjeKnjigeFragment();
        //Bundle bundle = new Bundle();
        //bundle.putStringArrayList("kategorije", kat);
        //dodavanjeKnjigeF.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment, dodavanjeKnjigeF);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void prikaziKnjige(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        KnjigeFragment knjigeF = new KnjigeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Kategorija", listLista.getItemAtPosition(position).toString());
        knjigeF.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment, knjigeF);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void dodajOnline() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FragmentOnline dodavanjeOnlinef = new FragmentOnline();
        //Bundle bundle = new Bundle();
        //bundle.putStringArrayList("kategorije", kat);
        //dodavanjeOnlinef.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment, dodavanjeOnlinef);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void dodajKategoriju() {
        Biblioteka b = Biblioteka.getBiblioteku();
        ArrayList<String> kategorije = b.getKategorije();
        if(!kategorije.contains(tekstPretraga.getText().toString())) {
            b.dodajKategoriju(tekstPretraga.getText().toString());
            adapterKategorije = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
            listLista.setAdapter(adapterKategorije);
            tekstPretraga.setText("");
            Toast.makeText(getActivity(), getString(R.string.kategorijaDodana), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getActivity(), getString(R.string.kategorijaPostoji), Toast.LENGTH_SHORT).show();
    }
}
