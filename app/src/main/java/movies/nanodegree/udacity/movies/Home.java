package movies.nanodegree.udacity.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.HashMap;

import movies.nanodegree.udacity.movies.adapter.ImageAdapter;


public class Home extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private final String LOG_TAG = Home.class.getSimpleName();

    private GridView mGridView;
    private ImageAdapter mAdapter;
    ProgressDialog progress;

    ArrayList<HashMap<String, String>> moviesList = new ArrayList<HashMap<String, String>>();

    HashMap<String, String> moviesMap;

    ArrayList<String> postersArrayList;
    String title,description,backdrop_path,vote_avg,total_votes,releasedate;

    //Boolean to indicate if a api call is needed after Settings Change
    public static boolean needARefresh;

    //Setter for the Above
    public void setNeedARefresh(boolean needARefresh) {
        this.needARefresh = needARefresh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Defining ArrayAdapter
        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setOnItemClickListener(this);

        //Checnking for SavedInstanceState
        if(savedInstanceState != null) {

            ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();
            ArrayList<String> postersAL = new ArrayList<String>();

            //Getting Serialized ArrayList from Bundle
            List = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("movieListArrayLists");

            //Getting Serialized String array of URLs
            postersAL = savedInstanceState.getStringArrayList("posters");

            //Experienced a problem of crash on 3 rotation back to back posterArrayList was null, hence..
            postersArrayList = postersAL;

            //Assigning current ArrayList with Saved.
            moviesList = List;

            if (postersAL.size() != 0) {

                //Calling the Adapter
                String[] arrayPosters = postersAL.toArray(new String[postersAL.size()]);

                mAdapter = new ImageAdapter(getBaseContext(), arrayPosters);
                mGridView.setAdapter(mAdapter);
            }


        }else {
            //Else Get those movies
            updateMovies();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //Saving instance on Rotations
        outState.putSerializable("movieListArrayLists", moviesList);
        outState.putStringArrayList("posters", postersArrayList);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //Call update only if needArefresh is chnaged
        if (needARefresh == true){
            updateMovies();
            needARefresh = false;
        }

    }

    public void updateMovies(){

        //Getting Shared Pref for Popular/Top Rated Setting
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String populated_movie_by = prefs.getString(getString(R.string.pref_sorting_key),
                getString(R.string.sort_default));

        //Calling Async : TODO : Replace it with Retrofit
        FetchMoviesPoster fetchMoviesPoster = new FetchMoviesPoster();
        progress = ProgressDialog.show(this, "Fetching Movies",
                "Grab a Popcorn, We are fetching Movies for you.", true);
        fetchMoviesPoster.execute(populated_movie_by);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(Home.this,SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         moviesMap = moviesList.get(position);

        //Reading Values from HashMap
         title = moviesMap.get(MovieConstants.MovieHashMapKeys.M_TITLE);
         description = moviesMap.get(MovieConstants.MovieHashMapKeys.M_DESCRIPTION);
         backdrop_path = moviesMap.get(MovieConstants.MovieHashMapKeys.M_BACKDROP_PATH);
         vote_avg = moviesMap.get(MovieConstants.MovieHashMapKeys.M_AVG_VOTES);
         total_votes = moviesMap.get(MovieConstants.MovieHashMapKeys.M_VOTES_COUNT);
         releasedate = moviesMap.get(MovieConstants.MovieHashMapKeys.M_RELEASE_DATE);

        //Starting Intent
        Intent intent = new Intent(Home.this, MovieDetail.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("backdrop_path", backdrop_path);
        intent.putExtra("vote_avg", vote_avg);
        intent.putExtra("total_votes", total_votes);
        intent.putExtra("releasedate", releasedate);

        startActivity(intent);
    }




    public class FetchMoviesPoster extends AsyncTask<String,String[],String[]>{

        private final String LOG_TAG = FetchMoviesPoster.class.getSimpleName();



        private String[] getMoviesDataFromJson(String movieJson)
                throws JSONException {



            JSONObject moviesJsonResult = new JSONObject(movieJson);
            JSONArray moviesArray = moviesJsonResult.getJSONArray(MovieConstants.TMDb.MJSN_RESULT);



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

                // Get the JSON object representing the movie
                JSONObject movie = moviesArray.getJSONObject(i);

                movie_title = movie.getString(MovieConstants.TMDb.MJSN_TITLE);
                description = movie.getString(MovieConstants.TMDb.MJSN_OVERVIEW);
                poster_path = movie.getString(MovieConstants.TMDb.MJSN_POSTER_PATH);

                poster_path = "http://image.tmdb.org/t/p/w342/"+poster_path;

                backdrop_path = movie.getString(MovieConstants.TMDb.MJSN_BACKDROP_PATH);
                vote_count = movie.getString(MovieConstants.TMDb.MJSN_VOTE_COUNT);

                vote_average = movie.getString(MovieConstants.TMDb.MJSN_VOTE_RATE);

                release_date = movie.getString(MovieConstants.TMDb.MJSN_RELEASE_DATE);


                HashMap<String, String> movie_details = new HashMap<>();

                // Store Each detail as a Hashmap and insert that into a ArrayList

                movie_details.put(MovieConstants.MovieHashMapKeys.M_TITLE,movie_title);
                movie_details.put(MovieConstants.MovieHashMapKeys.M_DESCRIPTION,description);
                movie_details.put(MovieConstants.MovieHashMapKeys.M_POSTER_PATH,poster_path);
                movie_details.put(MovieConstants.MovieHashMapKeys.M_BACKDROP_PATH,backdrop_path);
                movie_details.put(MovieConstants.MovieHashMapKeys.M_AVG_VOTES, vote_average);
                movie_details.put(MovieConstants.MovieHashMapKeys.M_VOTES_COUNT,vote_count);
                movie_details.put(MovieConstants.MovieHashMapKeys.M_RELEASE_DATE,release_date);



                // Adding to ArrayList
                moviesList.add(movie_details);

                //String Array of Poster
                imagePosters[i] = poster_path;



            }

            return imagePosters;


        }


        @Override
        protected String[] doInBackground(String... params) {


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

            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            progress.dismiss();
            if (result != null) {


                postersArrayList = new ArrayList<String>(Arrays.asList(result));

                mAdapter = new ImageAdapter(getBaseContext(),result);
                mGridView.setAdapter(mAdapter);


            }else
            {
                Toast.makeText(getBaseContext(),"Error Fetching Movie",Toast.LENGTH_LONG).show();
            }

        }
    }


}
