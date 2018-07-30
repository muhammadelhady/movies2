package com.example.muham.movies;



import android.graphics.Movie;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by muham on 4/28/2018.
 */

public class JsonParser {



   public static ArrayList<movie> parseMovies(String jsonString) throws JSONException {

      ArrayList<movie> listOfMovies=new ArrayList<>();
      JSONObject jsonData= new JSONObject(jsonString);
      JSONArray movies=jsonData.getJSONArray("results");
      ArrayList<JSONObject> jsonObjects=new ArrayList<>();
      for (int i = 0;i<movies.length();i++)
      {
         jsonObjects.add(movies.getJSONObject(i));
      }
      for (int i =0;i<jsonObjects.size();i++)
      {
         movie movie= new movie();
         JSONObject currenObject=jsonObjects.get(i);
         movie.setTitle(currenObject.getString("title"));
         movie.setOrinalTitle(currenObject.getString("original_title"));
         movie.setMovieRate(currenObject.getString("vote_average"));
         movie.setOverView(currenObject.getString("overview"));
         movie.setReleseDate(currenObject.getString("release_date"));
         movie.setImageUrl(currenObject.getString("poster_path"));
         movie.setMovieId(currenObject.getString("id"));
         listOfMovies.add(movie);
      }
      return listOfMovies;
   }

 public static String streamtostring(InputStream inputStream) throws IOException {
      BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
      String line;
      String text="";
      while ((line=bufferedReader.readLine())!=null)
      {
         text+=line;
      }
      bufferedReader.close();
      return text;


   }

   static  ArrayList<String> ParseMovieTrailer(String jsonString) throws JSONException {
       ArrayList<String> trailerKey=new ArrayList<>();
       JSONObject jsonData= new JSONObject(jsonString);
       JSONArray trailer=jsonData.getJSONArray("results");
       for (int i = 0  ; i < trailer.length();i++)
       {
           JSONObject jsonObject = trailer.getJSONObject(i);
           trailerKey.add(jsonObject.getString("key")) ;
       }

       return trailerKey;
   }


    static ArrayList<String> ParseMovieReviews(String jsonString) throws JSONException {
        ArrayList<String> reviewContent=new ArrayList<>();
        JSONObject jsonData= new JSONObject(jsonString);
        JSONArray trailer=jsonData.getJSONArray("results");
        for (int i = 0  ; i < trailer.length();i++)
        {
            JSONObject jsonObject = trailer.getJSONObject(i);
            reviewContent.add(jsonObject.getString("content")) ;
        }
         return reviewContent;
    }



}
