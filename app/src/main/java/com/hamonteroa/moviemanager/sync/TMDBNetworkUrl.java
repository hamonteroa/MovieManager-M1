package com.hamonteroa.moviemanager.sync;

import android.net.Uri;
import android.util.Log;

import com.hamonteroa.moviemanager.model.User;
import com.hamonteroa.moviemanager.sync.TMDBNetworkConstants.ApiConstants;
import com.hamonteroa.moviemanager.sync.TMDBNetworkConstants.ApiMethods;
import com.hamonteroa.moviemanager.sync.TMDBNetworkConstants.ParameterKeys;

import static com.hamonteroa.moviemanager.sync.TMDBNetworkConstants.ApiConstants.API_HOST;
import static com.hamonteroa.moviemanager.sync.TMDBNetworkConstants.ApiConstants.API_PATH;
import static com.hamonteroa.moviemanager.sync.TMDBNetworkConstants.ApiConstants.API_SCHEME;
import static com.hamonteroa.moviemanager.sync.TMDBNetworkConstants.ApiConstants.AUTHORIZATION_URL;

public class TMDBNetworkUrl {

    public static final String LOG_TAG = TMDBNetworkUrl.class.getSimpleName();


    private static Uri.Builder buildBaseUrl() {
        return new Uri.Builder()
                .scheme(API_SCHEME)
                .authority(API_HOST)
                .appendPath(API_PATH);
    }

    private static Uri.Builder buildBaseUrlWithApiKey() {
        return buildBaseUrl().
            appendQueryParameter(ParameterKeys.API_KEY, ApiConstants.API_KEY);
    }

    public static String getNewToken() {
        String url = buildBaseUrlWithApiKey()
                .appendEncodedPath(ApiMethods.AUTHENTICATION_TOKEN_NEW).build().toString();

        Log.v(LOG_TAG, "getNewToken: " + url);
        return url;
    }

    public static String loginWithToken() {
        String url = (new StringBuilder(AUTHORIZATION_URL).append(
                        User.getInstance().getRequestToken())).toString();
        Log.v(LOG_TAG, "loginWithToken: " + url);
        return url;
    }

    public static String getNewSessionID() {
        String url = buildBaseUrlWithApiKey()
                .appendEncodedPath(ApiMethods.AUTHENTICATION_SESSION_NEW)
                .appendQueryParameter(ParameterKeys.REQUEST_TOKEN, User.getInstance().getRequestToken()).build().toString();

        Log.v(LOG_TAG, "getNewSessionID: " + url);
        return url;
    }

    public static String getUserID() {
        String url = buildBaseUrlWithApiKey()
                .appendEncodedPath(ApiMethods.ACCOUNT)
                .appendQueryParameter(ParameterKeys.SESSION_ID, User.getInstance().getSessionID())
                .build().toString();
        Log.v(LOG_TAG, "getUserID: " + url);
        return url;
    }

    public static String getMoviesWithSearchString(String searchString) {
        String url = buildBaseUrlWithApiKey()
                .appendEncodedPath(ApiMethods.SEARCH_MOVIE)
                .appendQueryParameter(ParameterKeys.QUERY, searchString)
                .build().toString();
        Log.v(LOG_TAG, "getMoviesWithSearchString: " + url);
        return url;
    }

    public static String getWatchlistMovies() {
        String url = buildBaseUrlWithApiKey()
                .appendEncodedPath(ApiMethods.ACCOUNT_ID_WATCHLIST_MOVIES.replace(ApiMethods.ID_TO_REPLEACE, User.getInstance().getUserID()))
                .appendQueryParameter(ParameterKeys.SESSION_ID, User.getInstance().getSessionID())
                .build().toString();
        Log.v(LOG_TAG, "getWatchlistMovies: " + url);
        return url;
    }

    public static String getFavoritesMovies() {
        String url = buildBaseUrlWithApiKey()
                .appendEncodedPath(ApiMethods.ACCOUNT_ID_FAVORITE_MOVIES.replace(ApiMethods.ID_TO_REPLEACE, User.getInstance().getUserID()))
                .appendQueryParameter(ParameterKeys.SESSION_ID, User.getInstance().getSessionID())
                .build().toString();
        Log.v(LOG_TAG, "getFavoritesMovies: " + url);
        return url;
    }

    public static String postWatchlist() {
        String url = buildBaseUrlWithApiKey()
                .appendEncodedPath(ApiMethods.ACCOUNT_ID_WATCHLIST.replace(ApiMethods.ID_TO_REPLEACE, User.getInstance().getUserID()))
                .appendQueryParameter(ParameterKeys.SESSION_ID, User.getInstance().getSessionID())
                .build().toString();
        Log.v(LOG_TAG, "postWatchlist: " + url);
        return url;
    }

    public static String postFavorite() {
        String url = buildBaseUrlWithApiKey()
                .appendEncodedPath(ApiMethods.ACCOUNT_ID_FAVORITE.replace(ApiMethods.ID_TO_REPLEACE, User.getInstance().getUserID()))
                .appendQueryParameter(ParameterKeys.SESSION_ID, User.getInstance().getSessionID())
                .build().toString();
        Log.v(LOG_TAG, "postFavorite: " + url);
        return url;
    }

    public static String getImageMoviePoster(String moviePosterPath) {
        String url = (new StringBuilder(ApiConstants.BASE_IMAGE_URL))
                .append(TMDBNetworkConstants.Configuration.POSTER_SIZE)
                .append(moviePosterPath).toString();

        Log.v(LOG_TAG, "getImageMoviePoster: " + url);
        return url;
    }

    public static String getConfig() {
        String url = buildBaseUrlWithApiKey()
                .appendEncodedPath(ApiMethods.CONFIG)
                .build().toString();

        Log.v(LOG_TAG, "getConfig: " + url);
        return url;
    }
}
