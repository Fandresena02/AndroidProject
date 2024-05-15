package fr.android.devmobproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BddSQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DevMobile";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_CITY = "Ville";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_VILLE = "ville";
    public static final String COLUMN_ADRESSE = "adresse";

    public BddSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_CITY + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VILLE + " TEXT, " +
                COLUMN_ADRESSE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        onCreate(db);
    }

    public void ajouterFavori(String ville, String adresse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VILLE, ville);
        values.put(COLUMN_ADRESSE, adresse);
        db.insert(TABLE_CITY, null, values);
        db.close();
    }

}
