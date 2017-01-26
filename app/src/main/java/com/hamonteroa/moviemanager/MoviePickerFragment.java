package com.hamonteroa.moviemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

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

import static com.hamonteroa.moviemanager.sync.TMDBNetworkUrl.LOG_TAG;

public class MoviePickerFragment extends Fragment {

    private SearchView mMovieSearchView;
    private ListView mMovieListView;

    private MovieAdapter mMovieAdapter;
    private ArrayList<TMDBMovie> mMovieArrayList;

    public MoviePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_picker, container, false);

        mMovieSearchView = (SearchView)rootView.findViewById(R.id.movie_searchView);
        mMovieListView = (ListView)rootView.findViewById(R.id.movie_listView);

        mMovieArrayList = new ArrayList<TMDBMovie>();

        mMovieAdapter = new MovieAdapter(getActivity(), mMovieArrayList);
        mMovieListView.setAdapter(mMovieAdapter);
        mMovieListView.setOnItemClickListener(mMovieAdapter);

        mMovieSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchMovie(newText);
                return false;
            }
        });

        return rootView;
    }

    private void searchMovie(String searchString) {
        Log.v(LOG_TAG, "searchMovie: " + searchString);

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, TMDBNetworkUrl.getMoviesWithSearchString(searchString), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray resultsMovies = response.getJSONArray(TMDBNetworkConstants.JSONResponseKeys.MOVIE_RESULTS);
                            mMovieArrayList = new ArrayList<TMDBMovie>();

                            Log.v(LOG_TAG, "getMoviesWithSearchString resultsMovies: " + resultsMovies);

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

                            mMovieAdapter.clear();
                            mMovieAdapter.addAll(mMovieArrayList);
                            mMovieAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            Log.e(LOG_TAG, "getWatchlistMovies-onResponse exception: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(LOG_TAG, "getWatchlistMovies-onErrorResponse error: " + error);
                    }


                })
        );
    }
}
