package com.example.kuldeepgupta.popularmoviesapp.data.adapter.async;

import android.widget.ArrayAdapter;

import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kuldeep.gupta on 19-01-2016.
 */
public class AsyncReviewAdapter extends AbstractAsyncArrayAdapter<String> {

    private Movie movie;
    private TmdbUtil tmdbUtil;
    public static final List<String> NA = Arrays.asList(new String[]{"N/A"});

    public AsyncReviewAdapter(ArrayAdapter<String> wrapped, Movie movie) {
        this(wrapped,movie,0);
    }

    public AsyncReviewAdapter(ArrayAdapter<String> wrapped, Movie movie, int page) {
        super(wrapped, page);
        this.movie = movie;
        tmdbUtil = TmdbUtil.get();
    }

    @Override
    protected List<String> loadListData(int page) {
        List<String> reviews = tmdbUtil.fetchReviews(movie.getMid(), page);
        if(reviews == null || reviews.isEmpty())
        {
            //stopAppending();
            return NA;
        }
        return reviews;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
