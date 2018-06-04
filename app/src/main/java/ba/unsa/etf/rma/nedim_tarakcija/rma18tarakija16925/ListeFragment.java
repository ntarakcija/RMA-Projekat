package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;


import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManagerNonConfig;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;
import java.util.function.BinaryOperator;

import static ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925.BazaOpenHelper.COLUMN_ID;
import static ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925.BazaOpenHelper.COLUMN_ID_AUTORA;
import static ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925.BazaOpenHelper.COLUMN_IME;
import static ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925.BazaOpenHelper.COLUMN_NAZIV;
import static ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925.BazaOpenHelper.TABLE_AUTOR;
import static ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925.BazaOpenHelper.TABLE_AUTORSTVO;
import static ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925.BazaOpenHelper.TABLE_KNJIGA;

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
    Boolean kategorijeAutori = true;
    // Čuva id-eve svih autora i šaljse id na poziciji klika na listu
    ArrayList<Integer> autoriId;

    BazaOpenHelper helper;
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_liste, container, false);
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.home);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        helper = new BazaOpenHelper(getActivity());
        db = helper.getReadableDatabase();

        tekstPretraga = (EditText) getView().findViewById(R.id.tekstPretraga);
        buttonPretraga = (Button) getView().findViewById(R.id.dPretraga);
        buttonDodajKategoriju = (Button) getView().findViewById(R.id.dDodajKategoriju);
        buttonDodajKnjigu = (Button) getView().findViewById(R.id.dDodajKnjigu);
        buttonKategorije = (Button) getView().findViewById(R.id.dKategorije);
        buttonAutori = (Button) getView().findViewById(R.id.dAutori);
        listLista = (ListView) getView().findViewById(R.id.listaKategorija);
        buttonDodajOnline = (Button) getView().findViewById(R.id.dDodajOnline);

        buttonDodajKategoriju.setEnabled(false);
        buttonDodajKategoriju.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkGray)));
        buttonDodajKategoriju.setTextColor(getResources().getColor(R.color.colorDarkGray));

        if(kategorijeAutori) {
            Biblioteka b = Biblioteka.getBiblioteku();
            ArrayList<String> kategorije = b.getKategorije();
            adapterKategorije = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
            listLista.setAdapter(adapterKategorije);
            kategorijeAutori = true;
        }
        else {
            prikaziAutore();
        }

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

        buttonPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtriraj();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(kategorijeAutori) {
            prikaziKategorije();
        }
        else {
            prikaziAutore();
        }
    }

    public void prikaziKategorije() {

        ArrayList<String> kategorije = new ArrayList<>();
        String query = "select * from " + helper.TABLE_KATEGORIJA;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            kategorije.add(cursor.getString(cursor.getColumnIndex(BazaOpenHelper.COLUMN_NAZIV)));
        }

        adapterKategorije = new KategorijaAdapter(getActivity(), R.layout.kategorija, kategorije);
        listLista.setAdapter(adapterKategorije);

        tekstPretraga.setVisibility(View.VISIBLE);
        buttonPretraga.setVisibility(View.VISIBLE);
        buttonDodajKategoriju.setVisibility(View.VISIBLE);
        kategorijeAutori = true;
    }

    public void prikaziAutore() {
        ArrayList<String> autori = new ArrayList<>();
        autoriId = new ArrayList<>();

        // Uzimanje svih autora iz baze
        String queryAutori = "select * from " + TABLE_AUTOR;
        Cursor cursorAutori = db.rawQuery(queryAutori, null);

        while(cursorAutori.moveToNext()) {
            String prikaz;
            prikaz = cursorAutori.getString(cursorAutori.getColumnIndex(COLUMN_IME));
            prikaz += "\nBroj knjiga: ";
            autoriId.add(cursorAutori.getInt(cursorAutori.getColumnIndex(COLUMN_ID)));

            // Prebrojavanje knjiga koje je napisao svaki autor
            String queryAutorstvo = "select * from " + TABLE_AUTORSTVO + " where " + COLUMN_ID_AUTORA + " = \"" +
                    cursorAutori.getInt(cursorAutori.getColumnIndex(COLUMN_ID)) + "\"";
            Cursor cursorAutorstvo = db.rawQuery(queryAutorstvo, null);

            prikaz += cursorAutorstvo.getCount();
            autori.add(prikaz);
        }

        ArrayAdapter<String> adapterAutori = new KategorijaAdapter(getActivity(), R.layout.kategorija, autori);
        listLista.setAdapter(adapterAutori);

        tekstPretraga.setVisibility(View.GONE);
        buttonPretraga.setVisibility(View.GONE);
        buttonDodajKategoriju.setVisibility(View.GONE);
        kategorijeAutori = false;
    }

    private void dodajKnjigu() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DodavanjeKnjigeFragment dodavanjeKnjigeF = new DodavanjeKnjigeFragment();

        fragmentTransaction.replace(R.id.fragment, dodavanjeKnjigeF);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void prikaziKnjige(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        KnjigeFragment knjigeF = new KnjigeFragment();
        Bundle bundle = new Bundle();

        if(kategorijeAutori) {
            bundle.putString("kategorija", listLista.getItemAtPosition(position).toString());
            bundle.putBoolean("kategorijeAutori", kategorijeAutori);
            knjigeF.setArguments(bundle);
        }
        else {
            bundle.putInt("autor", autoriId.get(position));
            bundle.putBoolean("kategorijeAutori", kategorijeAutori);
            knjigeF.setArguments(bundle);
        }

        FrameLayout fragment2 = (FrameLayout) getActivity().findViewById(R.id.fragment2);

        if(fragment2 != null) {
            fragmentTransaction.replace(R.id.fragment2, knjigeF, "KnjigaFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        else {
            fragmentTransaction.replace(R.id.fragment, knjigeF);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void dodajOnline() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FragmentOnline dodavanjeOnlinef = new FragmentOnline();

        fragmentTransaction.replace(R.id.fragment, dodavanjeOnlinef);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void dodajKategoriju() {
        if(tekstPretraga.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.praznaKategorija), Toast.LENGTH_SHORT).show();
        }
        else {
            long id = helper.dodajKategoriju(tekstPretraga.getText().toString());
            if(id == -1) {
                Toast.makeText(getActivity(), getString(R.string.kategorijaPostoji), Toast.LENGTH_SHORT).show();
                prikaziKategorije();
            }
            else {
                Toast.makeText(getActivity(), getString(R.string.kategorijaDodana), Toast.LENGTH_SHORT).show();
                prikaziKategorije();
                tekstPretraga.setText("");
                buttonDodajKategoriju.setEnabled(false);
                buttonDodajKategoriju.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDarkGray)));
                buttonDodajKategoriju.setTextColor(getResources().getColor(R.color.colorDarkGray));
                prikaziKategorije();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void filtriraj() {
        ArrayList<String> kategorije = new ArrayList<>();
        String query = "select * from " + helper.TABLE_KATEGORIJA + " where " + helper.COLUMN_NAZIV + " = \"" +
                tekstPretraga.getText().toString() + "\"";;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            kategorije.add(cursor.getString(cursor.getColumnIndex(BazaOpenHelper.COLUMN_NAZIV)));
        }

        adapterKategorije = new KategorijaAdapter(getActivity(), R.layout.kategorija, kategorije);
        listLista.setAdapter(adapterKategorije);

        if(cursor.getCount() == 0) {
            buttonDodajKategoriju.setEnabled(true);
            buttonDodajKategoriju.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue2)));
            buttonDodajKategoriju.setTextColor(getResources().getColor(R.color.colorBlue2));
        }
    }
}
