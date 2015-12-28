package com.example.kuldeepgupta.popularmoviesapp.util.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public class HttpResponseWrapper {

    private static final String NAME = HttpResponseWrapper.class.getName();
    private static final String JSON_RESPONSE_TYPE = "application/json";

    private int status;
    private boolean failed;
    private String response;
    private String error;
    private String responseType;

    public HttpResponseWrapper(int status, String response, String responseType) {
        this(status, response, responseType, false, "");
    }

    public HttpResponseWrapper(int status, String response, String responseType, boolean failed, String error) {
        this.status = status;
        this.response = response;
        this.responseType = responseType;
        this.failed = failed;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public boolean isJsonResponseType() {
        if(responseType != null)
            return responseType.contains(JSON_RESPONSE_TYPE);
        else
            return false;
    }

    public JSONObject buildJsonFromError() throws JSONException {
        if(!failed)
            return null;
        else if("".equals(error)) {
            Log.w(NAME, "No error string obtained.");
            return null;
        }
        else
            return getJsonObj(error);
    }

    public JSONObject buildJsonFromResponse() throws JSONException {
        if(failed)
            return null;
        else if("".equals(response)) {
            Log.w(NAME, "No response obtained.");
            return null;
        }
        else
            return getJsonObj(response);
    }

    private JSONObject getJsonObj(String s) throws JSONException {
        return new JSONObject(s);
    }
}
