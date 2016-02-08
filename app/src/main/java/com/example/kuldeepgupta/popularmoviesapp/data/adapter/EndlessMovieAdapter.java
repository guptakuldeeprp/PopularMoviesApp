package com.example.kuldeepgupta.popularmoviesapp.data.adapter;

import com.example.kuldeepgupta.popularmoviesapp.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.commonsware.cwac.endless.EndlessAdapter;


import java.util.List;

import com.example.kuldeepgupta.popularmoviesapp.tmdb.SortBy;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public class EndlessMovieAdapter extends EndlessAdapter{

    private static final String NAME = EndlessMovieAdapter.class.getName();

    int page = 0;
    private RotateAnimation rotate = null;
    private View pendingView = null;
    private List<Movie> movies = null;
    private TmdbUtil tmdbUtil;
    private SortBy sortBy;


    public EndlessMovieAdapter(Context context, int resource, List<Movie> movies) {
        this(context, resource, movies, SortBy.DEFAULT);
    }

    public EndlessMovieAdapter(Context context, int resource, List<Movie> movies, SortBy sortBy) {
        this(context, resource, movies, sortBy, 0);
    }

    public EndlessMovieAdapter(Context context, int resource, List<Movie> movies, SortBy sortBy, int page) {

        super(new MovieAdapter(context, resource, movies));
        this.page = page;
        this.sortBy = sortBy;
        tmdbUtil = TmdbUtil.get();
        rotate=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(600);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);


    }

    public View getPendingView(ViewGroup parent) {
        if(pendingView == null)
        {
            ImageView imgView = new ImageView(parent.getContext());
            imgView.setImageResource(R.drawable.ic_popup_sync_1);
            pendingView = imgView;
        }
        pendingView.setVisibility(View.VISIBLE);
        startProgressAnimation();
        return pendingView;
    }

    void startProgressAnimation() {
        if (pendingView!=null) {
            pendingView.startAnimation(rotate);
        }
    }

    @Override
    protected boolean cacheInBackground() throws Exception {

        ++page;
        Log.i(NAME,"cacheInBackground() called. Will discover movies of page " + page);
        movies = tmdbUtil.discoverMovies(page, sortBy);
        if(movies.isEmpty())
            return false;
        else
            return true;
    }

    public ArrayAdapter getDelegateAdapter() {
        return (ArrayAdapter) getWrappedAdapter();
    }

    @Override
    protected void appendCachedData() {
        Log.i(NAME,"appeding to the end of the list");
        MovieAdapter wrappedAdapter = (MovieAdapter) getWrappedAdapter();
        if(movies != null && !movies.isEmpty())
            wrappedAdapter.addAll(movies);
        else
        {
            System.out.println("No wrapped Movie Adapter found!");
            Log.e(NAME,"No wrapped Movie Adapter found!");
        }
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }
}
