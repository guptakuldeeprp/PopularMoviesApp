package com.example.kuldeepgupta.popularmoviesapp.tmdb.movie;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public class Movie implements Parcelable {

    private static final String YOUTUBE_BASE_URL_WEB = "https://www.youtube.com/watch?v=";
    private static final String YOUTUBE_BASE_URL_APP = "vnd.youtube:";
    private static final String TRAILER_DESC_PREFIX = "Trailer";
    private static final Matcher TRAILER_DESC_MATCHER = Pattern.compile("Trailer\\d+").matcher("");

    // for part-1
    private long mid;
    private String title; // original_title
    private String posterPath; // poster_path
    private String releaseDate; // release_date
    private String plotSynopsis; // overview
    private double userRating; // vote_average

    // for part-2
    private boolean isFav; // is this movie marked as favourite?
    private String posterSaveLoc;


    private List<String> trailerKeys;


    public Movie() {
    }

    public Movie(long mid, String title, String posterPath, String releaseDate, String plotSynopsis, double userRating) {
        this.mid = mid;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        trailerKeys = new ArrayList<>();
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
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

    public List<String> getTrailerKeys() {
        return trailerKeys;
    }

    public void setTrailerKeys(List<String> trailerKeys) {
        this.trailerKeys = trailerKeys;
    }


    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    public String getPosterSaveLoc() {
        return posterSaveLoc;
    }

    public void setPosterSaveLoc(String posterSaveLoc) {
        this.posterSaveLoc = posterSaveLoc;
    }

    public boolean isFav() {
        return isFav;
    }
    /*
    public String getTrailerWebUrlFromDesc(String trailerDesc) {
        return getTrailerWebUrlFromKey(getTrailerKeyFromDesc(trailerDesc));
    }

    public String getTrailerAppUrlFromDesc(String trailerDesc) {
        return getTrailerAppUrlFromKey(getTrailerKeyFromDesc(trailerDesc));
    }

    public String getTrailerKeyFromDesc(String trailerDesc) {
        if(TRAILER_DESC_MATCHER.reset(trailerDesc).matches()) {
            int trailerKeyIndex = extractIntFromStringEnd(trailerDesc) - 1;
            if (trailerKeyIndex < trailerKeys.size()) {
                return trailerKeys.get(trailerKeyIndex);
            }
        }
            throw new IllegalArgumentException("Cannot obtain trailer key for description " + trailerDesc + ". Invalid format.");

    }

    private Integer extractIntFromStringEnd(String s) {
        int i = s.length();
        while (i > 0 && Character.isDigit(s.charAt(i - 1))) {
            i--;
        }
        return new Integer(s.substring(i));

    }

    public Map<String,String> getTrailerWebUrlsDescMap() {
        return getTrailerUrlsMap(getTrailerWebUrls());
    }

    public Map<String,String> getTrailerAppUrlsDescMap() {
        return getTrailerUrlsMap(getTrailerAppUrls());
    }

    public Map<String,String> getTrailerKeysDescMap() {
        return getTrailerUrlsMap(trailerKeys);
    }

    public Map<String,String> getTrailerUrlsMap(List<String> urls) {
        if(urls != null) {

            Map<String,String> urlsMap = new LinkedHashMap<>();
            for (int i = 0; i < urls.size(); i++) {
                urlsMap.put(TRAILER_DESC_PREFIX +(i+1),urls.get(i));
            }
            return urlsMap;
        }
        return Collections.emptyMap();
    }


    public List<String> getTrailerWebUrls() {
        List<String> trailerWebUrls = new ArrayList<>(getTrailerKeys().size());
        for (String trailerKey : getTrailerKeys()) {
            trailerWebUrls.add(getTrailerWebUrlFromKey(trailerKey));
            return trailerWebUrls;
        }

        return Collections.emptyList();
    }

    public List<String> getTrailerAppUrls() {
        List<String> trailerAppUrls = new ArrayList<>(getTrailerKeys().size());
        for (String trailerKey : getTrailerKeys()) {
            trailerAppUrls.add(getTrailerAppUrlFromKey(trailerKey));
            return trailerAppUrls;
        }

        return Collections.emptyList();
    }

    public String getTrailerWebUrlFromKey(String trailerKey) {
        return YOUTUBE_BASE_URL_WEB + trailerKey;
    }

    public String getTrailerAppUrlFromKey(String trailerKey) {
        return YOUTUBE_BASE_URL_APP + trailerKey;
    }
*/



    protected Movie(Parcel in) {
        mid = in.readLong();
        title = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readDouble();
        isFav = in.readByte() != 0x00;
        posterSaveLoc = in.readString();
        if (in.readByte() == 0x01) {
            trailerKeys = new ArrayList<String>();
            in.readList(trailerKeys, String.class.getClassLoader());
        } else {
            trailerKeys = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mid);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(plotSynopsis);
        dest.writeDouble(userRating);
        dest.writeByte((byte) (isFav ? 0x01 : 0x00));
        dest.writeString(posterSaveLoc);
        if (trailerKeys == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(trailerKeys);
        }
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
                "mid=" + mid +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", userRating=" + userRating +
                ", isFav=" + isFav +
                ", posterSaveLoc='" + posterSaveLoc + '\'' +
                ", trailerKeys=" + trailerKeys +
                '}';
    }
}
