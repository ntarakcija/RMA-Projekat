package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
                // textDatumObjavljivanja.setText(getContext().getString(R.string.datumObjavljivanja) + " " + knjiga.getDatumObjavljivanja());
                textDatumObjavljivanja.setText(knjiga.getDatumObjavljivanja());
            }

            if(textBrojStranica != null) {
                // textBrojStranica.setText(getContext().getString(R.string.brojStranica) + " " + Integer.toString(knjiga.getBrojStranica()));
                textBrojStranica.setText(Integer.toString(knjiga.getBrojStranica()));
            }

            if(textOpis != null) {
                // textOpis.setText(getContext().getString(R.string.opis) + " " + knjiga.getOpis());
                textOpis.setText(knjiga.getOpis());
                textOpis.setMovementMethod(new ScrollingMovementMethod());
            }

            buttonPreporuci.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = parent.getContext();
                    FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    FragmentPreporuci preporuciF = new FragmentPreporuci();
                    Bundle bundle = new Bundle();

                    bundle.putString("naziv", knjiga.getNaziv());

                    String autori = knjiga.getAutori().get(0).getImeiPrezime();

                    /*
                    for(int i = 0; i < knjiga.getAutori().size(); i++) {
                        autori = autori + knjiga.getAutori().get(i).getImeiPrezime();
                        if(i == knjiga.getAutori().size() - 1) break;
                        else autori = autori + ", ";
                    }
                    */

                    bundle.putString("autori", autori);
                    bundle.putString("slika", knjiga.getSlika().toString());
                    bundle.putString("datum objavljivanja", knjiga.getDatumObjavljivanja());
                    bundle.putString("broj stranica", Integer.toString(knjiga.getBrojStranica()));
                    bundle.putString("opis", knjiga.getOpis());

                    preporuciF.setArguments(bundle);

                    fragmentTransaction.replace(R.id.fragment, preporuciF);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }



        return v;
    }

}
