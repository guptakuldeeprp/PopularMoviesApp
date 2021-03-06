package com.example.kuldeepgupta.popularmoviesapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;
import com.example.kuldeepgupta.popularmoviesapp.util.CommonUtil;

public class MainActivity extends AppCompatActivity implements MovieGridFragment.OnFragmentInteractionListener {


    private static final String TAG = MainActivity.class.getName();
    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "MovieDetailsFragment";
    private TmdbUtil tbdbUtil;
    private CommonUtil cutil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.w(TAG,"onCreate MainActivity called");
        super.onCreate(savedInstanceState);
        tbdbUtil = TmdbUtil.get(getApplicationContext());
        cutil = CommonUtil.get(getApplicationContext());
        setContentView(R.layout.activity_main);
        //MovieDetailsFragment
        removeDetailsFragment();

        findViewById(R.id.fragment_details_container).setVisibility(View.GONE);
        //invalidateOptionsMenu();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.ic_popup_sync_1);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, MovieGridFragment.newInstance())
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onFragmentInteraction(Movie movie) {

        if (cutil.isLargeDevice()) {
            findViewById(R.id.fragment_details_container).setVisibility(View.VISIBLE);
            Log.w(TAG, "This is a a large device");
            Bundle arguments = new Bundle();
            Log.w(TAG, "Setting movie to argument: " + movie);
            arguments.putParcelable(getString(R.string.movie_key), movie);

            MovieDetailsFragment fragment = MovieDetailsFragment.newInstance();
            fragment.setArguments(arguments);
            //getSupportFragmentManager().beginTransaction().replace(R.id.movie_details_fragment_inner, fragment).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_details_container, fragment, MOVIE_DETAILS_FRAGMENT_TAG).commit();
        } else {

            Log.w(TAG, "This is a small device");
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.movie_key), movie);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void removeDetailsFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MOVIE_DETAILS_FRAGMENT_TAG);

        if (fragment != null) {
            Log.w(TAG, "Removing fragment " + MOVIE_DETAILS_FRAGMENT_TAG);
            fragment.setMenuVisibility(false);

            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

}
