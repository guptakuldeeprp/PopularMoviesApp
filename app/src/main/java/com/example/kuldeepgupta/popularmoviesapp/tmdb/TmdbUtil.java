package com.example.kuldeepgupta.popularmoviesapp.tmdb;

import android.content.Context;
import android.util.Log;

import com.example.kuldeepgupta.popularmoviesapp.R;
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

    private  static final String NAME = TmdbUtil.class.getName();

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

    public static TmdbUtil get() {
        return INSTANCE;
    }

    public static TmdbUtil get(Context context) {
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

    private String getMovieDiscoveryUrl(int page, SortBy sortBy) {
        String movieDiscUrlStr = apiBaseUrl + context.getString(R.string.tmdb_movie_discover);
        movieDiscUrlStr = addQueryParam(movieDiscUrlStr, SORT_BY_PARAM, sortBy.toString());
        movieDiscUrlStr = addQueryParam(movieDiscUrlStr, PAGE_PARAM, String.valueOf(page));
        movieDiscUrlStr = appendApiKey(movieDiscUrlStr);
        return movieDiscUrlStr;
    }

    //TODO: Add validations
    private Movie buildMovieFromJson(JSONObject json) {
        return new Movie(json.optLong(ID_KEY), json.optString(TITLE_KEY), json.optString(POSTER_KEY), json.optString(RELEADE_DATE_KEY), json.optString(OVERVIEW_KEY), json.optDouble(VOTE_AVG_KEY));
    }

    public List<Movie> discoverMovies(int page, SortBy sortBy) {
        String movieDiscUrlStr = getMovieDiscoveryUrl(page, sortBy);
        Log.v(NAME, "movieDiscUrlStr: " + movieDiscUrlStr);
        try {
            HttpResponseWrapper response = HttpHelper.get(movieDiscUrlStr);
            if(response.isFailed()) {
                String errMsg = "";
                if(response.isJsonResponseType())
                {
                    Log.i(NAME, "error response: " + response.getError());
                    try {
                        JSONObject jsonObj = response.buildJsonFromError();
                        if(jsonObj != null) {
                            errMsg = jsonObj.optString(STATUS_MSG_KEY);
                        }
                    } catch (JSONException e) {
                        Log.w(NAME,"Failed to build json from resp: " + e.getMessage());
                    }
                } else
                {
                    Log.w(NAME,"Response type is not json");
                }
                Log.e(NAME, "Failed to discover movies. Status Message: " + errMsg);
            } else {
                try {
                    JSONObject jsonResp = response.buildJsonFromResponse();
                    JSONArray results = jsonResp.getJSONArray(RESULTS_KEY);

                    List<Movie> movies = new ArrayList<>(results.length());
                    for(int i = 0; i < results.length(); i++) {
                        movies.add(buildMovieFromJson(results.getJSONObject(i)));
                    }
                    Log.v(NAME,"Total movies fetched: " + movies.size());
                    return movies;

                } catch (JSONException e) {
                    Log.e(NAME,"Obtained response is not a valid JSON string");
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            Log.e(NAME, e.getMessage(), e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Movie> discoverMovies() {
        return discoverMovies(1, SortBy.DEFAULT);
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

        return getBaseUrl() + context.getString(R.string.poster_size) + "/" + movie.getPosterPath();
    }

    public String getMainPosterUrl(Movie movie) {

        return getBaseUrl() + context.getString(R.string.poster_main_size) + "/" + movie.getPosterPath();
    }


}
