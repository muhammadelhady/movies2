package com.example.muham.movies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME="movies.db";

    static final  int DATABASE_VERSION=1;
    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CRAETE_MOVIES_TABEL_QUERY = "CREATE TABLE "+ MoviesContract.MoviesEntry.TABLE_NAME+
                " ( "+ MoviesContract.MoviesEntry.COULMN_ID+" INTEGER PRIMARY KEY , "+
                MoviesContract.MoviesEntry.COULMN_TITLE + " TEXT NOT NULL );";

        db.execSQL(SQL_CRAETE_MOVIES_TABEL_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);

    }
}
