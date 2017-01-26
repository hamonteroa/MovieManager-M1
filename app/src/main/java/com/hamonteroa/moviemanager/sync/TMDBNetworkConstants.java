package com.hamonteroa.moviemanager.sync;

/**
 * Created by hamonteroa on 1/13/17.
 */

public class TMDBNetworkConstants {

    public class ApiConstants {

        public static final String API_KEY =  "c3d2f84e3eb79f0d9c0a2096e922ff11";

        public static final String API_SCHEME = "https";
        public static final String API_HOST = "api.themoviedb.org";
        public static final String API_PATH = "3";
        public static final String AUTHORIZATION_URL = "https://www.themoviedb.org/authenticate/";

        public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    }

    public class ApiMethods {

        public static final String ACCOUNT = "account";
        public static final String ACCOUNT_ID_FAVORITE_MOVIES = "account/{id}/favorite/movies";
        public static final String ACCOUNT_ID_FAVORITE = "account/{id}/favorite";
        public static final String ACCOUNT_ID_WATCHLIST_MOVIES = "account/{id}/watchlist/movies";
        public static final String ACCOUNT_ID_WATCHLIST = "account/{id}/watchlist";

        public static final String ID_TO_REPLEACE = "{id}";

        // MARK: Authentication
        public static final String AUTHENTICATION_TOKEN_NEW = "authentication/token/new";
        public static final String AUTHENTICATION_SESSION_NEW = "authentication/session/new";

        // MARK: Search
        public static final String SEARCH_MOVIE = "search/movie";

        // MARK: Config
        public static final String CONFIG = "configuration";

    }

    public class ApiURLKeys {

        public static final String USER_ID = "USER_ID";

    }

    public class ParameterKeys {

        public static final String API_KEY = "api_key";
        public static final String SESSION_ID = "session_id";
        public static final String REQUEST_TOKEN = "request_token";
        public static final String QUERY = "query";

    }

    public class JSONBodyKeys {

        public static final String MEDIA_TYPE = "media_type";
        public static final String MEDIA_ID = "media_id";
        public static final String FAVORITE = "favorite";
        public static final String WATCHLIST = "watchlist";

    }

    public class JSONResponseKeys {

        // General
        public static final String STATUS_MESSAGE = "status_message";
        public static final String STATUS_CODE = "status_code";
        public static final String SUCCESS = "success";

        // Authorization
        public static final String REQUEST_TOKEN = "request_token";
        public static final String SESSION_ID = "session_id";
        public static final String EXPIRES_AT = "expires_at";

        // Account
        public static final String USER_ID = "id";

        // Movies
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_POSTER_PATH = "poster_path";
        public static final String MOVIE_RELEASE_DATE = "release_date";
        public static final String MOVIE_RELEASE_YEAR = "release_year";
        public static final String MOVIE_RESULTS = "results";
        public static final String MOVIE_OVERVIEW = "overview";

        // Config
        public static final String CONFIG_BASE_IMAGE_URL = "base_url";
        public static final String CONFIG_SECURE_BASE_IMAGE_URL = "secure_base_url";
        public static final String CONFIG_IMAGES = "images";
        public static final String CONFIG_POSTER_SIZES = "poster_sizes";
        public static final String CONFIG_PROFILE_SIZES = "profile_sizes";

    }

    public static final class Configuration {

        public static final String[] POSTER_SIZES = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};

        public static final String ROW_POSTER = POSTER_SIZES[2];
        public static final String POSTER_SIZE = POSTER_SIZES[4];

        /*
        var daysSinceLastUpdate: Int? {
        if let lastUpdate = dateUpdated {
            //return Int(NSDate().timeIntervalSinceDate(lastUpdate)) / 60*60*24
            return Int(NSDate().timeIntervalSince(lastUpdate as Date)) / 60*60*24
        } else {
            return nil
        }
         */

    }

}
