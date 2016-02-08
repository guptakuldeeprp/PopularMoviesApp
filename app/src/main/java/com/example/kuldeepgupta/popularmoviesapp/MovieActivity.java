package com.example.kuldeepgupta.popularmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    private TmdbUtil tmdbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        tmdbUtil = TmdbUtil.get();
        Movie movie = (Movie) getIntent().getParcelableExtra("movie");

        TextView titleView = (TextView) findViewById(R.id.movie_title);
        titleView.setText(movie.getTitle());

        TextView synopsisView = (TextView) findViewById(R.id.movie_synopsis);
        synopsisView.setText(movie.getPlotSynopsis());

        TextView releaseView = (TextView) findViewById(R.id.movie_release);
        releaseView.setText(movie.getReleaseDate());

        TextView ratingView = (TextView) findViewById(R.id.movie_rating);
        ratingView.setText(String.valueOf(movie.getUserRating()));

        RatingBar ratingBar = (RatingBar) findViewById(R.id.movie_rating_stars);
        ratingBar.setRating((float)movie.getUserRating()/2.0F);

        ImageView imgView = (ImageView) findViewById(R.id.movie_img);

        Picasso.with(this)
                .load(tmdbUtil.getMainPosterUrl(movie))
                     //   .resize(350,350)
                //.resize(150,150)
                //.centerCrop()
                .into(imgView);



    }

}
