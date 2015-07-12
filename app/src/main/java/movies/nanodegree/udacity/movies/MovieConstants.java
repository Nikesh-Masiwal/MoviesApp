package movies.nanodegree.udacity.movies;

/**
 * Created by Nikesh-MBP on 11/07/15.
 */
public class MovieConstants {

    public class TMDb {

        //Constants for Movie Database API

        public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        protected static final String API_KEY_MOVIEDB = "59f4a7727c4d04f1e3138fd764cbe60e";


        // These are the names of the JSON objects that need to be extracted.
        public static final String MJSN_RESULT = "results";
        public static final String MJSN_ORGTITLE = "original_title";
        public static final String MJSN_OVERVIEW = "overview";
        public static final String MJSN_TITLE = "title";
        public static final String MJSN_POSTER_PATH = "poster_path";
        public static final String MJSN_VOTE_RATE = "vote_average";
        public static final String MJSN_VOTE_COUNT = "vote_count";
        public static final String MJSN_RELEASE_DATE = "release_date";
        public static final String MJSN_BACKDROP_PATH = "backdrop_path";




    }

    public class MovieHashMapKeys {

        // These are keys for HashMap.

        public static final String M_TITLE = "movie_title";
        public static final String M_DESCRIPTION = "movie_description";
        public static final String M_POSTER_PATH = "poster_path";
        public static final String M_BACKDROP_PATH = "backdrop_path";
        public static final String M_AVG_VOTES = "avg_votes";
        public static final String M_VOTES_COUNT = "total_votes";
        public static final String M_RELEASE_DATE = "release_date";



    }


}
