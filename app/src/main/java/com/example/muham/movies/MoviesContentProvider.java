package com.example.muham.movies;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.Log;

public class MoviesContentProvider extends ContentProvider {





    public static final int CODE_MOVIES = 100;
    MoviesDbHelper moviesDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher() {


        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;




        matcher.addURI(authority, MoviesContract.PATH_MOVIES, CODE_MOVIES);




        return matcher;
    }
    @Override
    public boolean onCreate() {

        Context context=getContext();
         moviesDbHelper =new MoviesDbHelper(context);

        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = moviesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor = null;

        switch (match)
        {
            case CODE_MOVIES:
                retCursor=db.query(MoviesContract.MoviesEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

        }
retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

Uri returi;
        switch (match)
        {
            case CODE_MOVIES:
            long id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME,null,values);
                break;

        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returi;
        switch (match)
        {
            case CODE_MOVIES:
               db.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry.COULMN_ID + " = " + selection, null);
                break;

        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
