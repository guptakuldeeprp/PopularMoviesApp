package com.example.kuldeepgupta.popularmoviesapp.data.adapter.async;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.MovieUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by kuldeep.gupta on 28-01-2016.
 */
public class AsyncTrailerAdapter extends AbstractAsyncArrayAdapter<String> {

    private static final String TAG = AsyncTrailerAdapter.class.getName();
    private Movie movie;
    private TmdbUtil tmdbUtil;
    public static final List<String> NA = Arrays.asList(new String[]{"N/A"});


    public AsyncTrailerAdapter(ArrayAdapter<String> wrapped, Movie movie) {
        super(wrapped);
        this.movie = movie;
        tmdbUtil = TmdbUtil.get();
    }

    @Override
    protected List<String> loadListData(int page) {
        //Log.w(TAG, "page: " + page);
        // trailers do not have multiple pages
        if(page == 1 && movie != null) {
            tmdbUtil.setTrailerKeys(movie);
            if(movie.getTrailerKeys() == null || movie.getTrailerKeys().isEmpty()) {
                //stopAppending();
                return NA;
            }
            Set<String> trailersKeys = MovieUtil.getTrailerKeysDescMap(movie).keySet();
            return new ArrayList<>(trailersKeys);
        }
        return Collections.emptyList();
    }


}
