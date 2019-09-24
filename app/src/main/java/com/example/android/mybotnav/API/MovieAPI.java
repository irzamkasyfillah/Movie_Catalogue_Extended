package com.example.android.mybotnav.API;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class MovieAPI {
    private static final String API_KEY = "7ea833b4ece11fde1ceed2b26787fa17";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String URL_SEARCH = "https://api.themoviedb.org/3/search/movie";
    private static final String URL_DISCOVER = "https://api.themoviedb.org/3/discover/movie";

    public static URL getURL(String t) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(t)
                .appendQueryParameter("api_key", API_KEY).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getSearchURL(String query) {
        Uri uri = Uri.parse(URL_SEARCH).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("query", query).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getDiscoverURL(String date) {
        Uri uri = Uri.parse(URL_DISCOVER).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("primary_release_date.gte", date)
                .appendQueryParameter("primary_release_date.lte", date).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
