package com.example.muham.movies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

public class MoviesContentProvider extends ContentProvider {





    private  static final int MOVIES=100;
    MoviesDbHelper moviesDbHelper;
   private static final UriMatcher suriMatcher = uriMatcher();
    private static UriMatcher uriMatcher()
    {
        UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.AUTHORITY,MoviesContract.PATH_MOVIES,MOVIES);
        return uriMatcher;
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
        SQLiteDatabase database = moviesDbHelper.getReadableDatabase();

        Cursor mCursor = null;
        if (suriMatcher.match(uri)==MOVIES)
        {
            mCursor=database.query(MoviesContract.MoviesEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);

        }
        mCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
