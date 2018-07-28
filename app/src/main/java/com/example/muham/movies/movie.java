package com.example.muham.movies;

/**
 * Created by muham on 4/28/2018.
 */

public class movie {
    private String title;
    private String orinalTitle;
    private String imageUrl;
    private String overView;
    private String movieRate;
    private  String releseDate;
    private String movieId;
    public movie(String title,String orinalTitle,String imageUrl,String overView,String movieRate, String releseDate, String movieId)
    {
       this.setTitle(title);
       this.setOrinalTitle(orinalTitle);
       this.setImageUrl(imageUrl);
       this.setOverView(overView);
       this.setMovieRate(movieRate);
       this.setReleseDate(releseDate);
       this.setMovieId(movieId);
    }
    public movie()
    {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrinalTitle() {
        return orinalTitle;
    }

    public void setOrinalTitle(String orinalTitle) {
        this.orinalTitle = orinalTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getMovieRate() {
        return movieRate;
    }

    public void setMovieRate(String movieRate) {
        this.movieRate = movieRate;
    }

    public String getReleseDate() {
        return releseDate;
    }

    public void setReleseDate(String releseDate) {
        this.releseDate = releseDate;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

}
