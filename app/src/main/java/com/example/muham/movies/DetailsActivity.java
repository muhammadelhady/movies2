package com.example.muham.movies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
       String movieId;
    ImageView movieImage;

    ArrayList<String> DbMovies,movieKey,reviewContent;
    ImageButton favoritButton;
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
        favoritButton=(ImageButton)findViewById(R.id.FavoritImageButton);

        FillData();
        MovieReview();



       DbMovies = GetAllDbMovies();
       setFavoritButtonImage();
        WatchTrailer();
    }

void setFavoritButtonImage()
{
    Resources res = getResources();
    Drawable img = res.getDrawable(android.R.drawable.btn_star_big_off);
    favoritButton.setImageDrawable(img);
for (int i = 0; i  <DbMovies.size();i++)
{
    if (movieId.equals(DbMovies.get(i)))
    {
        img = res.getDrawable(android.R.drawable.btn_star_big_on);
        favoritButton.setImageDrawable(img);
    }
}
}

public  ArrayList<String> GetAllDbMovies()
{

    Cursor cursor  = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,null,null,null, MoviesContract.MoviesEntry.COULMN_ID);

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
        setFavoritButtonImage();
    }
    else
    {

        Toast.makeText(this, "favorit",Toast.LENGTH_LONG).show();
        AddMovieToDb(movieId);
        DbMovies=GetAllDbMovies();
        setFavoritButtonImage();

    }
}



 void RemoveMovieFromDb(String movieId)
 {

     getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI,movieId,null);

 }

 void AddMovieToDb(String movieId)
 {

     ContentValues values = new ContentValues();
     values.put(MoviesContract.MoviesEntry.COULMN_ID,movieId);
     values.put(MoviesContract.MoviesEntry.COULMN_TITLE,title.getText().toString());

   Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI,values);



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


    public void WatchTrailer()
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


       TrailersAdapter trailersAdapter = new TrailersAdapter(movieKey);
       ListView  listView = (ListView)findViewById(R.id.trailers_ListView);
       listView.setAdapter(trailersAdapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String url=getResources().getString(R.string.YOUTUBE_URL)+movieKey.get(position);
               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
               startActivity(intent);
           }
       });

            super.onPostExecute(s);
        }
    }

    class TrailersAdapter extends BaseAdapter{

        ArrayList<String> trailers=new ArrayList<>();

        public TrailersAdapter(ArrayList<String> trailers)
        {
            this.trailers=trailers;

        }
        @Override
        public int getCount() {
            return trailers.size();
        }

        @Override
        public Object getItem(int position) {
            return trailers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.trailer_row_view,null);
            TextView textView =(TextView)view.findViewById(R.id.trailer_txt);
            Log.d("test",trailers.size()+"");
            textView.setText("trailer "+(position+1));
            return view;
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
            ReviwesAdapter reviwesAdapter=new ReviwesAdapter(reviewContent);
            ListView listView  =(ListView)findViewById(R.id.review_ListView);
            listView.setAdapter(reviwesAdapter);
           super.onPostExecute(s);
        }

    }


    class ReviwesAdapter extends BaseAdapter
    {

        ArrayList<String> reviwes=new ArrayList<>();

        public ReviwesAdapter(ArrayList<String> reviwes)
        {
            this.reviwes=reviwes;
        }

        @Override
        public int getCount() {
            return reviwes.size();
        }

        @Override
        public Object getItem(int position) {
            return reviwes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.review_row_view,null);
            TextView counter = (TextView)view.findViewById(R.id.review_txt_counter);
            counter.setText("Review "+(position+1)+" :");
            TextView txt = (TextView)view.findViewById(R.id.review_txt);
            txt.setText(reviwes.get(position));
            return view;
        }
    }

}


