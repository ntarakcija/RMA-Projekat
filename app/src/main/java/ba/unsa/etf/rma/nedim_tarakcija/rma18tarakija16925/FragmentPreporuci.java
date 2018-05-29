package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FragmentPreporuci extends android.app.Fragment {
    TextView autor;
    TextView naziv;
    TextView datumObjavljivanja;
    TextView brojStranica;
    TextView opis;
    Button buttonPosalji;
    Spinner spinnerKontakti;
    ImageView slika;
    ArrayList<String> emailovi;
    ArrayList<String> imena;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preporuci, container, false);

        autor = (TextView) v.findViewById(R.id.autor);
        naziv = (TextView) v.findViewById(R.id.naziv);
        datumObjavljivanja = (TextView) v.findViewById(R.id.datumObjavljivanja);
        opis = (TextView) v.findViewById(R.id.opis);
        brojStranica = (TextView) v.findViewById(R.id.brojStranica);
        buttonPosalji = (Button) v.findViewById(R.id.dPosalji);
        spinnerKontakti = (Spinner) v.findViewById(R.id.sKontakti);
        slika = (ImageView) v.findViewById(R.id.naslovna);

        naziv.setText(getArguments().getString("naziv"));
        autor.setText(getArguments().getString("autori"));
        datumObjavljivanja.setText(getArguments().getString("datum objavljivanja"));
        brojStranica.setText(getArguments().getString("broj stranica"));
        opis.setText(getArguments().getString("opis"));
        Picasso.get().load(getArguments().getString("slika")).into(slika);

        emailovi = new ArrayList<>();
        imena = new ArrayList<>();

        ucitajEmailove();

        ArrayAdapter<String> adapterKategorije =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, emailovi);
        spinnerKontakti.setAdapter(adapterKategorije);

        buttonPosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posaljiEmail();
            }
        });

        return v;
    }

    void ucitajEmailove() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);

        //while(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Cursor email = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
                        null, null);

                while(email.moveToNext()) {
                    String emailAdress = email.getString(email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    imena.add(name);
                    emailovi.add(emailAdress);
                }
                email.close();
            }
        //}
        cursor.close();
    }

    void posaljiEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        String[] TO = {spinnerKontakti.getSelectedItem().toString()};
        String[] CC = {spinnerKontakti.getSelectedItem().toString()};
        String text = "Zdravo " + imena.get(spinnerKontakti.getSelectedItemPosition()) + ",\n" +
                "Pročitaj knjigu " + getArguments().getString("naziv") + " od " + getArguments().getString("autori")
                + "!";

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Preporuka za knjigu");
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            Toast.makeText(getActivity(), "Finished sending email", Toast.LENGTH_SHORT).show();
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }

}

