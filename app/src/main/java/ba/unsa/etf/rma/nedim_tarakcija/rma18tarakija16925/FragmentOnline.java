package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentOnline extends android.app.Fragment implements
        DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, MojResultReceiver.Receiver {

    Spinner spinnerKategorije;
    EditText editTextUnos;
    Spinner spinnerRezultat;
    Button buttonPretraga;
    Button buttonDodajKnjigu;
    ArrayList<String> kategorije;
    ArrayList<Knjiga> knjigeRezultat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_online, container, false);

        spinnerKategorije = (Spinner) v.findViewById(R.id.sKategorije);
        editTextUnos = (EditText) v.findViewById(R.id.tekstUpit);
        spinnerRezultat = (Spinner) v.findViewById(R.id.sRezultat);
        buttonPretraga = (Button) v.findViewById(R.id.dRun);
        buttonDodajKnjigu = (Button) v.findViewById(R.id.dAdd);
        knjigeRezultat = new ArrayList<Knjiga>();

        Biblioteka b = Biblioteka.getBiblioteku();
        kategorije = b.getKategorije();
        ArrayAdapter<String> adapterKategorije =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, kategorije);
        spinnerKategorije.setAdapter(adapterKategorije);

        buttonPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obradiUnos();
            }
        });

        buttonDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajKnjigu();
            }
        });

        return v;
    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> knjige) {
        if(knjige.size() > 0) {
            Toast.makeText(getActivity(), Integer.toString(knjige.size()) + " " + getString(R.string.knjiga) +
                    " " + getString(R.string.pronadjeno), Toast.LENGTH_SHORT).show();

            knjigeRezultat = knjige;
            ArrayList<String> naziviKnjiga = new ArrayList<String>();

            for (int i = 0; i < knjige.size(); i++)
                naziviKnjiga.add(knjige.get(i).getNaziv());

            ArrayAdapter<String> adapterRezultat = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, naziviKnjiga);
            spinnerRezultat.setAdapter(adapterRezultat);
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.nijePronadjeno), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> knjige) {
        if(knjige.size() > 0) {
            Toast.makeText(getActivity(), Integer.toString(knjige.size()) + " " + getString(R.string.knjiga) +
                    " " + getString(R.string.pronadjeno), Toast.LENGTH_SHORT).show();

            knjigeRezultat = knjige;
            ArrayList<String> naziviKnjiga = new ArrayList<String>();

            for (int i = 0; i < knjige.size(); i++)
                naziviKnjiga.add(knjige.get(i).getNaziv());

            ArrayAdapter<String> adapterRezultat = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, naziviKnjiga);
            spinnerRezultat.setAdapter(adapterRezultat);
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.nijePronadjeno), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceiveResults(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case 0:
                Toast.makeText(getActivity(), getString(R.string.webServisPokrenut),Toast.LENGTH_SHORT).show();
                break;
            case 1:
                ArrayList<Knjiga> knjige = resultData.getParcelableArrayList("rezultat");

                if(knjige.size() > 0) {
                    Toast.makeText(getActivity(), Integer.toString(knjige.size()) + " " + getString(R.string.knjiga) +
                            " " + getString(R.string.pronadjeno), Toast.LENGTH_SHORT).show();

                    knjigeRezultat = knjige;
                    ArrayList<String> naziviKnjiga = new ArrayList<String>();

                    for (int i = 0; i < knjige.size(); i++)
                        naziviKnjiga.add(knjige.get(i).getNaziv());

                    ArrayAdapter<String> adapterRezultat = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, naziviKnjiga);
                    spinnerRezultat.setAdapter(adapterRezultat);
                }
                else {
                    Toast.makeText(getActivity(), getString(R.string.nijePronadjeno), Toast.LENGTH_SHORT).show();
                }

                break;
            case 2:
                Toast.makeText(getActivity(), getString(R.string.webServisGreska),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void obradiUnos() {
        String unos = editTextUnos.getText().toString();
        if(unos.length() >= 6) {
            // autori
            if(unos.substring(0, 6).equals("autor:")) {
                String autor = unos.substring(6);
                if(autor.isEmpty()) return;
                new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone) FragmentOnline.this).execute(autor);
            }
            // korisnici
            else if(unos.length() >= 9) {
                if (unos.substring(0, 9).equals("korisnik:")) {
                    String id = unos.substring(9);
                    if(id.isEmpty()) return;

                    Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);

                    MojResultReceiver mReceiver = new MojResultReceiver(new Handler());
                    mReceiver.setmReceiver(FragmentOnline.this);

                    intent.putExtra("id", id);
                    intent.putExtra("receiver", (ResultReceiver) mReceiver);

                    getActivity().startService(intent);
                }
                // knjige
                else {
                    String[] knjige = unos.split(";");
                    new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(knjige);
                }
            }
            // knjige
            else {
                String[] knjige = unos.split(";");
                new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(knjige);
            }
        }
        // knjige
        else {
            String[] knjige = unos.split(";");
            new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(knjige);
        }
    }

    public void dodajKnjigu() {
        Biblioteka b = Biblioteka.getBiblioteku();
        if(b.getKategorije().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.kategorijePrazne), Toast.LENGTH_SHORT).show();
            return;
        }
        int i = spinnerRezultat.getSelectedItemPosition();
        Knjiga k = new Knjiga();
        k.setId(knjigeRezultat.get(i).getId());
        k.setNaziv(knjigeRezultat.get(i).getNaziv());
        k.setAutori(knjigeRezultat.get(i).getAutori());
        k.setOpis(knjigeRezultat.get(i).getOpis());
        k.setDatumObjavljivanja(knjigeRezultat.get(i).getDatumObjavljivanja());
        k.setSlika(knjigeRezultat.get(i).getSlika());
        k.setBrojStranica(knjigeRezultat.get(i).getBrojStranica());
        k.setKategorija(spinnerKategorije.getSelectedItem().toString());
        k.setNacinDodavanja("online");
        b.dodajKnjigu(k);
        Toast.makeText(getActivity(), getString(R.string.dodanaNovaKnjiga), Toast.LENGTH_SHORT).show();
        editTextUnos.setText("");
    }
}
