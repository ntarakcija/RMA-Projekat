package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.w3c.dom.Text;

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

    public static final String DATABASE_CREATE_KNJIGA = "create table " + TABLE_KNJIGA + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAZIV + " text not null, " +
            COLUMN_OPIS + " text, " +
            COLUMN_DATUM_OBJAVLJIVANJA + " text, " +
            COLUMN_BROJ_STRANICA + " integer, " +
            COLUMN_ID_WEB_SERVIS + " text unique not null, " +
            COLUMN_ID_KATEGORIJE + " integer not null);";

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
                    valuesAutorstvo.put(COLUMN_ID_KNJIGE, knjiga.getId());
                    db.insert(TABLE_AUTORSTVO, null, valuesAutorstvo);
                }
            }
        }

        return  kod;
    }
}
