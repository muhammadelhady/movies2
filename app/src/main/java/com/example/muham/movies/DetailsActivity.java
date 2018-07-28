package com.example.muham.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    TextView title,orignalTitle,userRating,relaseDate,overView, reviewTextView;
       String movieId,movieKey,reviewContent;
    ImageView movieImage;
    private  SQLiteDatabase mDb;
    ArrayList<String> DbMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        movieImage=(ImageView)findViewById(R.id.movie_image);
        title=(TextView)findViewById(R.id.title_txt);
        orignalTitle=(TextView)findViewById(R.id.original_title_txt);
        userRating=(TextView)findViewById(R.id.user_rating_txt);
        relaseDate=(TextView)findViewById(R.id.relase_date_txt);
        overView=(TextView)findViewById(R.id.overviewe_txt);

        FillData();
        MovieReview();

        MoviesDbHelper moviesDbHelper =new MoviesDbHelper(this);
        mDb=moviesDbHelper.getWritableDatabase();

       DbMovies = GetAllDbMovies();
    }



public  ArrayList<String> GetAllDbMovies()
{
  Cursor cursor =  mDb.query(
            MoviesContract.MoviesEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            MoviesContract.MoviesEntry.COULMN_ID);
ArrayList<String> DbMovies=new ArrayList<>();

    while(cursor.moveToNext())
  {
      DbMovies.add(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COULMN_ID)));
  }
  cursor.close();
  return DbMovies;
}

boolean IfMovieExistInDb(String movieId)
{
    for(int i = 0 ; i < DbMovies.size();i++)
    {
        if (movieId.equals(DbMovies.get(i)))
        {
            Log.d("db","found  = " +DbMovies.get(i));
            return true;
        }
    }
    return false;
}

public void  Favorit(View view)
{
    if (IfMovieExistInDb(movieId))
    {

        Toast.makeText(this, "Unfavorit",Toast.LENGTH_LONG).show();
        RemoveMovieFromDb(movieId);
        DbMovies=GetAllDbMovies();
    }
    else
    {

        Toast.makeText(this, "favorit",Toast.LENGTH_LONG).show();
        AddMovieToDb(movieId);
        DbMovies=GetAllDbMovies();

    }
}



 void RemoveMovieFromDb(String movieId)
 {
      mDb.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry.COULMN_ID + " = " + movieId, null);
      Log.d("db","remove  = "+title.getText().toString());
 }

 void AddMovieToDb(String movieId)
 {
     ContentValues values = new ContentValues();
     values.put(MoviesContract.MoviesEntry.COULMN_ID,movieId);
     values.put(MoviesContract.MoviesEntry.COULMN_TITLE,title.getText().toString());
     mDb.insert(MoviesContract.MoviesEntry.TABLE_NAME,null,values);
     Log.d("db","insert = "+ title.getText().toString() );

 }


    public void FillData()
    {
        Intent intent=getIntent();
        Picasso.get().load(getResources().getString(R.string.image_base_url)+intent.getStringExtra("image_url")).into(movieImage);
        String titlel=intent.getStringExtra("title");
        title.setText(intent.getStringExtra("title"));
        orignalTitle.setText(intent.getStringExtra("original_title"));
        userRating.setText(intent.getStringExtra("user_rate"));
        relaseDate.setText(intent.getStringExtra("relase_date"));
        overView.setText(intent.getStringExtra("overview"));
        movieId=intent.getStringExtra("id");
    }


    public void WatchTrailer(View view)
    {
      new GetTrailerUrl().execute();
    }

    class GetTrailerUrl extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... strings) {
            String trailerApiUrl= getResources().getString(R.string.API_URL_PART1)+movieId+getResources().getString(R.string.API_TRAILER_URL)+getResources().getString(R.string.api_key);


            try {
           String Data=  HttpConnections.ConnectHttpUrl(trailerApiUrl);
              movieKey=    JsonParser.ParseMovieTrailer(Data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            String url=getResources().getString(R.string.YOUTUBE_URL)+movieKey;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

            super.onPostExecute(s);
        }
    }


    public void MovieReview ()
    {
       new GetReviwe().execute();
    }

    class GetReviwe extends AsyncTask<String, String , String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String trailerApiUrl= getResources().getString(R.string.API_URL_PART1)+movieId+getResources().getString(R.string.API_REVIEW_URL)+getResources().getString(R.string.api_key);
                try {
                String Data=  HttpConnections.ConnectHttpUrl(trailerApiUrl);
                reviewContent=    JsonParser.ParseMovieReviews(Data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
             reviewTextView= (TextView)findViewById(R.id.review_TextView);
            reviewTextView.setText(reviewContent);
           super.onPostExecute(s);
        }
    }

}


