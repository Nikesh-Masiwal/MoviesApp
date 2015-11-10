package movies.nanodegree.udacity.movies;

/**
 * Created by Nikesh-MBP on 11/07/15.
 */
public class MovieConstants {

    public class TMDb {

        //Constants for Movie Database API

        public static final String BASE_TMDB_URL = "http://api.themoviedb.org/3";
        public static final String BASE_MOVIE_ENDPOINT = "/movie";
        public static final String BASE_API_MOVIE_ENDPOINT = BASE_TMDB_URL+BASE_MOVIE_ENDPOINT;

        public static final String POPULAR_MOVIE_BASE_URL = BASE_API_MOVIE_ENDPOINT+"/popular?";
        public static final String TOPRATED_MOVIE_BASE_URL = BASE_API_MOVIE_ENDPOINT+"/top_rated?";

        public static final String VIDEOS_RELATED_TO_MOVIE = "/videos?";

        public static final String REVIEWS_RELATED_TO_MOVIE = "/reviews?";



        public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

        // INSERT KEY HERE
        public static final String API_KEY_MOVIEDB = "";

        //public static final String FONT_BOLD_PATH = "fonts/Sintony-Bold.ttf";
        public static final String FONT_BOLD_PATH = "fonts/Megrim.ttf";
        public static final String FONT_REGULAR_PATH = "fonts/Sintony-Regular.ttf";


        // These are the names of the JSON objects that need to be extracted.
        public final static String MJSN_ID = "id";
        public static final String MJSN_RESULT = "results";
        public static final String MJSN_ORGTITLE = "original_title";
        public static final String MJSN_OVERVIEW = "overview";
        public static final String MJSN_TITLE = "title";
        public static final String MJSN_POSTER_PATH = "poster_path";
        public static final String MJSN_VOTE_RATE = "vote_average";
        public static final String MJSN_VOTE_COUNT = "vote_count";
        public static final String MJSN_RELEASE_DATE = "release_date";
        public static final String MJSN_BACKDROP_PATH = "backdrop_path";


        public static final String FAVOURITE_SET = "pref_movieID";
        public static final String FAVOURITE_MOVIE = "favourite_movie";



    }

    public class MovieHashMapKeys {

        // These are keys for HashMap.
        public final static String M_ID = "id";
        public static final String M_TITLE = "movie_title";
        public static final String M_DESCRIPTION = "movie_description";
        public static final String M_POSTER_PATH = "poster_path";
        public static final String M_BACKDROP_PATH = "backdrop_path";
        public static final String M_AVG_VOTES = "avg_votes";
        public static final String M_VOTES_COUNT = "total_votes";
        public static final String M_RELEASE_DATE = "release_date";



    }


}
