package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.net.ssl.HttpsURLConnection;

public class DohvatiNajnovije extends AsyncTask<String, Integer, Void> {

    public interface IDohvatiNajnovijeDone {
        public void onNajnovijeDone(ArrayList<Knjiga> knjige);
    }

    ArrayList<Knjiga> rez;
    IDohvatiNajnovijeDone pozivatelj;

    public DohvatiNajnovije(IDohvatiNajnovijeDone p) {
        pozivatelj = p;
        rez = new ArrayList<Knjiga>();
    }

    @Override
    protected Void doInBackground(String... params) {
        String query = null;
        try {
            query = URLEncoder.encode(params[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String link​ = "https://www.googleapis.com/books/v1/volumes?q=inauthor:";
        String rezCount = "&maxResults=40";
        String orderBy = "&orderBy=newest";
        String url1 = link​ + query + rezCount + orderBy;

        ArrayList<Knjiga> sveKnjige = new ArrayList<Knjiga>();
        ArrayList<String> datumi = new ArrayList<String>();

        try {

            URL url = new URL(url1);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            InputStream is = urlConnection.getInputStream();
            String rezultat = convertStreamToString(is);

            JSONObject jo = new JSONObject(rezultat);
            if(jo == null) return null;
            JSONArray items = jo.optJSONArray("items");
            if(items == null) return null;

            for (int j = 0; j < items.length(); j++) {
                JSONObject knjiga = items.optJSONObject(j);
                if(knjiga == null) continue;
                JSONObject volumeInfo = knjiga.optJSONObject("volumeInfo");
                if(volumeInfo == null) continue;
                JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                if(imageLinks == null) continue;

                // id
                String id = knjiga.optString("id");
                if(id == null) continue;

                // naziv
                String naziv = volumeInfo.optString("title");
                if(naziv == null) continue;

                // autori
                JSONArray autoriJSON = volumeInfo.optJSONArray("authors");
                if(autoriJSON == null) continue;
                ArrayList<Autor> autori = new ArrayList<Autor>();
                for(int k = 0; k < autoriJSON.length(); k++)
                    autori.add(new Autor((String) autoriJSON.get(k), id));

                // opis
                String opis = volumeInfo.optString("description");
                if(opis == null) continue;

                // datum objavljivanja
                String datumObjavljivanja = volumeInfo.optString("publishedDate");
                if(datumObjavljivanja == null) continue;

                // slika
                URL slika = new URL(imageLinks.optString("thumbnail"));
                if(slika == null) continue;

                // broj stranica
                int brojStranica = volumeInfo.optInt("pageCount");

                // Odabir knjiga samo od datog autora
                Knjiga novaKnjiga = new Knjiga(id, naziv, autori, opis, datumObjavljivanja, slika, brojStranica);
                //for(int k = 0; k < novaKnjiga.getAutori().size(); k++)
                    //if(novaKnjiga.getAutori().get(k).getImeiPrezime().equals(params[0]))
                sveKnjige.add(novaKnjiga);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        // Sortiranje po datumu
        Collections.sort(sveKnjige, new Comparator<Knjiga>() {
            @Override
            public int compare(Knjiga k1, Knjiga k2) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                // obrnuto
                return k1.getDatumObjavljivanja().compareTo(k2.getDatumObjavljivanja()) * -1;
            }
        });
        */

        for(int i = 0; i < 5; i++) {
            if(i >= sveKnjige.size()) break;
            rez.add(sveKnjige.get(i));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onNajnovijeDone(rez);
    }

    private String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();

    }

}
