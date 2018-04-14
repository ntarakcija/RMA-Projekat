package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class KnjigaAdapter extends ArrayAdapter<Knjiga> {
    public KnjigaAdapter(@NonNull Context context, int resource, ArrayList<Knjiga> knjige) {
        super(context, resource, knjige);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflaterKnjiga;
            inflaterKnjiga = LayoutInflater.from(getContext());
            v = inflaterKnjiga.inflate(R.layout.knjiga, null);
        }

        Knjiga knjiga = getItem(position);

        if (knjiga != null) {
            ImageView imgViewNaslovna = (ImageView) v.findViewById(R.id.eNaslovna);
            TextView textNazivKnjige = (TextView) v.findViewById(R.id.eNaziv);
            TextView textImeAutora = (TextView) v.findViewById(R.id.eAutor);

            if (imgViewNaslovna != null) {
                imgViewNaslovna.setImageURI(knjiga.getLokacijaSlike());
            }

            if (textNazivKnjige != null) {
                textNazivKnjige.setText(knjiga.getNazivKnjige());
            }

            if (textImeAutora != null) {
                textImeAutora.setText(knjiga.getImeAutora());
            }
        }

        return v;
    }
}
