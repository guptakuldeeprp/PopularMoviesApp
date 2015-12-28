package com.example.kuldeepgupta.popularmoviesapp.tmdb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public class Movie implements Parcelable{

    private long id;
    private String title; // original_title
    private String posterPath; // poster_path
    private String releaseDate; // release_date
    private String plotSynopsis; // overview
    private double userRating; // vote_average

    public Movie() {
    }

    public Movie(long id, String title, String posterPath, String releaseDate, String plotSynopsis, double userRating) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(plotSynopsis);
        dest.writeDouble(userRating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", userRating=" + userRating +
                '}';
    }
}
