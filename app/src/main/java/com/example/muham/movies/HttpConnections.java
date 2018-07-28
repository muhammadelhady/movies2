package com.example.muham.movies;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnections {


    public static String ConnectHttpUrl(String link) throws IOException {
        URL  url = new URL(link);
            String Data;
        HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
        InputStream inputStream=new BufferedInputStream(urlConnection.getInputStream());
           Data= JsonParser.streamtostring(inputStream);
          inputStream.close();
        return Data;
    }
}
