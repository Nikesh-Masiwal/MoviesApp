package movies.nanodegree.udacity.movies.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import movies.nanodegree.udacity.movies.MovieConstants;
import movies.nanodegree.udacity.movies.MovieDetail;
import movies.nanodegree.udacity.movies.R;
import movies.nanodegree.udacity.movies.adapter.ImageAdapter;
import movies.nanodegree.udacity.movies.parcel.TMDbMovie;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    private ImageAdapter imageAdapter;
    private ArrayList<TMDbMovie> tmDbMovieArrayList;
    private final static String PREF_MOVIES_INST = "pref_movies";

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        imageAdapter = new ImageAdapter(getActivity(), new ArrayList<TMDbMovie>());
        GridView movie = (GridView)rootView.findViewById(R.id.grid);
        movie.setAdapter(imageAdapter);
        movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idMovie = imageAdapter.getItem(position).getId();
                Intent intent = new Intent(getActivity(), MovieDetail.class);
                intent.putExtra(MovieConstants.MovieHashMapKeys.M_ID, idMovie);
                intent.putExtra("title", imageAdapter.getItem(position).getTitle());
                intent.putExtra("description", imageAdapter.getItem(position).getSynopsis());
                intent.putExtra("backdrop_path", imageAdapter.getItem(position).getPosterW342());
                intent.putExtra("vote_avg", imageAdapter.getItem(position).getRate());
                intent.putExtra("total_votes", imageAdapter.getItem(position).getRateForRatingBar());
                intent.putExtra("releasedate", imageAdapter.getItem(position).getDuration());
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if(savedInstanceState == null || !savedInstanceState.containsKey(PREF_MOVIES_INST)) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String populated_movie_by = prefs.getString(getString(R.string.pref_sorting_key),
                    getString(R.string.sort_default));


            FetchMoviesPoster moviesTask = new FetchMoviesPoster();
            moviesTask.execute(populated_movie_by);

        } else {
            tmDbMovieArrayList = savedInstanceState.getParcelableArrayList(PREF_MOVIES_INST);
            setPosterAdapter();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String populated_movie_by = prefs.getString(getString(R.string.pref_sorting_key),
                getString(R.string.sort_default));

//            FetchMoviesPoster moviesTask = new FetchMoviesPoster();
//            moviesTask.execute(populated_movie_by);



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(PREF_MOVIES_INST, tmDbMovieArrayList);
        super.onSaveInstanceState(outState);
    }

    private void setPosterAdapter() {

        Log.v("Home Fragemnt","Setting Adapter");

        imageAdapter.clear();
        imageAdapter.addAll(tmDbMovieArrayList);

       // Log.v("Home Fragemnt", "Added Adapter" + tmDbMovieArrayList.toString());
    }

    public class FetchMoviesPoster extends AsyncTask<String, Void, ArrayList<TMDbMovie>> {

        private final String LOG_TAG = FetchMoviesPoster.class.getSimpleName();



        private ArrayList<TMDbMovie> getMoviesDataFromJson(String movieJson)
                throws JSONException {



            JSONObject moviesJsonResult = new JSONObject(movieJson);
            JSONArray moviesArray = moviesJsonResult.getJSONArray(MovieConstants.TMDb.MJSN_RESULT);

            ArrayList<TMDbMovie> movies = new ArrayList<TMDbMovie>();

            String[] imagePosters = new String[moviesArray.length()];
            for(int i = 0; i < moviesArray.length(); i++) {
                // Defining String to Store Data from Movies JSON
                String movie_title;
                String description;
                String poster_path;
                String backdrop_path;
                String vote_average;
                String vote_count;
                String release_date;
                String mid;

                // Get the JSON object representing the movie
                JSONObject movie = moviesArray.getJSONObject(i);
                mid = movie.getString(MovieConstants.TMDb.MJSN_ID);
                movie_title = movie.getString(MovieConstants.TMDb.MJSN_TITLE);
                description = movie.getString(MovieConstants.TMDb.MJSN_OVERVIEW);
                poster_path = movie.getString(MovieConstants.TMDb.MJSN_POSTER_PATH);

                poster_path = "http://image.tmdb.org/t/p/w342/"+poster_path;

                backdrop_path = movie.getString(MovieConstants.TMDb.MJSN_BACKDROP_PATH);
                vote_count = movie.getString(MovieConstants.TMDb.MJSN_VOTE_COUNT);

                vote_average = movie.getString(MovieConstants.TMDb.MJSN_VOTE_RATE);

                release_date = movie.getString(MovieConstants.TMDb.MJSN_RELEASE_DATE);

                TMDbMovie TMDbmovie = new TMDbMovie(mid, poster_path, movie_title, release_date, vote_average, description,
                        vote_count);
                movies.add(TMDbmovie);

            }

            return movies;


        }


        @Override
        protected ArrayList<TMDbMovie> doInBackground(String... params) {


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            //Define constants

            //Construct Base URL and URI
            //final String FORECAST_BASE_URL = "http://private-795ef-themoviedb.apiary-mock.com/3/discover/movie?";
            String populate_by;
            String BASE_URL_TO_POPULATE_BY;
            if (params.length != 0)
            {
                populate_by = params[0];
            }else {
                populate_by = "popular";
            }

            if (populate_by.equalsIgnoreCase("popular")){
                BASE_URL_TO_POPULATE_BY  = MovieConstants.TMDb.POPULAR_MOVIE_BASE_URL;
            }else {
                BASE_URL_TO_POPULATE_BY  = MovieConstants.TMDb.TOPRATED_MOVIE_BASE_URL;
            }


            final String API_PARAM = "api_key";
            final String PAGE_PARAM = "page";

            Uri builtUri = Uri.parse(BASE_URL_TO_POPULATE_BY).buildUpon()
                    .appendQueryParameter(API_PARAM, MovieConstants.TMDb.API_KEY_MOVIEDB)
                    .appendQueryParameter(PAGE_PARAM, "1")
                    .build();
            Log.v(LOG_TAG, "Url " + builtUri);


            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // Create the request to TMDb, and open the connection
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                urlConnection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            try {
                urlConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Read the input stream into a String
            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();

            Log.v(LOG_TAG,"Movies "+moviesJsonStr);

            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TMDbMovie> movies) {


            tmDbMovieArrayList = movies;
            setPosterAdapter();

        }
    }


}
