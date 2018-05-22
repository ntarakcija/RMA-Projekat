package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

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

import javax.net.ssl.HttpsURLConnection;

public class KnjigePoznanika extends IntentService {

    int STATUS_START = 0;
    int STATUS_FINISH = 1;
    int STATUS_ERROR = 2;

    public KnjigePoznanika(String name) {
        super(name);
    }

    public KnjigePoznanika() {
        super(null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String korisnikId = intent.getStringExtra("id");
        Bundle bundle = new Bundle();
        receiver.send(STATUS_START, bundle);

        String query = null;
        try {
            query = URLEncoder.encode(korisnikId, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            receiver.send(STATUS_ERROR, bundle);
        }

        String link​ = "https://www.googleapis.com/books/v1/users/";
        String url1 = link​ + query + "/bookshelves";

        ArrayList<Knjiga> rez = new ArrayList<>();

        try {

            URL url = new URL(url1);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            InputStream is = urlConnection.getInputStream();
            String rezultat = convertStreamToString(is);

            JSONObject jo = new JSONObject(rezultat);
            JSONArray items = jo.getJSONArray("items");

            ArrayList<String> policeId = new ArrayList<>();

            for(int i = 0; i < items.length(); i++) {
                JSONObject polica = items.getJSONObject(i);
                policeId.add(polica.getString("id"));
            }

            for(int i = 0; i < policeId.size(); i++) {
                    String noviLink = url1.toString() + "/" + policeId.get(i) + "/volumes?";
                    URL noviUrl = new URL(noviLink);
                    HttpsURLConnection urlConnection1 = (HttpsURLConnection) noviUrl.openConnection();

                    InputStream is1 = urlConnection1.getInputStream();
                    String rezultat1 = convertStreamToString(is1);

                    JSONObject jo1 = new JSONObject(rezultat1);
                    JSONArray items1 = jo1.optJSONArray("items");
                    if(items1 == null) continue;


                    for (int j = 0; j < items1.length(); j++) {
                        JSONObject knjiga = items1.getJSONObject(j);
                        JSONObject volumeInfo = knjiga.getJSONObject("volumeInfo");
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                        // id
                        String id = knjiga.optString("id");

                        // naziv
                        String naziv = volumeInfo.optString("title");

                        // autori
                        JSONArray autoriJSON = volumeInfo.optJSONArray("authors");
                        ArrayList<Autor> autori = new ArrayList<Autor>();
                        for (int k = 0; k < autoriJSON.length(); k++)
                            autori.add(new Autor((String) autoriJSON.get(k), id));

                        // opis
                        String opis = volumeInfo.optString("description");
                        if(opis == null) opis = "";

                        // datum objavljivanja
                        String datumObjavljivanja = volumeInfo.optString("publishedDate");

                        // slika
                        URL slika = new URL(imageLinks.optString("thumbnail"));

                        // broj stranica
                        int brojStranica = volumeInfo.optInt("pageCount");

                        rez.add(new Knjiga(id, naziv, autori, opis, datumObjavljivanja, slika, brojStranica));
                    }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            receiver.send(STATUS_ERROR, bundle);
        } catch (IOException e) {
            e.printStackTrace();
            receiver.send(STATUS_ERROR, bundle);
        } catch (JSONException e) {
            e.printStackTrace();
            receiver.send(STATUS_ERROR, bundle);
        }

        bundle.putParcelableArrayList("rezultat", rez);

        receiver.send(STATUS_FINISH, bundle);
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
