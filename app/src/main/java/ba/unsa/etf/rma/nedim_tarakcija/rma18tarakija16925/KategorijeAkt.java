package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class KategorijeAkt extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        setContentView(R.layout.activity_kategorije_akt);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue2));

        Configuration config = getResources().getConfiguration();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FrameLayout fragment2 = (FrameLayout) findViewById(R.id.fragment2);

        if(fragment2 != null) {
            KnjigeFragment kf = (KnjigeFragment) fragmentManager.findFragmentById(R.id.fragment2);
            if(kf != null) {
                kf = new KnjigeFragment();
                fragmentTransaction.replace(R.id.fragment2, kf, "KnjigaFragment");
                fragmentTransaction.commit();
            }
        }

        ListeFragment listeF = (ListeFragment) fragmentManager.findFragmentByTag("ListeFragment");
        if(listeF == null) {
            listeF = new ListeFragment();
            fragmentTransaction.replace(R.id.fragment, listeF, "ListeFragment");
            fragmentTransaction.commit();
        }
        else {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

}
