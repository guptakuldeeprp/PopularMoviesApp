package com.example.kuldeepgupta.popularmoviesapp.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuldeepgupta.popularmoviesapp.R;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.Movie;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.TmdbUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie>{

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

        TextView text=(TextView)view.findViewById(R.id.movie_caption);
        ImageView image=(ImageView)view.findViewById(R.id.movie_thumbnail);
        //Movie movie = (Movie) view.getTag();
        System.out.println("Movie tag in the view: " + movie);
        text.setText(movie.getTitle());

        Picasso.with(context)
                .load(tmdbUtil.getPosterUrl(movie))
                //.resize(100,100)
                //.centerCrop()
                .into(image);

        view.setTag(movie);
        return view;
    }
}