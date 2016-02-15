package com.example.kuldeepgupta.popularmoviesapp.data.adapter.async;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.example.kuldeepgupta.popularmoviesapp.R;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuldeep.gupta on 19-01-2016.
 */
public  abstract class AbstractAsyncArrayAdapter<T> extends EndlessAdapter {
    private static final String TAG = AbstractAsyncArrayAdapter.class.getName();
    protected int page;
    protected RotateAnimation rotate = null;
    protected View pendingView = null;
    protected List<T> list;

    public AbstractAsyncArrayAdapter(ArrayAdapter<T> wrapped) {
        this(wrapped, 0);
    }

    public AbstractAsyncArrayAdapter(ArrayAdapter<T> wrapped, int page) {
        super(wrapped);
        this.page = page;
        rotate=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(600);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
    }

    public View getPendingView(ViewGroup parent) {

        if(pendingView == null)
        {
            ImageView imgView = new ImageView(parent.getContext());
            imgView.setImageResource(R.drawable.ic_popup_sync_1);
            pendingView = imgView;
        }
        pendingView.setVisibility(View.VISIBLE);
        startProgressAnimation();
        return pendingView;

    }

    void startProgressAnimation() {
        if (pendingView!=null) {
            pendingView.startAnimation(rotate);
        }
    }



    @Override
    protected boolean cacheInBackground() throws Exception {

        ++page;
        list = loadListData(page);
        if(list == null || list.isEmpty()) {
            --page; // restore back the page
            return false;
        }
        else
            return true;
    }

    @Override
    protected void appendCachedData() {

        ArrayAdapter<T> wrappedAdapter =  (ArrayAdapter<T>) getWrappedAdapter();
        if(wrappedAdapter != null) {
            if (list != null && !list.isEmpty()) {
                wrappedAdapter.addAll(list);
                list = null; //help GC

            }
        }
        else
        {
            Log.e(TAG,"No wrapped ArrayAdapter found!");
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getUnderlyingList()
    {
        ArrayAdapter<T> wrappedAdapter =  (ArrayAdapter<T>) getWrappedAdapter();
        if(wrappedAdapter != null)
        {
            List<T> underlying =  new ArrayList<>();
            for (int i = 0; i < wrappedAdapter.getCount(); i++)
                underlying.add((T) wrappedAdapter.getItem(i));
            return underlying;

        }

        return null;
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    protected boolean onException(View pendingView, Exception e) {
        Log.e(TAG,e.getMessage(),e);
        return true;

    }
    public ArrayAdapter getDelegateAdapter() {
        return (ArrayAdapter) getWrappedAdapter();
    }

    protected abstract List<T> loadListData(int page);
}
