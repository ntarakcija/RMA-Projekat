package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class KategorijaAdapter extends ArrayAdapter<String> {

    public KategorijaAdapter(@NonNull Context context, int resource, ArrayList<String> kategorije) {
        super(context, resource, kategorije);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflaterKnjiga;
            inflaterKnjiga = LayoutInflater.from(getContext());
            v = inflaterKnjiga.inflate(R.layout.kategorija, null);
        }

        TextView textViewKategorija = (TextView) v.findViewById(R.id.kategorija);
        textViewKategorija.setText(getItem(position));

        return v;
    }
}
