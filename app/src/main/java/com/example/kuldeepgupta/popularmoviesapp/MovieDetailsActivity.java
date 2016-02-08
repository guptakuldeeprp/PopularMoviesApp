package com.example.kuldeepgupta.popularmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.util.CommonUtil;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;
    private CommonUtil cutil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cutil = CommonUtil.get();
        /*if(cutil.isLargeDevice()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_movie_details);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (savedInstanceState == null) {
            movie = (Movie) getIntent().getParcelableExtra(getString(R.string.movie_key));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_details_container, new MovieDetailsFragment())
                    .commit();
        }

        invalidateOptionsMenu();
        //setContentView(R.layout.activity_movie_details);


    }


}
