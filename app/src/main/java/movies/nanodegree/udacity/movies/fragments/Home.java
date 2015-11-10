package movies.nanodegree.udacity.movies.fragments;


import android.app.Activity;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
import movies.nanodegree.udacity.movies.utils.EndlessRecyclerOnScrollListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends android.support.v4.app.Fragment {


    private static final String LOG_TAG = Home.class.getSimpleName();
    private ImageAdapter imageAdapter;
    private ArrayList<TMDbMovie> tmDbMovieArrayList = new ArrayList<>();
    private final static String PREF_MOVIES_INST = "pref_movies";
    private Callbacks mCallbacks = sDummyCallbacks;

    ProgressBar progress;

    TMDbMovie movieTMDb;

    OkHttpClient client = new OkHttpClient();

    private final int current_page = 1;
    private boolean alreadyLoaded = false;

    private String currentSortOrder;


    public Home() {
        // Required empty public constructor
    }

    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(ArrayList<TMDbMovie> movie,int pos);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(ArrayList<TMDbMovie> movie,int pos) {
        }
    };

    GridView movie;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        imageAdapter = new ImageAdapter(getActivity(), new ArrayList<TMDbMovie>());
        movie = (GridView)rootView.findViewById(R.id.grid);

        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

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
                mCallbacks.onItemSelected(tmDbMovieArrayList, position);
                //  startActivity(intent);
            }
        });

        movie.setOnScrollListener(new EndlessRecyclerOnScrollListener() {

            @Override
            public void onLoadMore(int current_page) {
                Log.d(LOG_TAG, "onLoadMore " + current_page);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String populated_movie_by = prefs.getString(getString(R.string.pref_sorting_key),
                        getString(R.string.sort_default));

                progress.setVisibility(View.VISIBLE);
                FetchMoviesPoster moviesTask = new FetchMoviesPoster();
                moviesTask.execute(populated_movie_by, String.valueOf(current_page));

            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String populated_movie_by = prefs.getString(getString(R.string.pref_sorting_key),
                getString(R.string.sort_default));

        currentSortOrder = populated_movie_by;


        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if(savedInstanceState == null || !savedInstanceState.containsKey(PREF_MOVIES_INST)) {


            if (tmDbMovieArrayList.size() == 0) {
                progress.setVisibility(View.VISIBLE);
                alreadyLoaded = true;
                FetchMoviesPoster moviesTask = new FetchMoviesPoster();
                moviesTask.execute(currentSortOrder, String.valueOf(current_page));
            }

        } else {
            tmDbMovieArrayList = savedInstanceState.getParcelableArrayList(PREF_MOVIES_INST);
            setPosterAdapter(tmDbMovieArrayList);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String populated_movie_by_resume = prefs.getString(getString(R.string.pref_sorting_key),
                getString(R.string.sort_default));



        if (currentSortOrder != populated_movie_by_resume){

            Toast.makeText(getActivity(),"Sort Order Changed",Toast.LENGTH_SHORT).show();
            tmDbMovieArrayList.clear();
            imageAdapter.clear();

            movie.setOnScrollListener(new EndlessRecyclerOnScrollListener() {

                @Override
                public void onLoadMore(int current_page) {
                    Log.d(LOG_TAG, "onLoadMore " + current_page);

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String populated_movie_by = prefs.getString(getString(R.string.pref_sorting_key),
                            getString(R.string.sort_default));

                    progress.setVisibility(View.VISIBLE);
                    FetchMoviesPoster moviesTask = new FetchMoviesPoster();
                    moviesTask.execute(populated_movie_by, String.valueOf(current_page));

                }
            });

            progress.setVisibility(View.VISIBLE);
            FetchMoviesPoster moviesTask = new FetchMoviesPoster();
            moviesTask.execute(populated_movie_by_resume, String.valueOf(current_page));
            currentSortOrder = populated_movie_by_resume;
        }



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(PREF_MOVIES_INST, tmDbMovieArrayList);
        super.onSaveInstanceState(outState);
    }

    private void setPosterAdapter(ArrayList<TMDbMovie> movies) {




        progress.setVisibility(View.GONE);
        Log.v(LOG_TAG, "Setting Adapter " + movies.size());

        imageAdapter.addAll(movies);
        this.imageAdapter.notifyDataSetChanged();

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

                backdrop_path = movie.getString(MovieConstants.TMDb.MJSN_BACKDROP_PATH);


                vote_count = movie.getString(MovieConstants.TMDb.MJSN_VOTE_COUNT);

                vote_average = movie.getString(MovieConstants.TMDb.MJSN_VOTE_RATE);

                release_date = movie.getString(MovieConstants.TMDb.MJSN_RELEASE_DATE);


                TMDbMovie TMDbmovie = new TMDbMovie(mid, poster_path, movie_title, release_date, vote_average, description,
                        vote_count,backdrop_path);


                movies.add(TMDbmovie);
                tmDbMovieArrayList.add(TMDbmovie);

            }
            Log.v(LOG_TAG,"Page "+current_page);
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

            String page_instance = params[1];

            Uri builtUri = Uri.parse(BASE_URL_TO_POPULATE_BY).buildUpon()
                    .appendQueryParameter(API_PARAM, MovieConstants.TMDb.API_KEY_MOVIEDB)
                    .appendQueryParameter(PAGE_PARAM, page_instance)
                    .build();
            Log.v(LOG_TAG, "Url " + builtUri);

            String response = null;
            try {
                response = run(builtUri.toString());
                return getMoviesDataFromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TMDbMovie> movies) {

            setPosterAdapter(movies);

        }
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
