package com.example.kuldeepgupta.popularmoviesapp.data.adapter.async;

import android.content.Context;
import android.database.DatabaseUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kuldeepgupta.popularmoviesapp.R;
import com.example.kuldeepgupta.popularmoviesapp.data.adapter.MovieAdapter;
import com.example.kuldeepgupta.popularmoviesapp.provider.MovieSQLiteOpenHelper;
import com.example.kuldeepgupta.popularmoviesapp.provider.movie.MovieColumns;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.MovieUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by kuldeep.gupta on 30-01-2016.
 */
public class AsyncFavouriteMovieAdapter extends AbstractAsyncArrayAdapter<Movie> {

    private static final String TAG = AsyncFavouriteMovieAdapter.class.getName();
    private Context context;
    /**
     * checks whether any movie is marked as fav. Helps in choosing the pending view
     **/
    private static boolean favEmpty = true;
    //private View pendingView;

    public AsyncFavouriteMovieAdapter(Context context, int resource, List<Movie> movies) {
        super(new MovieAdapter(context, resource, movies));
        //setRunInBackground(false);
        this.context = context;
        if (favEmpty) {
            long favEntries = DatabaseUtils.queryNumEntries(MovieSQLiteOpenHelper.getInstance(context).getReadableDatabase(), MovieColumns.TABLE_NAME);
            if (favEntries > 0)
                favEmpty = false;
        }
    }

    @Override
    public View getPendingView(ViewGroup parent) {
        if (favEmpty) {
            // if no movies are marked as fav, return a blank place holder
            ImageView imgView = new ImageView(parent.getContext());
            imgView.setImageResource(R.drawable.blank);
            //pendingView = imgView;
            return imgView;
        } else {
            return super.getPendingView(parent);
        }

    }

   /* @Override
    protected void appendCachedData() {
        super.appendCachedData();
        stopAppending();
        if(pendingView != null) {
            Log.w(TAG,"Disabling pending view");
            pendingView.setVisibility(View.GONE);
            pendingView = null;
        }

    }*/
    /*@Override
    public void onDataReady() {
        Log.w(TAG,"onDataReady() called");
        super.onDataReady();
    }*/

    @Override
    protected List<Movie> loadListData(int page) {
        if (page == 1) {
            //Log.i(TAG, "Getting saved fav movies");
            List<Movie> favMovies = MovieUtil.getSavedMovies(context);
            //Log.i(TAG, "favMovies: " + favMovies);
            return favMovies;
            //return MovieUtil.getSavedMovies(context);
        }
        return Collections.emptyList();
    }
}
