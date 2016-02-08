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

import com.example.kuldeepgupta.popularmoviesapp.data.adapter.async.AsyncReviewAdapter;
import com.example.kuldeepgupta.popularmoviesapp.data.adapter.async.AsyncTrailerAdapter;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.MovieUtil;
import com.example.kuldeepgupta.popularmoviesapp.util.CommonUtil;
import com.squareup.picasso.Picasso;

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

        //Log.w(TAG, getActivity().getClass().getName());
        if (cutil.isLargeDevice()) {
            Log.w(TAG, "created as a fragment within " + getActivity().getClass().getName());
            movie = (Movie) getArguments().getParcelable(getString(R.string.movie_key));
        } else {
            Log.w(TAG, "created as a fragment in new " + getActivity().getIntent().toString());
            if (getActivity().getIntent().getExtras() != null)
                movie = (Movie) getActivity().getIntent().getExtras().getParcelable(getString(R.string.movie_key));
        }
        /*if (getActivity().getIntent() != null) {
            Log.w(TAG,"created as a fragment of MovieDetailsActivity " + getActivity().getIntent().toString());
            if (getActivity().getIntent().getExtras() != null)
                movie = (Movie) getActivity().getIntent().getExtras().getParcelable(getString(R.string.movie_key));
        } else {
            Log.w(TAG,"created as a fragment of MainActivity");
            movie = (Movie) getArguments().getParcelable(getString(R.string.movie_key));
        }*/

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
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

            //ArrayAdapter<String> reviewAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_review, R.id.review, (List<String>) null);

            ArrayAdapter<String> reviewAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_review, R.id.review, new ArrayList<String>());
            AsyncReviewAdapter endlessReviewAdapter = new AsyncReviewAdapter(reviewAdapter, movie);
            ListView reviewList = (ListView) rootView.findViewById(R.id.movie_reviews);
            reviewList.setAdapter(endlessReviewAdapter);

            //ArrayAdapter<String> trailerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_trailer, R.id.trailer_caption, (List<String>) null);
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
            Picasso.with(getActivity())
                    .load(tmdbUtil.getMainPosterUrl(movie))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                            //   .resize(350,350)
                            //.resize(150,150)
                            //.centerCrop()
                    .into(imgView);


        }


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/

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

    /*public void onTrailerClick(View view) {
        Log.i(TAG, "onTrailerClick called");
        TextView tv = (TextView) view;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieUtil.getTrailerAppUrlFromDesc(movie, tv.getText().toString().trim())));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(MovieUtil.getTrailerWebUrlFromDesc(movie, tv.getText().toString().trim())));
            startActivity(intent);
        }


    }
*/
   /* public void onFavoutireClick(View view) {
        Log.i(TAG, "onFavoutireClick called");
        movie.setIsFav(true);
        String fileSaveLoc = cutil.saveImg(tmdbUtil.getPosterUrl(movie), movie.getPosterPath());
        movie.setPosterSaveLoc(fileSaveLoc);
        MovieUtil.saveMovie(getActivity(), movie);
    }*/

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
                String fileSaveLoc = cutil.saveImg(tmdbUtil.getPosterUrl(movie), movie.getPosterPath());
                movie.setPosterSaveLoc(fileSaveLoc);
                MovieUtil.saveMovie(getActivity(), movie);
            } else {
                Log.e(TAG, "No movie tag found in the view");
            }
        }
    }

  /*  @Override
    public void onDetach() {
        super.onDetach();
        *//*if (!cutil.isLargeDevice()) // If we are detaching this fragment because we are no longer on a large device, then we should invalidate the menu options generated by this fragment
        {
            Log.w(TAG,"Got back to small screen. Hiding menu item");
            setMenuVisibility(false);
        }*//*
    }*/
}
