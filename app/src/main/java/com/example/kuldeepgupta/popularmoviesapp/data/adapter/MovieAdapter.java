package com.example.kuldeepgupta.popularmoviesapp.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuldeepgupta.popularmoviesapp.R;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = MovieAdapter.class.getName();
    private Context context;
    private int layoutResourceId;
    //private List<Movie> mGridData = new ArrayList<Movie>();
    private List<Movie> gridData;
    private TmdbUtil tmdbUtil;

    public MovieAdapter(Context context, int layoutResourceId, List<Movie> dataList) {
        //this(context, layoutResourceId, dataList, TmdbUtil.get());
        super(context, layoutResourceId, dataList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.gridData = dataList;
        tmdbUtil = TmdbUtil.get();

    }


//    public MovieAdapter(Context context, int layoutResourceId, List<Movie> dataList, TmdbUtil tmdbUtil) {
//        super(context, layoutResourceId, dataList);
//        this.context = context;
//        this.layoutResourceId = layoutResourceId;
//        this.gridData = dataList;
//        this.tmdbUtil = tmdbUtil;
//    }


    /**
     * Updates grid data and refresh grid items.
     *
     * @param gridData
     */
    public void setGridData(ArrayList<Movie> gridData) {
        this.gridData = gridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, parent, false);
        }

        Movie movie = gridData.get(position);

        TextView text = (TextView) view.findViewById(R.id.movie_caption);
        ImageView image = (ImageView) view.findViewById(R.id.movie_thumbnail);
        //Movie movie = (Movie) view.getTag();
        text.setText(movie.getTitle());

        Uri imgUri = null;
        if (movie.isFav()) {
            //Log.w(TAG, "Getting img from saveLoc: " + movie.getPosterSaveLoc());
            imgUri = Uri.fromFile(new File(movie.getPosterSaveLoc()));
        } else {
            //Log.w(TAG, "Getting img from remote api: " + tmdbUtil.getMainPosterUrl(movie));
            imgUri = Uri.parse(tmdbUtil.getPosterUrl(movie));

        }
        Picasso.with(context)
                //.load(tmdbUtil.getPosterUrl(movie))
                .load(imgUri)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fit()
                        //.resize(150,150)
                        //.centerCrop()
                .into(image);
        view.setTag(movie);
        return view;
    }


}
