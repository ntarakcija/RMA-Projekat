package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BazaOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mojaBaza.db";
    public static final int DATABASE_VERSION = 1;

    // Tabela Kategorija
    public static final String TABLE_KATEGORIJA = "Kategorija";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAZIV = "naziv";

    private static final String DATABASE_CREATE_KATEGORIJA = "create table " + TABLE_KATEGORIJA + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAZIV + " text unique not null);";

    // Tabela Knjiga
    public static final String TABLE_KNJIGA = "Knjiga";
    public static final String COLUMN_OPIS = "opis";
    public static final String COLUMN_DATUM_OBJAVLJIVANJA = "datumObjavljivanja";
    public static final String COLUMN_BROJ_STRANICA = "brojStranica";
    public static final String COLUMN_ID_WEB_SERVIS = "idWebServis";
    public static final String COLUMN_ID_KATEGORIJE = "idkategorije";
    public static final String COLUMN_SLIKA = "slika";
    public static final String COLUMN_PREGLEDANA = "pregledana";

    public static final String DATABASE_CREATE_KNJIGA = "create table " + TABLE_KNJIGA + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAZIV + " text not null, " +
            COLUMN_OPIS + " text, " +
            COLUMN_DATUM_OBJAVLJIVANJA + " text, " +
            COLUMN_BROJ_STRANICA + " integer, " +
            COLUMN_ID_WEB_SERVIS + " text unique not null, " +
            COLUMN_ID_KATEGORIJE + " integer not null, " +
            COLUMN_SLIKA + " text, " +
            COLUMN_PREGLEDANA + " integer);";

    // Tabela Autor
    public static final String TABLE_AUTOR = "Autor";
    public static final String COLUMN_IME = "ime";

    private static final String DATABASE_CREATE_AUTOR = "create table " + TABLE_AUTOR + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_IME + " text unique not null);";


    // Tabela Autorstvo
    public static final String TABLE_AUTORSTVO = "Autorstvo";
    public static final String COLUMN_ID_AUTORA = "idautora";
    public static final String COLUMN_ID_KNJIGE = "idknjige";

    public static final String DATABASE_CREATE_AUTORSTVO = "create table " + TABLE_AUTORSTVO + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_ID_AUTORA + " integer not null, " +
            COLUMN_ID_KNJIGE + " integer not null);";


    public BazaOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_KATEGORIJA);
        db.execSQL(DATABASE_CREATE_KNJIGA);
        db.execSQL(DATABASE_CREATE_AUTOR);
        db.execSQL(DATABASE_CREATE_AUTORSTVO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Brisanje stare verzije
        db.execSQL("drop table if exists " + TABLE_KATEGORIJA);
        db.execSQL("drop table if exists " + TABLE_AUTOR);
        db.execSQL("drop table if exists " + TABLE_KNJIGA);
        db.execSQL("drop table if exists " + TABLE_AUTORSTVO);
        // Kreiranje nove
        onCreate(db);
    }

    long dodajKategoriju(String naziv) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from " + TABLE_KATEGORIJA + " where " + COLUMN_NAZIV + " = \"" + naziv + "\"";
        //Cursor cursor = db.rawQuery(query, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAZIV, naziv);
        return db.insert(TABLE_KATEGORIJA, null, values);
    }

    long dodajKnjigu(Knjiga knjiga) {
        SQLiteDatabase db = this.getReadableDatabase();
        long kod;

        ContentValues valuesKnjiga = new ContentValues();
        valuesKnjiga.put(COLUMN_NAZIV, knjiga.getNaziv());
        valuesKnjiga.put(COLUMN_DATUM_OBJAVLJIVANJA, knjiga.getDatumObjavljivanja());
        valuesKnjiga.put(COLUMN_OPIS, knjiga.getOpis());
        valuesKnjiga.put(COLUMN_BROJ_STRANICA, knjiga.getBrojStranica());
        valuesKnjiga.put(COLUMN_ID_WEB_SERVIS, knjiga.getId());
        valuesKnjiga.put(COLUMN_ID_KATEGORIJE, knjiga.getKategorijaId());
        valuesKnjiga.put(COLUMN_SLIKA, knjiga.getSlika().toString());

        kod = db.insert(TABLE_KNJIGA, null, valuesKnjiga);

        if(kod != -1) {
            for(int i = 0; i < knjiga.getAutori().size(); i++) {
                ContentValues valuesAutor = new ContentValues();
                valuesAutor.put(COLUMN_IME, knjiga.getAutori().get(i).getImeiPrezime());
                db.insert(TABLE_AUTOR, null, valuesAutor);
            }
            for(int i = 0; i < knjiga.getAutori().size(); i++) {
                String query = "select * from " + TABLE_AUTOR + " where " + COLUMN_IME + " = \"" +
                        knjiga.getAutori().get(i).getImeiPrezime() + "\"";
                Cursor cursor = db.rawQuery(query, null);
                if(cursor.moveToFirst()) {
                    ContentValues valuesAutorstvo = new ContentValues();
                    valuesAutorstvo.put(COLUMN_ID_AUTORA, cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    valuesAutorstvo.put(COLUMN_ID_KNJIGE, kod);
                    db.insert(TABLE_AUTORSTVO, null, valuesAutorstvo);
                }
            }
        }

        return  kod;
    }

    ArrayList<Knjiga> knjigeKategorije(long idKategorije) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Knjiga> knjige = new ArrayList<>();

        // Pretraživanje knjiga koji imaju id proslijeđene kategorije
        String queryKnjige = "select * from " + TABLE_KNJIGA + " where " + COLUMN_ID_KATEGORIJE + " = \"" +
                idKategorije + "\"";
        Cursor cursorKnjige = db.rawQuery(queryKnjige, null);
        //if(cursorKnjige.moveToFirst()) {
            while(cursorKnjige.moveToNext()) {
                Knjiga knjiga = new Knjiga();
                knjiga.setId(cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_ID_WEB_SERVIS)));
                knjiga.setNaziv(cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_NAZIV)));
                knjiga.setOpis(cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_OPIS)));
                knjiga.setKategorijaId(cursorKnjige.getInt(cursorKnjige.getColumnIndex(COLUMN_ID_KATEGORIJE)));
                knjiga.setDatumObjavljivanja(cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_DATUM_OBJAVLJIVANJA)));
                knjiga.setBrojStranica(cursorKnjige.getInt(cursorKnjige.getColumnIndex(COLUMN_BROJ_STRANICA)));
                String s = cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_SLIKA));
                try {
                    knjiga.setSlika(new URL(s));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                // Pretraživanje tabele Autorstvo i spremanje id-eva autora koji su napisali knjigu sa id-em trenutne knjige
                ArrayList<Integer> autoriId = new ArrayList<>();
                String queryAutorstvo = "select * from " + TABLE_AUTORSTVO + " where " + COLUMN_ID_KNJIGE + " = \"" +
                        cursorKnjige.getInt(cursorKnjige.getColumnIndex(COLUMN_ID)) + "\"";
                Cursor cursorAutorstvo = db.rawQuery(queryAutorstvo, null);
                while(cursorAutorstvo.moveToNext()) {
                    autoriId.add(cursorAutorstvo.getInt(cursorAutorstvo.getColumnIndex(COLUMN_ID_AUTORA)));
                }

                // Pretraživanje autora
                ArrayList<Autor> autori = new ArrayList<>();

                for(int i = 0; i < autoriId.size(); i++) {
                    Autor autor = new Autor();

                    // Pretraživanje autora sa datim id-em i dodavanje imena
                    String queryAutori = "select * from " + TABLE_AUTOR + " where " + COLUMN_ID + " = \"" +
                            autoriId.get(i).toString() + "\"";
                    Cursor cursorAutori = db.rawQuery(queryAutori, null);
                    if(cursorAutori.moveToFirst()) {
                        autor.setImeiPrezime(cursorAutori.getString(cursorAutori.getColumnIndex(COLUMN_IME)));
                    }

                    // Pretraživanje autorstva i dodavanje id-eva knjiga koje je napisao
                    ArrayList<String> knjigeId = new ArrayList<>();
                    queryAutorstvo = "select * from " + TABLE_AUTORSTVO + " where " + COLUMN_ID_AUTORA + " = \"" +
                            autoriId.get(i).toString() + "\"";
                    cursorAutorstvo = db.rawQuery(queryAutorstvo, null);
                    while(cursorAutorstvo.moveToNext()) {
                        knjigeId.add(cursorAutorstvo.getString(cursorAutorstvo.getColumnIndex(COLUMN_ID_KNJIGE)));
                    }

                    autor.setKnjige(knjigeId);
                    autori.add(autor);
                    cursorAutori.close();
                }
                knjiga.setAutori(autori);
                knjige.add(knjiga);
                cursorAutorstvo.close();
            }
        //}
        cursorKnjige.close();

        return knjige;
    }

    ArrayList<Knjiga> knjigeAutora(long idAutora) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Knjiga> knjige = new ArrayList<>();

        // Pretraživanje tabele Autorstvo i uzimanje id knjige za svaki red koji odgovara id datog autora
        String queryAutorstvo = "select * from " + TABLE_AUTORSTVO + " where " + COLUMN_ID_AUTORA + " = \"" +
                idAutora + "\"";
        Cursor cursorAutorstvo = db.rawQuery(queryAutorstvo, null);

        while(cursorAutorstvo.moveToNext()) {
            // Pretraživanje tabele Knjige i uzimanje one knjige kojoj odgovara uzeti id iz tabele Autorstvo
            String queryKnjige = "select * from " + TABLE_KNJIGA + " where " + COLUMN_ID + " = \"" +
                    cursorAutorstvo.getInt(cursorAutorstvo.getColumnIndex(COLUMN_ID_KNJIGE)) + "\"";
            Cursor cursorKnjige = db.rawQuery(queryKnjige, null);

            while(cursorKnjige.moveToNext()) {
                Knjiga knjiga = new Knjiga();
                knjiga.setId(cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_ID_WEB_SERVIS)));
                knjiga.setNaziv(cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_NAZIV)));
                knjiga.setOpis(cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_OPIS)));
                knjiga.setKategorijaId(cursorKnjige.getInt(cursorKnjige.getColumnIndex(COLUMN_ID_KATEGORIJE)));
                knjiga.setDatumObjavljivanja(cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_DATUM_OBJAVLJIVANJA)));
                knjiga.setBrojStranica(cursorKnjige.getInt(cursorKnjige.getColumnIndex(COLUMN_BROJ_STRANICA)));
                String s = cursorKnjige.getString(cursorKnjige.getColumnIndex(COLUMN_SLIKA));
                try {
                    knjiga.setSlika(new URL(s));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                // Dodavanje svih autora koji su napisali datu knjigu
                ArrayList<Autor> autori = new ArrayList<>();
                String queryAutorstvo2 = "select * from " + TABLE_AUTORSTVO + " where " + COLUMN_ID_KNJIGE + " = \"" +
                        cursorKnjige.getInt(cursorKnjige.getColumnIndex(COLUMN_ID)) + "\"";
                Cursor cursorAutorstvo2 = db.rawQuery(queryAutorstvo2, null);

                while(cursorAutorstvo2.moveToNext()) {
                    // Pretraživanje tabele Autor i uzimanje podataka za autora
                    String queryAutor = "select * from " + TABLE_AUTOR + " where " + COLUMN_ID + " = \"" +
                            cursorAutorstvo2.getInt(cursorAutorstvo2.getColumnIndex(COLUMN_ID_AUTORA)) + "\"";
                    Cursor cursorAutor = db.rawQuery(queryAutor, null);

                    while(cursorAutor.moveToNext()) {
                        Autor a = new Autor();
                        a.setImeiPrezime(cursorAutor.getString(cursorAutor.getColumnIndex(COLUMN_IME)));

                        // Pretraživanje autorstva i dodavanje id-eva knjiga koje je napisao
                        ArrayList<String> knjigeId = new ArrayList<>();
                        String queryAutorstvo3 = "select * from " + TABLE_AUTORSTVO + " where " + COLUMN_ID_AUTORA + " = \"" +
                                cursorAutor.getInt(cursorAutor.getColumnIndex(COLUMN_ID)) + "\"";
                        Cursor cursorAutorstvo3 = db.rawQuery(queryAutorstvo3, null);

                        while(cursorAutorstvo3.moveToNext()) {
                            knjigeId.add(cursorAutorstvo.getString(cursorAutorstvo.getColumnIndex(COLUMN_ID_KNJIGE)));
                        }

                        a.setKnjige(knjigeId);
                        autori.add(a);
                    }
                }

                knjiga.setAutori(autori);
                knjige.add(knjiga);
            }
        }

        return knjige;
    }

}
