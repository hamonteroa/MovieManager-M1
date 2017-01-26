package com.hamonteroa.moviemanager;

import android.os.Bundle;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.hamonteroa.moviemanager.model.TMDBMovie;
import com.hamonteroa.moviemanager.model.User;
import com.hamonteroa.moviemanager.sync.TMDBNetworkConstants;
import com.hamonteroa.moviemanager.sync.TMDBNetworkUrl;
import com.hamonteroa.moviemanager.sync.VolleySingleton;
import com.hamonteroa.moviemanager.utility.MMConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailMovieActivity.class.getSimpleName();
    private static final float IMAGE_FULL_OPAQUE = 1f;
    private static final float IMAGE_MEDIUM_OPAQUE = 0.5f;

    //    private NetworkImageView mPosterNetworkImageView;
    private ImageView mPosterImageView;
    private TextView mOverviewTextView;

    private Toolbar mActionToolbar;
    private ImageButton mWatchlistImageButton, mFavoriteImageButton;
    private Space mNr1SpaceToolbar, mNr2SpaceToolbar, mNr3SpaceToolbar;

    private TMDBMovie mMovie;
    private boolean isWatchlist = false, isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        User.getInstance().print();

        mMovie = (new Gson()).fromJson(getIntent().getStringExtra(MMConstants.MOVIE), TMDBMovie.class);

