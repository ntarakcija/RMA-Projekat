package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class KnjigaAdapter extends ArrayAdapter<Knjiga> {
    public KnjigaAdapter(@NonNull Context context, int resource, ArrayList<Knjiga> knjige) {
        super(context, resource, knjige);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflaterKnjiga;
            inflaterKnjiga = LayoutInflater.from(getContext());
            v = inflaterKnjiga.inflate(R.layout.knjiga, null);
        }

        final Knjiga knjiga = getItem(position);
        final Button preporuci = (Button) v.findViewById(R.id.dPreporuci);

        if (knjiga != null) {
            ImageView imgViewNaslovna = (ImageView) v.findViewById(R.id.eNaslovna);
            TextView textNazivKnjige = (TextView) v.findViewById(R.id.eNaziv);
            TextView textImeAutora = (TextView) v.findViewById(R.id.eAutor);
            TextView textDatumObjavljivanja = (TextView) v.findViewById(R.id.eDatumObjavljivanja);
            TextView textBrojStranica = (TextView) v.findViewById(R.id.eBrojStranica);
            TextView textOpis = (TextView) v.findViewById(R.id.eOpis);
            Button buttonPreporuci = (Button) v.findViewById(R.id.dPreporuci);
            LinearLayout pozadina = (LinearLayout) v.findViewById(R.id.layout);
            final CheckBox cbProcitana = (CheckBox) v.findViewById(R.id.cbProcitana);
            final Button buttonObrisi = (Button) v.findViewById(R.id.dObrisi);

            final BazaOpenHelper helper;
            SQLiteDatabase db;
            helper = new BazaOpenHelper(getContext());
            db = helper.getReadableDatabase();

            if(pozadina != null) {
                if(knjiga.getPregledana() == 1) {
                    //v.setBackgroundColor(v.getResources().getColor(R.color.lightBlue));
                    cbProcitana.setChecked(true);
                }
                else {
                    v.setBackgroundColor(Color.TRANSPARENT);
                    cbProcitana.setChecked(false);
                }
            }

            if (imgViewNaslovna != null) {
                //if(knjiga.getNacinDodavanja().equals("ofline"))
                  //  imgViewNaslovna.setImageURI(knjiga.getLokacijaSlike());
                //else {
                if(knjiga.getSlika() != null)
                    Picasso.get().load(knjiga.getSlika().toString()).into(imgViewNaslovna);
                //}
            }

            if (textNazivKnjige != null) {
                textNazivKnjige.setText(knjiga.getNaziv());
            }

            if (textImeAutora != null) {
                String autori = "";
                for(int i = 0; i < knjiga.getAutori().size(); i++) {
                    autori = autori + knjiga.getAutori().get(i).getImeiPrezime();
                    if(i == knjiga.getAutori().size() - 1) break;
                    else autori = autori + ", ";
                }
                textImeAutora.setText(autori);
            }

            if(textDatumObjavljivanja != null) {
                textDatumObjavljivanja.setText(knjiga.getDatumObjavljivanja());
            }

            if(textBrojStranica != null) {
                textBrojStranica.setText(Integer.toString(knjiga.getBrojStranica()));
            }

            if(textOpis != null) {
                textOpis.setText(knjiga.getOpis());
            }

            buttonPreporuci.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]
                                {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else {
                        final Context context = parent.getContext();
                        FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        FragmentPreporuci preporuciF = new FragmentPreporuci();
                        Bundle bundle = new Bundle();

                        bundle.putString("naziv", knjiga.getNaziv());

                        String autori = knjiga.getAutori().get(0).getImeiPrezime();

                        bundle.putString("autori", autori);
                        bundle.putString("slika", knjiga.getSlika().toString());
                        bundle.putString("datum objavljivanja", knjiga.getDatumObjavljivanja());
                        bundle.putString("broj stranica", Integer.toString(knjiga.getBrojStranica()));
                        bundle.putString("opis", knjiga.getOpis());

                        preporuciF.setArguments(bundle);

                        Configuration config = getContext().getResources().getConfiguration();

                        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            fragmentTransaction.replace(R.id.fragment2, preporuciF, "PreporuciFragment");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        else {
                            fragmentTransaction.replace(R.id.fragment, preporuciF);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                }
            });

            final View finalV = v;
            cbProcitana.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cbProcitana.isChecked()) {
                        helper.oznaciKnjigu(knjiga, true);
                        //finalV.setBackgroundColor(finalV.getResources().getColor(R.color.lightBlue));
                    }
                    else {
                        helper.oznaciKnjigu(knjiga, false);
                        //finalV.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            });

            buttonObrisi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.obrisiKnjigu(knjiga.getId());
                    Toast.makeText(getContext(), getContext().getString(R.string.knjigaObrisana), Toast.LENGTH_SHORT).show();
                    final Context context = parent.getContext();
                    FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                    fragmentManager.popBackStack();
                }
            });
        }

        return v;
    }
}
