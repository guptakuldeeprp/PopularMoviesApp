package com.example.kuldeepgupta.popularmoviesapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuldeepgupta.popularmoviesapp.data.adapter.async.AsyncReviewAdapter;
import com.example.kuldeepgupta.popularmoviesapp.data.adapter.async.AsyncTrailerAdapter;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.MovieUtil;
import com.example.kuldeepgupta.popularmoviesapp.util.CommonUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private static final String TAG = MovieDetailsFragment.class.getName();
    private OnFragmentInteractionListener mListener;
    private Movie movie;
    private TmdbUtil tmdbUtil;
    private CommonUtil cutil;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieDetailsFragment.
     */

    public static MovieDetailsFragment newInstance() {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        tmdbUtil = TmdbUtil.get();
        cutil = CommonUtil.get();
        if (savedInstanceState != null)
            handleSavedInstance(savedInstanceState);
        else
            handleNewInstance();
        if (movie == null) {
            Log.w(TAG, "No movie instance passed to the fragment!");
            //throw new IllegalStateException("No movie instance passed to the fragment!");
        }

        setHasOptionsMenu(true);
    }

    private void handleSavedInstance(Bundle savedInstanceState) {
        movie = (Movie) savedInstanceState.getParcelable(getString(R.string.movie_key));
    }

    private void handleNewInstance() {


        if (cutil.isLargeDevice()) {

            if (getArguments() != null)
                movie = (Movie) getArguments().getParcelable(getString(R.string.movie_key));
        } else {

            if (getActivity().getIntent().getExtras() != null)
                movie = (Movie) getActivity().getIntent().getExtras().getParcelable(getString(R.string.movie_key));
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_details, menu);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.movie_key), movie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        if (movie != null) {
            rootView.setTag(movie);
            TextView titleView = (TextView) rootView.findViewById(R.id.movie_title);
            titleView.setText(movie.getTitle());

            TextView synopsisView = (TextView) rootView.findViewById(R.id.movie_synopsis);
            synopsisView.setText(movie.getPlotSynopsis());

            TextView releaseView = (TextView) rootView.findViewById(R.id.movie_release);
            releaseView.setText(movie.getReleaseDate());

            TextView ratingView = (TextView) rootView.findViewById(R.id.movie_rating);
            ratingView.setText(String.valueOf(movie.getUserRating()));

            RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.movie_rating_stars);
            ratingBar.setRating((float) movie.getUserRating() / 2.0F);

            ImageView favImgView = (ImageView) rootView.findViewById(R.id.movie_favourite_img);
            favImgView.setOnClickListener(new FavouriteClickListener(movie));



            ArrayAdapter<String> reviewAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_review, R.id.review, new ArrayList<String>());
            AsyncReviewAdapter endlessReviewAdapter = new AsyncReviewAdapter(reviewAdapter, movie);
            ListView reviewList = (ListView) rootView.findViewById(R.id.movie_reviews);
            reviewList.setAdapter(endlessReviewAdapter);


            AsyncTrailerAdapter endlessTrailerAdapter = null;
            if (movie.getTrailerKeys() != null && movie.getTrailerKeys().size() != 0) {
                ArrayAdapter<String> trailerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_trailer, R.id.trailer_caption, new ArrayList<>(MovieUtil.getTrailerKeysDescMap(movie).keySet()));
                endlessTrailerAdapter = new AsyncTrailerAdapter(trailerAdapter, movie);
                endlessTrailerAdapter.setPage(1); // this will prevent further api call over network
            } else {
                ArrayAdapter<String> trailerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_trailer, R.id.trailer_caption, new ArrayList<String>());
                endlessTrailerAdapter = new AsyncTrailerAdapter(trailerAdapter, movie);
            }


            ListView trailerList = (ListView) rootView.findViewById(R.id.movie_trailers);
            trailerList.setAdapter(endlessTrailerAdapter);

            trailerList.setOnItemClickListener(new TrailerClickListener(movie));

            ImageView imgView = (ImageView) rootView.findViewById(R.id.movie_img);

            Uri imgUri = null;
            if (movie.isFav()) {
                imgUri = Uri.fromFile(new File(movie.getPosterSaveLoc()));
            } else {
                imgUri = Uri.parse(tmdbUtil.getMainPosterUrl(movie));
            }
            Log.w(TAG, "Img URI: " + imgUri);
            Picasso.with(getActivity())
                    //.load(tmdbUtil.getMainPosterUrl(movie))
                    .load(imgUri)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imgView);

        }


        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialize the loader here
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.share_trailer:
                if (movie != null) {
                    if (movie.getTrailerKeys() != null && !movie.getTrailerKeys().isEmpty()) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_SUBJECT, "Trailer URL");
                        share.putExtra(Intent.EXTRA_TEXT, MovieUtil.getTrailerWebUrlFromKey(movie.getTrailerKeys().get(0))); // share the first URL
                        startActivity(Intent.createChooser(share, "Share trailer link!"));
                    } else {
                        Log.w(TAG, "No trailer found for movie " + movie.getTitle());
                    }
                }
        }
        return true;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class TrailerClickListener implements AdapterView.OnItemClickListener {

        private Movie movie;

        public TrailerClickListener(Movie movie) {
            this.movie = movie;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (movie == null) {
                Log.e(TAG, "No movie tag found in the view");
                return;
            }
            TextView tv = (TextView) view.findViewById(R.id.trailer_caption);
            if (tv != null) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieUtil.getTrailerAppUrlFromDesc(movie, tv.getText().toString().trim())));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(MovieUtil.getTrailerWebUrlFromDesc(movie, tv.getText().toString().trim())));
                    startActivity(intent);
                }
            } else {
                Log.e(TAG, "Could not find text view in the trailer list view");
            }
        }
    }

    public class FavouriteClickListener implements View.OnClickListener {

        private Movie movie;

        public FavouriteClickListener(Movie movie) {
            this.movie = movie;
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "onFavoutireClick called");
            if (movie != null) {
                movie.setIsFav(true);
                String fileSaveLoc = cutil.saveImg(tmdbUtil.getMainPosterUrl(movie), movie.getPosterPath());
                movie.setPosterSaveLoc(fileSaveLoc);
                MovieUtil.saveMovie(getActivity(), movie);
                Toast.makeText(getActivity(), "Added to favourites!",
                        Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG, "No movie tag found in the view");
            }
        }
    }

}
