package com.example.kuldeepgupta.popularmoviesapp.util;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public interface AsyncResponse<T> {

    void processFinish(T response);

}
