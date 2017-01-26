package com.hamonteroa.moviemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hamonteroa.moviemanager.model.TMDBMovie;
import com.hamonteroa.moviemanager.sync.TMDBNetworkConstants;
import com.hamonteroa.moviemanager.sync.TMDBNetworkUrl;
import com.hamonteroa.moviemanager.sync.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private static final String LOG_TAG = FavoritesFragment.class.getSimpleName();

    private ProgressBar mLoadingProgressBar;
    private ListView mFavoriteListView;

    private ArrayList<TMDBMovie> mMovieArrayList;

    public FavoritesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        mLoadingProgressBar = (ProgressBar) rootView.findViewById(R.id.loading_progressBar);
        mFavoriteListView = (ListView) rootView.findViewById(R.id.favorite_listView);

        mLoadingProgressBar.setVisibility(View.VISIBLE);

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, TMDBNetworkUrl.getFavoritesMovies(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray resultsMovies = response.getJSONArray(TMDBNetworkConstants.JSONResponseKeys.MOVIE_RESULTS);
                            mMovieArrayList = new ArrayList<TMDBMovie>();

//                            Log.v(LOG_TAG, "getFavoritesMovies resultsMovies: " + resultsMovies);

                            for (int i = 0; i < resultsMovies.length(); i++) {
                                Object object = resultsMovies.getJSONObject(i);
                                JSONObject resultMovie = (JSONObject)object;
                                mMovieArrayList.add(new TMDBMovie(
                                        resultMovie.has(TMDBNetworkConstants.JSONResponseKeys.MOVIE_TITLE) ? resultMovie.getString(TMDBNetworkConstants.JSONResponseKeys.MOVIE_TITLE) : "",
                                        resultMovie.has(TMDBNetworkConstants.JSONResponseKeys.MOVIE_ID) ? resultMovie.getInt(TMDBNetworkConstants.JSONResponseKeys.MOVIE_ID) : -1,
                                        resultMovie.has(TMDBNetworkConstants.JSONResponseKeys.MOVIE_POSTER_PATH) ? resultMovie.getString(TMDBNetworkConstants.JSONResponseKeys.MOVIE_POSTER_PATH) : "",
                                        resultMovie.has(TMDBNetworkConstants.JSONResponseKeys.MOVIE_RELEASE_DATE) ? resultMovie.getString(TMDBNetworkConstants.JSONResponseKeys.MOVIE_RELEASE_DATE) : "",
                                        resultMovie.has(TMDBNetworkConstants.JSONResponseKeys.MOVIE_OVERVIEW) ? resultMovie.getString(TMDBNetworkConstants.JSONResponseKeys.MOVIE_OVERVIEW) : ""
                                ));
                            }

                            MovieAdapter adapter = new MovieAdapter(getContext(), mMovieArrayList);

                            mFavoriteListView.setAdapter(adapter);
                            mFavoriteListView.setOnItemClickListener(adapter);
                            mLoadingProgressBar.setVisibility(View.GONE);

                        } catch (Exception e) {
                            Log.e(LOG_TAG, "getFavoritesMovies-onResponse exception: " + e.getMessage());
                            e.printStackTrace();

                            mLoadingProgressBar.setVisibility(View.GONE);

                            new AlertDialog.Builder(getContext())
                                    .setTitle("Error")
                                    .setMessage((new StringBuilder(getString(R.string.error_getting_movies))).append(": ").append(e.getMessage()))
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(LOG_TAG, "getFavoritesMovies-onErrorResponse error: " + error);
                    }
                }));

        return rootView;
    }
}
