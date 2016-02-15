package com.example.kuldeepgupta.popularmoviesapp.util.http;

import com.example.kuldeepgupta.popularmoviesapp.util.http.HttpResponseWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kuldeep.gupta on 25-12-2015.
 */
public class HttpHelper {

    private static final String CONTENT_TYPE = "content-type";

    public static HttpResponseWrapper get(String urlStr) throws IOException{
        HttpURLConnection urlConnection = null;


            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            String response = null;
            int code = urlConnection.getResponseCode();
            String responseType = urlConnection.getHeaderField(CONTENT_TYPE);

            if(code == HttpURLConnection.HTTP_OK) {

                response = readAsString(urlConnection.getInputStream(), true);
                return new HttpResponseWrapper(code, response, responseType);

            } else {
                response = readAsString(urlConnection.getErrorStream(), true);
                return new HttpResponseWrapper(code, "", responseType, true, response);
            }

    }

    public static String readAsString(InputStream inputStream, boolean appendNewLine) throws IOException {
        StringBuffer buffer = new StringBuffer();
        if (inputStream != null) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Adding a new line makes debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                if(appendNewLine)
                    buffer.append(line + "\n");
                else
                    buffer.append(line);
            }

        }
        return buffer.toString();
    }


}
