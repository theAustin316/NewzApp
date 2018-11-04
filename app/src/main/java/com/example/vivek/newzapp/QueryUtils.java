package com.example.vivek.newzapp;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    public static List<Newz> fetchNewzData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Newz}s
        List<Newz> news = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Newz}s
        return news;
    }

    private static List<Newz> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<Newz> news = new ArrayList<>();
        try {

            // Create a JSONObject from the JSON response string
            JSONObject reader = new JSONObject(jsonResponse);
            JSONObject response = reader.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of attributes.
            JSONArray resultArray = response.getJSONArray("results");

            // For each result in the resultArray, create an {@link NEwz} object
            for(int i=0; i< resultArray.length(); i++) {

                // Get a single news at position i within the list of articles
                JSONObject newses = resultArray.getJSONObject(i);

                // For a given news, extract the JSONObject associated with the
                // key called "fields", which represents a list of all authorName
                // and trailText for that article.
                JSONObject jsonObjectFields = newses.getJSONObject("fields");

                // Extract the value for the key called "webTitle"
                String title = newses.optString("webTitle");
                // Extract the value for the key called "webPublicationDate"
                String date = newses.optString("webPublicationDate");
                // Extract the value for the key called "sectionName"
                String section = newses.optString("sectionName");
                // Extract the value for the key called "webUrl"
                String url =  newses.optString("webUrl");
                // Extract the value for the key called "trailText"
                String trailTxt = jsonObjectFields.optString("trailText");
                // Extract the value for the key called "byLine"
                String author = jsonObjectFields.optString("byline");


                // Create a new {@link Newz} object with the date, section, title,
                // trailText, url and author from the JSON response.
                Newz n = new Newz(date, section, title, html2text(trailTxt), url, author);

                // Add the new {@link Newz} to the list of news.
                news.add(n);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // return the list of news
        return news;
    }

    // Converts trailText from html format to text
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
}
