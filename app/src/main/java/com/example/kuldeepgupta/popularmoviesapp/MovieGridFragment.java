package com.example.kuldeepgupta.popularmoviesapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.kuldeepgupta.popularmoviesapp.data.adapter.async.AbstractAsyncArrayAdapter;
import com.example.kuldeepgupta.popularmoviesapp.data.adapter.async.AsyncFavouriteMovieAdapter;
import com.example.kuldeepgupta.popularmoviesapp.data.adapter.async.AsyncMovieAdapter;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.SortBy;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.MovieUtil;
import com.example.kuldeepgupta.popularmoviesapp.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieGridFragment extends Fragment {

    private static final String TAG = MovieGridFragment.class.getName();
    private static final String MOVIE_KEY = "MOVIES";

    private OnFragmentInteractionListener mListener;
    private AbstractAsyncArrayAdapter movieAdapter;
    private GridView grid;
    private boolean isFavView;
    private CommonUtil cutil;
    private boolean moreMoviesToFetch = true;
    //private int pageNo;

    List<Movie> movies;

    public MovieGridFragment() {
        // Required empty public constructor
    }


    public static MovieGridFragment newInstance() {
        MovieGridFragment fragment = new MovieGridFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (cutil == null)
            cutil = CommonUtil.get();
        movies = new ArrayList<>();
        if (savedInstanceState != null) {
            isFavView = savedInstanceState.getBoolean(getString(R.string.isFavView), false);
            movies = (List<Movie>) savedInstanceState.get(getString(R.string.movie_arr_key));
            if (movies == null) {
                movies = new ArrayList<>();
            }
            if (isFavView && movies.size() > 0)
                moreMoviesToFetch = false;
        } else {
            movies = new ArrayList<>();
        }
        setHasOptionsMenu(true);
    }

    private void notifySortChange() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getString(R.string.isFavView), isFavView);
        List<Movie> list = movieAdapter.getUnderlyingList();
        if (list != null) {
            outState.putParcelableArrayList(getString(R.string.movie_arr_key), (ArrayList<? extends Parcelable>) list);
            //Log.w(TAG, "Saving pageNo: " + movieAdapter.getPage());
            //outState.putInt(getString(R.string.movie_page), movieAdapter.getPage());

        }
    }

    private boolean refreshMovieGrid(SortBy sortBy) {
        mListener.removeDetailsFragment();
        if (movieAdapter != null) {
            movieAdapter.getDelegateAdapter().clear();
            if (isFavView) {
                isFavView = false;
                movieAdapter = createMovieAdapter(isFavView, sortBy);
            }
            moreMoviesToFetch = true;
            movieAdapter.setPage(0);
            // ideally should not use casting.

            //TODO: Find a better way to achieve this
            ((AsyncMovieAdapter) movieAdapter).setSortBy(sortBy);
            grid.setAdapter(movieAdapter);
            movieAdapter.getDelegateAdapter().notifyDataSetChanged();
            movieAdapter.notifyDataSetChanged();
            return true;
        } else {
            Log.e(TAG, "movieAdapter is not found in the fragment!");
            return false;
        }

    }

    private boolean refreshFavMovieGrid() {
        mListener.removeDetailsFragment();
        if (movieAdapter != null) {
            movieAdapter.getDelegateAdapter().clear();
            if (!isFavView) {
                isFavView = true;
                movieAdapter = createMovieAdapter(isFavView, null);
            }
            moreMoviesToFetch = true;
            movieAdapter.setPage(0);
            grid.setAdapter(movieAdapter);
            movieAdapter.getDelegateAdapter().notifyDataSetChanged();
            movieAdapter.notifyDataSetChanged();
            return true;
        } else {
            Log.e(TAG, "movieAdapter is not found in the fragment!");
            return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_popularity:
                return refreshMovieGrid(SortBy.DEFAULT);
            case R.id.mi_rating:
                return refreshMovieGrid(new SortBy(SortBy.Option.vote_average, SortBy.Order.desc));
            case R.id.mi_fav:
                return refreshFavMovieGrid();
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.grid_movie, container, false);

        AbstractAsyncArrayAdapter<Movie> arrAdapter = null;
        movieAdapter = createMovieAdapter(isFavView, SortBy.DEFAULT);


        grid = (GridView) rootView.findViewById(R.id.grid_movie);
        grid.setAdapter(movieAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) view.getTag();
                if (movie == null) {
                    Log.e(TAG, "No movie tag found in the view");
                    return;
                }
                mListener.onFragmentInteraction(movie);

            }
        });
        return rootView;

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

    /**
     * Too simple a object creation to move into a separate factory.
     * As and when the movie adapter types increases, need to use the type enum in a separate factory
     *
     * @param isFav
     * @param sortBy
     * @return
     */
    public AbstractAsyncArrayAdapter<Movie> createMovieAdapter(boolean isFav, SortBy sortBy) {
        if (movies == null)
            movies = new ArrayList<>();
        if (isFav) {
            AbstractAsyncArrayAdapter<Movie> adapter = new AsyncFavouriteMovieAdapter(getActivity(), R.layout.fragment_movie, movies);
            if (moreMoviesToFetch)
                adapter.setPage(0);
            else
                adapter.setPage(1);
            //return new AsyncFavouriteMovieAdapter(getActivity(), R.layout.fragment_movie, movies);
            return adapter;
        } else {
            AbstractAsyncArrayAdapter<Movie> adapter = new AsyncMovieAdapter(getActivity(), R.layout.fragment_movie, movies, sortBy);
            //adapter.setPage(pageNo);
            //return new AsyncMovieAdapter(getActivity(), R.layout.fragment_movie, movies, sortBy);
            return adapter;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

        void onFragmentInteraction(Movie movie);

        void removeDetailsFragment();
    }
}
