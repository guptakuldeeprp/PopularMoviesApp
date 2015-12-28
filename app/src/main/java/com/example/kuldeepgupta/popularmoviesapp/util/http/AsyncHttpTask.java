package com.example.kuldeepgupta.popularmoviesapp.util.http;

import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 * Supports only GET requests as of now.
 * Will have to add support for other HTTP verbs as required.
 */
public class AsyncHttpTask extends AsyncTask<String, Void, HttpResponseWrapper>{

//    private AsyncResponse<HttpResponseWrapper> delegate;
//
//    public AsyncHttpTask(AsyncResponse<HttpResponseWrapper> delegate) {
//        this.delegate = delegate;
//    }

    private HttpResponseWrapper response;

    public HttpResponseWrapper getResponse() {
        return response;
    }

    @Override
    protected HttpResponseWrapper doInBackground(String... params) {
        if(params.length != 0)
            throw new IllegalArgumentException("One String URL expected.");
        try {
            HttpHelper.get(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(HttpResponseWrapper httpResponseWrapper) {
        super.onPostExecute(httpResponseWrapper);
        this.response = httpResponseWrapper;
        //delegate.processFinish(httpResponseWrapper);
    }
}
