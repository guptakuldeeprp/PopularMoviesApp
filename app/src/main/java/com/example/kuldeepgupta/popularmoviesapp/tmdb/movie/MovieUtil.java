package com.example.kuldeepgupta.popularmoviesapp.tmdb.movie;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import com.example.kuldeepgupta.popularmoviesapp.provider.movie.MovieColumns;
import com.example.kuldeepgupta.popularmoviesapp.provider.movie.MovieContentValues;
import com.example.kuldeepgupta.popularmoviesapp.provider.movie.MovieCursor;
import com.example.kuldeepgupta.popularmoviesapp.provider.movie.MovieSelection;

import java.awt.font.TextAttribute;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kuldeep.gupta on 30-01-2016.
 */
public class MovieUtil {

    private static final String TAG = MovieUtil.class.getName();
    private static final String YOUTUBE_BASE_URL_WEB = "https://www.youtube.com/watch?v=";
    private static final String YOUTUBE_BASE_URL_APP = "vnd.youtube:";
    private static final String TRAILER_DESC_PREFIX = "Trailer";
    private static final Matcher TRAILER_DESC_MATCHER = Pattern.compile("Trailer\\d+").matcher("");

    /*public static Map<String,String> getTrailerKeysDescMap(Movie movie) {
        return getTrailerUrlsMap(movie.getTrailerKeys());
    }*/

    public static String getTrailerAppUrlFromDesc(Movie movie, String trailerDesc) {
        return getTrailerAppUrlFromKey(getTrailerKeyFromDesc(movie, trailerDesc));
    }

    public static String getTrailerWebUrlFromDesc(Movie movie, String trailerDesc) {
        return getTrailerWebUrlFromKey(getTrailerKeyFromDesc(movie, trailerDesc));
    }


    public static String getTrailerKeyFromDesc(Movie movie, String trailerDesc) {
        if (TRAILER_DESC_MATCHER.reset(trailerDesc).matches()) {
            int trailerKeyIndex = extractIntFromStringEnd(trailerDesc) - 1;
            if (trailerKeyIndex < movie.getTrailerKeys().size()) {
                return movie.getTrailerKeys().get(trailerKeyIndex);
            }
        }
        throw new IllegalArgumentException("Cannot obtain trailer key for description " + trailerDesc + ". Invalid format.");

    }

    public static Integer extractIntFromStringEnd(String s) {
        int i = s.length();
        while (i > 0 && Character.isDigit(s.charAt(i - 1))) {
            i--;
        }
        return new Integer(s.substring(i));

    }


    public static Map<String, String> getTrailerWebUrlsDescMap(Movie movie) {
        return getTrailerUrlsMap(getTrailerWebUrls(movie));
    }

    public static Map<String, String> getTrailerAppUrlsDescMap(Movie movie) {
        return getTrailerUrlsMap(getTrailerAppUrls(movie));
    }

    public static Map<String, String> getTrailerKeysDescMap(Movie movie) {
        return getTrailerUrlsMap(movie.getTrailerKeys());
    }

    public static Map<String, String> getTrailerUrlsMap(List<String> urls) {
        if (urls != null) {

            Map<String, String> urlsMap = new LinkedHashMap<>();
            for (int i = 0; i < urls.size(); i++) {
                urlsMap.put(TRAILER_DESC_PREFIX + (i + 1), urls.get(i));
            }
            return urlsMap;
        }
        return Collections.emptyMap();
    }


    public static List<String> getTrailerWebUrls(Movie movie) {
        List<String> trailerWebUrls = new ArrayList<>(movie.getTrailerKeys().size());
        for (String trailerKey : movie.getTrailerKeys()) {
            trailerWebUrls.add(getTrailerWebUrlFromKey(trailerKey));
            return trailerWebUrls;
        }

        return Collections.emptyList();
    }

    public static List<String> getTrailerAppUrls(Movie movie) {
        List<String> trailerAppUrls = new ArrayList<>(movie.getTrailerKeys().size());
        for (String trailerKey : movie.getTrailerKeys()) {
            trailerAppUrls.add(getTrailerAppUrlFromKey(trailerKey));
            return trailerAppUrls;
        }

        return Collections.emptyList();
    }

    public static String getTrailerWebUrlFromKey(String trailerKey) {
        return YOUTUBE_BASE_URL_WEB + trailerKey;
    }

    public static String getTrailerAppUrlFromKey(String trailerKey) {
        return YOUTUBE_BASE_URL_APP + trailerKey;
    }

    // persistence helper methods
    public static void saveMovie(Context context, Movie movie) {
        if (movie == null || context == null) {
            Log.w(TAG, "Context or Movie to save is null.");
            return;
        }

        MovieContentValues movieVal = new MovieContentValues();
        movieVal.putMid(movie.getMid());
        movieVal.putTitle(movie.getTitle());
        movieVal.putPlotsynopsis(movie.getPlotSynopsis());
        movieVal.putReleasedate(movie.getReleaseDate());
        movieVal.putPosterpath(movie.getPosterPath());
        movieVal.putPostersaveloc(movie.getPosterSaveLoc());
        movieVal.putUserrating(movie.getUserRating());
        context.getContentResolver().insert(MovieColumns.CONTENT_URI, movieVal.values());
    }

    public static List<Movie> getSavedMovies(Context context) {
        if (context == null) {
            Log.w(TAG, "Context passed is null.");
            return Collections.emptyList();

        }
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MovieColumns.CONTENT_URI, new String[]{MovieColumns.MID, MovieColumns.TITLE, MovieColumns.RELEASEDATE, MovieColumns.PLOTSYNOPSIS, MovieColumns.POSTERPATH, MovieColumns.USERRATING, MovieColumns.POSTERSAVELOC}, null, null, MovieColumns._ID + " ASC");
            if (cursor != null) {
                List<Movie> movies = new ArrayList<>();
                while (cursor.moveToNext()) {
                    //public Movie(long mid, String title, String posterPath, String releaseDate, String plotSynopsis, double userRating) {
                    Movie movie = new Movie(cursor.getLong(cursor.getColumnIndex(MovieColumns.MID)), cursor.getString(cursor.getColumnIndex(MovieColumns.TITLE))
                            , cursor.getString(cursor.getColumnIndex(MovieColumns.POSTERPATH)), cursor.getString(cursor.getColumnIndex(MovieColumns.RELEASEDATE)),
                            cursor.getString(cursor.getColumnIndex(MovieColumns.PLOTSYNOPSIS)), cursor.getString(cursor.getColumnIndex(MovieColumns.POSTERSAVELOC)), cursor.getDouble(cursor.getColumnIndex(MovieColumns.USERRATING)));
                    movie.setIsFav(true);
                    movies.add(movie);
                }
                return movies;
            } else {
                Log.e(TAG, "Provider error while fetching cursor for movies.");
            }
            return Collections.emptyList();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }

}
