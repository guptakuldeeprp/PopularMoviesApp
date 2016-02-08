package com.example.kuldeepgupta.popularmoviesapp.data.adapter.async;

import android.content.Context;

import com.example.kuldeepgupta.popularmoviesapp.data.adapter.MovieAdapter;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;

import java.util.List;

import com.example.kuldeepgupta.popularmoviesapp.tmdb.SortBy;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public class AsyncMovieAdapter extends AbstractAsyncArrayAdapter<Movie> {

    private static final String TAG = AsyncMovieAdapter.class.getName();
    private TmdbUtil tmdbUtil;
    private SortBy sortBy;


    public AsyncMovieAdapter(Context context, int resource, List<Movie> movies) {
        this(context, resource, movies, SortBy.DEFAULT);
    }

    public AsyncMovieAdapter(Context context, int resource, List<Movie> movies, SortBy sortBy) {
        this(context, resource, movies, sortBy, 0);
    }

    public AsyncMovieAdapter(Context context, int resource, List<Movie> movies, SortBy sortBy, int page) {

        super(new MovieAdapter(context, resource, movies), page);
        this.sortBy = sortBy;
        tmdbUtil = TmdbUtil.get();
    }

    @Override
    protected List<Movie> loadListData(int page) {
        return tmdbUtil.discoverMovies(page, sortBy);
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

}
