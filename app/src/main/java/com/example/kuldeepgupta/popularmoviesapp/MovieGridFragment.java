package com.example.kuldeepgupta.popularmoviesapp;

import android.content.Context;
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
import android.widget.GridView;

import com.example.kuldeepgupta.popularmoviesapp.data.adapter.EndlessMovieAdapter;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.SortBy;

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

    private static final String NAME = MovieGridFragment.class.getName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EndlessMovieAdapter ema;
    private GridView grid;

    public MovieGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieGridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieGridFragment newInstance(String param1, String param2) {
        MovieGridFragment fragment = new MovieGridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie, menu);
    }

    /**
     * Helper method to refresh the entire GridView based on the new SortBy criteria
     * @param sortBy
     * @return
     */
    private boolean refreshGrid(SortBy sortBy) {
        if(ema != null && grid != null) {
            Log.v(NAME,"Refreshing movie grid with new sortby condition: " + sortBy);
            ema.setSortBy(sortBy);
            ema.setPage(1);
            ema.getDelegateAdapter().clear();
            ema.getDelegateAdapter().notifyDataSetChanged();
            ema.notifyDataSetChanged();
            grid.setAdapter(ema);

            return true;
        } else{
            if(ema == null)
                Log.e(NAME,"EndlessMovieAdapter is not initialized!");
            if(grid == null)
                Log.e(NAME,"Movie GridView is not initialized!");
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_popularity:
                return refreshGrid(SortBy.DEFAULT);
            case R.id.mi_rating:
                return refreshGrid(new SortBy(SortBy.Option.vote_average, SortBy.Order.desc));
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.grid_movie, container, false);
        List<Movie> movies = new ArrayList<>();
        ema = new EndlessMovieAdapter(getActivity(),R.layout.fragment_movie, movies, SortBy.DEFAULT);
        grid = (GridView) rootView.findViewById(R.id.grid_movie);
        grid.setAdapter(ema);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) view.getTag();
                if(movie == null) {
                    Log.e(NAME, "No movie tag found in the view");
                    return;
                }

                Intent intent = new Intent(getActivity(), MovieActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie", movie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