//        mPosterNetworkImageView = (NetworkImageView) findViewById(R.id.poster_networkImageView);
//        mPosterNetworkImageView.setImageUrl(TMDBNetworkUrl.getImageMoviePoster(mMovie.getPosterPath()), VolleySingleton.getInstance(this).getImageLoader());

        mPosterImageView = (ImageView)findViewById(R.id.poster_imageView);
        VolleySingleton.getInstance(this).getImageLoader().get(TMDBNetworkUrl.getImageMoviePoster(mMovie.getPosterPath()), new ImageLoader.ImageListener() {

            public void onErrorResponse(VolleyError error) {
                Log.v(LOG_TAG, "onErrorResponse");
                showErrorMessage(false, error.getMessage());
//                mPosterImageView.setImageResource(null); // set an error image if the download fails
            }

            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                Log.v(LOG_TAG, "onResponse bitmap: " + response.getBitmap());
                if (response.getBitmap() != null) {
                    mPosterImageView.setImageBitmap(response.getBitmap());
                }
            }
        });

        mActionToolbar = (Toolbar) findViewById(R.id.action_toolbar);
        mNr1SpaceToolbar = (Space) findViewById(R.id.nr1_space_toolbar);
        mNr2SpaceToolbar = (Space) findViewById(R.id.nr2_space_toolbar);
        mNr3SpaceToolbar = (Space) findViewById(R.id.nr3_space_toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOverviewTextView = (TextView) findViewById(R.id.overview_textView);
        mOverviewTextView.setText(mMovie.getOverview());

        mWatchlistImageButton = (ImageButton) findViewById(R.id.watchlist_imageButton);
        mWatchlistImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAsWatchlist();
            }
        });

        mFavoriteImageButton = (ImageButton) findViewById(R.id.favorite_imageButton);
        mFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAsFavorite();
            }
        });
        setToolbarEvenDistribution();
        checkIfIsWatchlist();
        checkIfIsFavorite();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mNr3SpaceToolbar.post(new Runnable() {
            @Override
            public void run() {
                setToolbarEvenDistribution();
            }
        });
    }

    private void checkIfIsWatchlist() {
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, TMDBNetworkUrl.getWatchlistMovies(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray resultsMovies = response.getJSONArray(TMDBNetworkConstants.JSONResponseKeys.MOVIE_RESULTS);

                    for (int i = 0; i < resultsMovies.length(); i++) {
                        Object object = resultsMovies.getJSONObject(i);
                        JSONObject resultMovie = (JSONObject) object;
                        if (resultMovie.has(TMDBNetworkConstants.JSONResponseKeys.MOVIE_ID) && mMovie.getId() == resultMovie.getInt(TMDBNetworkConstants.JSONResponseKeys.MOVIE_ID)) {
                            isWatchlist = true;
                            mWatchlistImageButton.setImageResource(R.drawable.watchlist_filled);
                        }
                    }

                    if (!isWatchlist) {
                        mWatchlistImageButton.setImageResource(R.drawable.watchlist_empty);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMessage(true, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorMessage(true, error.getMessage());
            }
        }
        ));
    }

    private void checkIfIsFavorite() {
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, TMDBNetworkUrl.getFavoritesMovies(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray resultsMovies = response.getJSONArray(TMDBNetworkConstants.JSONResponseKeys.MOVIE_RESULTS);

                    for (int i = 0; i < resultsMovies.length(); i++) {
                        Object object = resultsMovies.getJSONObject(i);
                        JSONObject resultMovie = (JSONObject) object;
                        if (resultMovie.has(TMDBNetworkConstants.JSONResponseKeys.MOVIE_ID) && mMovie.getId() == resultMovie.getInt(TMDBNetworkConstants.JSONResponseKeys.MOVIE_ID)) {
                            isFavorite = true;
                            mFavoriteImageButton.setImageResource(R.drawable.star_filled);
                        }

                        if (!isFavorite) {
                            mFavoriteImageButton.setImageResource(R.drawable.star_empty);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMessage(true, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorMessage(true, error.getMessage());
            }
        }
        ));

    }

    private void markAsWatchlist() {
        final boolean markAsWatchlist = !isWatchlist;

        StringBuilder jsonBody = new StringBuilder("{\"");
        jsonBody.append(TMDBNetworkConstants.JSONBodyKeys.MEDIA_TYPE).append("\": \"movie\", \"")
                .append(TMDBNetworkConstants.JSONBodyKeys.MEDIA_ID).append("\": ").append(mMovie.getId()).append(", \"")
                .append(TMDBNetworkConstants.JSONBodyKeys.WATCHLIST).append("\": ").append(markAsWatchlist).append("}");

        try {
            JSONObject jsonObject = new JSONObject(jsonBody.toString());
            Log.v(LOG_TAG, "postWatchlist jsonBody: " + jsonBody.toString());
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(Request.Method.POST, TMDBNetworkUrl.postWatchlist(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean(TMDBNetworkConstants.JSONResponseKeys.SUCCESS)) {
                            if (isWatchlist = markAsWatchlist) {
                                mWatchlistImageButton.setImageResource(R.drawable.watchlist_filled);
                            } else {
                                mWatchlistImageButton.setImageResource(R.drawable.watchlist_empty);
                            }
                        } else {
                            showErrorMessage(true, response.getString(TMDBNetworkConstants.JSONResponseKeys.STATUS_CODE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorMessage(true, e.getMessage());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showErrorMessage(true, error.getMessage());
                }
            }
            ));
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage(true, e.getMessage());
        }
    }

    private void markAsFavorite() {
        final boolean markAsFavorite = !isFavorite;

        StringBuilder jsonBody = new StringBuilder("{\"");
        jsonBody.append(TMDBNetworkConstants.JSONBodyKeys.MEDIA_TYPE).append("\": \"movie\", \"")
                .append(TMDBNetworkConstants.JSONBodyKeys.MEDIA_ID).append("\": ").append(mMovie.getId()).append(", \"")
                .append(TMDBNetworkConstants.JSONBodyKeys.FAVORITE).append("\": ").append(markAsFavorite).append("}");

        try {
            JSONObject jsonObject = new JSONObject(jsonBody.toString());
            Log.v(LOG_TAG, "postFavorite jsonBody: " + jsonBody.toString());
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(Request.Method.POST, TMDBNetworkUrl.postFavorite(), jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean(TMDBNetworkConstants.JSONResponseKeys.SUCCESS)) {
                            if (isFavorite = markAsFavorite) {
                                mFavoriteImageButton.setImageResource(R.drawable.star_filled);
                            } else {
                                mFavoriteImageButton.setImageResource(R.drawable.star_empty);
                            }
                        } else {
                            showErrorMessage(false, response.getString(TMDBNetworkConstants.JSONResponseKeys.STATUS_CODE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorMessage(false, e.getMessage());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showErrorMessage(false, error.getMessage());
                }
            }
            ));

        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage(false, e.getMessage());
        }
    }

    private void showErrorMessage(boolean isWatchlist, String errorMessage) {
        int resourceMessage = 0;
        if (isWatchlist) {
            Log.v(LOG_TAG, "Watchlist error: " + errorMessage);
            resourceMessage = R.string.error_posting_watchlist;
        } else {
            Log.v(LOG_TAG, "Favorite error: " + errorMessage);
            resourceMessage = R.string.error_posting_favorite;
        }

        Toast.makeText(this, (new StringBuilder(getString(resourceMessage))).append(": ").append(errorMessage), Toast.LENGTH_SHORT).show();

    }

    private void setToolbarEvenDistribution() {
        int watchlistIconWidth = this.mWatchlistImageButton.getWidth();
        int favoriteIconWidth = this.mFavoriteImageButton.getWidth();
        int toolbarWidth = this.mActionToolbar.getWidth();
        int spaceWidth = (toolbarWidth - watchlistIconWidth - favoriteIconWidth) / 3;

        Log.v(LOG_TAG, "setToolbarEvenDistribution, cameraIconWidth: " + watchlistIconWidth
                + ", albumIconWidth: " + watchlistIconWidth
                + ", favoriteIconWidth: " + favoriteIconWidth
                + ", spaceWidth: " + spaceWidth
        );

        this.mNr1SpaceToolbar.setMinimumWidth(spaceWidth);
        this.mNr2SpaceToolbar.setMinimumWidth(spaceWidth);
        this.mNr3SpaceToolbar.setMinimumWidth(spaceWidth);
    }
}
