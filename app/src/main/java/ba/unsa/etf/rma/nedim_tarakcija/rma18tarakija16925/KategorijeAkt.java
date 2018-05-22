package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class KategorijeAkt extends AppCompatActivity {
    ArrayList<String> kategorije;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);

        kategorije = new ArrayList<String>();
        kategorije.add("Roman");
        kategorije.add("Novela");
        kategorije.add("Triler");
        kategorije.add("Horor");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ListeFragment listeF = new ListeFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("Kategorije", kategorije);
        listeF.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment, listeF);
        fragmentTransaction.commit();
    }

}
