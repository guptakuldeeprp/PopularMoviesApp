package com.example.kuldeepgupta.popularmoviesapp.tmdb;

import android.content.Context;
import android.util.Log;

import com.example.kuldeepgupta.popularmoviesapp.R;
import com.example.kuldeepgupta.popularmoviesapp.tmdb.movie.Movie;
import com.example.kuldeepgupta.popularmoviesapp.util.http.HttpResponseWrapper;
import com.example.kuldeepgupta.popularmoviesapp.util.http.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public class TmdbUtil {

    private  static final String TAG = TmdbUtil.class.getName();

    // All String constants ending with PARAM represent request param keys to be used in forming URL for tmdb api.


    private static final String API_KEY_PARAM = "api_key";
    private static final String SORT_BY_PARAM = "sort_by";
    private static final String PAGE_PARAM = "page";

    // All String constants ending with KEY represent JSON keys obtained from the response of calling tmdb api.
    private static final String STATUS_MSG_KEY = "status_message";
    private static final String RESULTS_KEY = "results";
    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "original_title";
    private static final String POSTER_KEY = "poster_path";
    private static final String RELEADE_DATE_KEY = "release_date";
    private static final String OVERVIEW_KEY = "overview";
    private static final String VOTE_AVG_KEY = "vote_average";

    private static final String VIDEO_TRAILER_KEY = "key";
    private static final String REVIEW_AUTHOR_KEY = "author";
    private static final String REVIEW_CONTENT_KEY = "content";


    private Context context;
    private String baseUrl;
    private String apiBaseUrl;
    private static final TmdbUtil INSTANCE = new TmdbUtil();

//    public TmdbUtil(Context context) {
//
//        this.context = context;
//    }

    private TmdbUtil() {

    }



    public static TmdbUtil get()
    {
        //Log.w(TAG,"get without context called ");
        if(INSTANCE.context == null)
            throw new IllegalStateException("Context is not initialized!");
        return INSTANCE;
    }

    public static TmdbUtil get(Context context) {
        //Log.w(TAG,"get with context called: " + context);
        if(context != null)
            synchronized (INSTANCE){
                INSTANCE.setContext(context);
                INSTANCE.setApiBaseUrl(context.getString(R.string.tmdb_api_base_url));
            }
        return INSTANCE;
    }

    void setContext(Context context) {
        this.context = context;
    }

    void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getBaseUrl() {
//        AsyncHttpTask httpTask = new AsyncHttpTask();
//        httpTask.execute(appendApiKey(context.getString(R.string.tmdb_config)));
        if(baseUrl == null)
            baseUrl = "http://image.tmdb.org/t/p/";
        return baseUrl;
    }

    public String getYoutubeBaseUrl() {
        return context.getString(R.string.youtube_base_url);
    }

    private String getMovieDiscoveryUrl(int page, SortBy sortBy) {
        String movieDiscUrlStr = apiBaseUrl + context.getString(R.string.tmdb_movie_discover);
        movieDiscUrlStr = addQueryParam(movieDiscUrlStr, SORT_BY_PARAM, sortBy.toString());
        movieDiscUrlStr = addQueryParam(movieDiscUrlStr, PAGE_PARAM, String.valueOf(page));
        movieDiscUrlStr = appendApiKey(movieDiscUrlStr);
        return movieDiscUrlStr;
    }

    private String getMovieVideoUrl(long movieId) {
        return appendApiKey(apiBaseUrl + String.format(context.getString(R.string.tmdb_movie_video), movieId));
    }

    private String getMovieReviewUrl(long movieId) {
        return appendApiKey(apiBaseUrl + String.format(context.getString(R.string.tmdb_movie_review), movieId));
    }

    //TODO: Add validations
    private Movie buildMovieFromJson(JSONObject json) {
        return new Movie(json.optLong(ID_KEY), json.optString(TITLE_KEY), json.optString(POSTER_KEY), json.optString(RELEADE_DATE_KEY), json.optString(OVERVIEW_KEY), json.optDouble(VOTE_AVG_KEY));
    }

    /**
     * Specific to TMDB
     * @param response
     * @return
     */
    private String readErrResponseMsg(HttpResponseWrapper response) throws JSONException{
        String errMsg = "";
        if(response.isJsonResponseType())
        {

                JSONObject jsonObj = response.buildJsonFromError();
                if(jsonObj != null) {
                    errMsg = jsonObj.optString(STATUS_MSG_KEY);
                }

        } else
        {
            Log.w(TAG,"Response type is not json");
        }
        return errMsg;
    }

    public List<Movie> discoverMovies(int page, SortBy sortBy) {
        String movieDiscUrlStr = getMovieDiscoveryUrl(page, sortBy);
        //Log.w(TAG, "movieDiscUrlStr: " + movieDiscUrlStr);
        try {
            HttpResponseWrapper response = HttpHelper.get(movieDiscUrlStr);
            if(response.isFailed()) {
                Log.e(TAG,"HTTP request failed!");
                String errMsg = readErrResponseMsg(response);
                Log.e(TAG, "Failed to discover movies. Status Message: " + errMsg);
            } else {

                    JSONObject jsonResp = response.buildJsonFromResponse();
                    JSONArray results = jsonResp.getJSONArray(RESULTS_KEY);
                    if(results.length() > 0) {
                        List<Movie> movies = new ArrayList<>(results.length());
                        for (int i = 0; i < results.length(); i++) {
                            movies.add(buildMovieFromJson(results.getJSONObject(i)));
                        }
                        Log.v(TAG, "Total movies fetched: " + movies.size());
                        return movies;
                    }

            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            //e.printStackTrace();
        }
        catch (JSONException e) {
            Log.e(TAG, "Obtained response is not a valid JSON string: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public List<Movie> discoverMovies() {
        return discoverMovies(1, SortBy.DEFAULT);
    }

    /**
     * Returns list of review string for given movie.
     * The author of the review is merged with the review in the same string for convenience.
     * @param movieId
     * @param page
     * @return
     */
    //TODO: check whether a map of author and review seems more sensible
    public List<String> fetchReviews(long movieId, int page) {
        try {
            String reviewUrl = getMovieReviewUrl(movieId);
            //Log.w(TAG,"Movies Review URL: " + reviewUrl);
            HttpResponseWrapper response = HttpHelper.get(reviewUrl);
            if(response.isFailed()) {
                Log.e(TAG,"HTTP request failed!");
                String errMsg = readErrResponseMsg(response);
                Log.e(TAG, "Failed to discover movies. Status Message: " + errMsg);
            } else {

                JSONObject jsonResp = response.buildJsonFromResponse();
                JSONArray results = jsonResp.getJSONArray(RESULTS_KEY);
                if(results.length() > 0) {
                    List<String> reviews = new ArrayList<>(results.length());
                    for (int i = 0; i < results.length(); i++) {
                        reviews.add(buildReviewFromJson(results.getJSONObject(i)));
                    }
                    Log.w(TAG, "Total reviews fetched: " + reviews.size());
                    return reviews;
                } else {
                    Log.w(TAG, "No reviews fetched!");
                }

            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        catch (JSONException e) {
            Log.e(TAG, "Obtained response is not a valid JSON string: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    private String buildReviewFromJson(JSONObject jsonObject) {
        return jsonObject.optString(REVIEW_AUTHOR_KEY) + " - " + jsonObject.optString(REVIEW_CONTENT_KEY);
    }

    /**
     * mutator method to set/override the video trailer key of the movie argument
     * @param movie
     */
    public void setTrailerKeys(Movie movie) {
        movie.setTrailerKeys(getTrailerKeys(movie.getMid()));
    }


    public List<String> getTrailerKeys(long movieId) {

        try {
            String videoUrl = getMovieVideoUrl(movieId);
            //Log.w(TAG,"Movie trailer URL: " + videoUrl);
            HttpResponseWrapper response = HttpHelper.get(videoUrl);
            if(response.isFailed()) {
                Log.e(TAG,"HTTP request failed!");
                String errMsg = readErrResponseMsg(response);
                Log.e(TAG, "Failed to get trailer key for movieId ["+movieId+"]. Status Message: " + errMsg);
            } else {
                JSONObject jsonResp = response.buildJsonFromResponse();
                JSONArray results = jsonResp.getJSONArray(RESULTS_KEY);
                if(results.length() > 0) {
                    // just the first element of the array is considered
                    List<String> trailerKeys = new ArrayList<>();
                    for(int i = 0; i < results.length(); i++) {
                        trailerKeys.add(results.getJSONObject(i).optString(VIDEO_TRAILER_KEY));
                    }
                    Log.w(TAG, "Total trailers fetched: " + trailerKeys.size());
                    return trailerKeys;
                } else {
                    Log.w(TAG, "No trailers fetched!");
                }

            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (JSONException e) {
            Log.e(TAG, "Obtained response is not a valid JSON string: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }



    /**
     * A dumb encoder that appends the api_key request param at the end of url.
     * WIll need to use URLEncoder to correctly handle the encoding in case of complex URL strings.
     * @param url
     * @return
     */
    public String appendApiKey(String url) {
        return addQueryParam(url, API_KEY_PARAM, context.getString(R.string.tmdb_api_key));
    }

    private String addQueryParam(String url, String qkey, String qval) {

        URI uri = URI.create(url);
        if(uri.getRawQuery() != null) {
            url = uri + "&"+qkey+"=" + qval;
        } else {
            url = uri + "?"+qkey+"=" + qval;
        }
        return url;
    }

    public String getPosterUrl(Movie movie) {

        if(movie != null)
            return getBaseUrl() + context.getString(R.string.poster_size) + "/" + movie.getPosterPath();
        return null;
    }

    public String getMainPosterUrl(Movie movie) {
        if(movie != null)
            return getBaseUrl() + context.getString(R.string.poster_main_size) + "/" + movie.getPosterPath();
        return null;
    }


}
