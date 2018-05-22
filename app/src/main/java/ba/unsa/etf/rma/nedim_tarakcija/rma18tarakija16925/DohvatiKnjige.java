package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DohvatiKnjige extends AsyncTask<String, Integer, Void> {

    public interface IDohvatiKnjigeDone {
        public void onDohvatiDone(ArrayList<Knjiga> knjige);
    }

    ArrayList<Knjiga> rez;
    IDohvatiKnjigeDone pozivatelj;

    public DohvatiKnjige(IDohvatiKnjigeDone p) {
        pozivatelj = p;
        rez = new ArrayList<Knjiga>();
    }

    @Override
    protected Void doInBackground(String... params) {
        for(int i = 0; i < params.length; i++) {
            String query = null;
            try {
                query = URLEncoder.encode(params[i], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String link​ = "https://www.googleapis.com/books/v1/volumes?q=intitle:";
            String rezCount = "&maxResults=5";
            String url1 = link​ + query + rezCount;

            try {

                URL url = new URL(url1);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

                InputStream is = urlConnection.getInputStream();
                String rezultat = convertStreamToString(is);

                JSONObject jo = new JSONObject(rezultat);
                JSONArray items = jo.optJSONArray("items");
                if(items == null) continue;

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

                    rez.add(new Knjiga(id, naziv, autori, opis, datumObjavljivanja, slika, brojStranica));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onDohvatiDone(rez);
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
