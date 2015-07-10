package movies.nanodegree.udacity.movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

import movies.nanodegree.udacity.movies.adapter.ImageAdapter;


public class Home extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private final String LOG_TAG = Home.class.getSimpleName();

    private GridView mGridView;
    private ImageAdapter mAdapter;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //Defining ArrayAdapter
        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setOnItemClickListener(this);



        FetchMoviesPoster fetchMoviesPoster = new FetchMoviesPoster();
        progress = ProgressDialog.show(this, "Fetching Movies",
                "Grab a Popcorn, We are fetching Movies for you.", true);
        fetchMoviesPoster.execute();



//        FetchMoviesPoster fetchMoviesPoster = new FetchMoviesPoster();
//        fetchMoviesPoster.execute();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getBaseContext(),"Position "+position,Toast.LENGTH_SHORT).show();
    }

    public class FetchMoviesPoster extends AsyncTask<Void,String[],String[]>{

        private final String LOG_TAG = FetchMoviesPoster.class.getSimpleName();
        private final String API_KEY_MOVIEDB = "59f4a7727c4d04f1e3138fd764cbe60e";


        private String[] getMoviesDataFromJson(String movieJson)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String MJSN_RESULT = "results";
            final String MJSN_ORGTITLE = "original_title";
            final String MJSN_OVERVIEW = "overview";
            final String MJSN_TITLE = "title";
            final String MJSN_POSTER_PATH = "poster_path";
            final String MJSN_VOTE_RATE = "vote_average";
            final String MJSN_RELEASE_DATE = "release_date";
            final String MJSN_ACKDROP_PATH = "backdrop_path";

            JSONObject moviesJsonResult = new JSONObject(movieJson);
            JSONArray moviesArray = moviesJsonResult.getJSONArray(MJSN_RESULT);
            String[] imagePosters = new String[moviesArray.length()];
            for(int i = 0; i < moviesArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String movie_title;
                String description;
                String poster_path;
                String ratings;

                // Get the JSON object representing the day
                JSONObject movie = moviesArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                movie_title = movie.getString(MJSN_TITLE);
                description = movie.getString(MJSN_OVERVIEW);
                poster_path = movie.getString(MJSN_POSTER_PATH);

                poster_path = "http://image.tmdb.org/t/p/w342/"+poster_path;

                imagePosters[i] = poster_path;



            }

            return imagePosters;


        }


        @Override
        protected String[] doInBackground(Void... params) {


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            //Define constants

            //Construct Base URL and URI
            //final String FORECAST_BASE_URL = "http://private-795ef-themoviedb.apiary-mock.com/3/discover/movie?";

            final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

            final String API_PARAM = "api_key";
            final String PAGE_PARAM = "page";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(API_PARAM, API_KEY_MOVIEDB)
                    .appendQueryParameter(PAGE_PARAM, "1")
                    .build();
            Log.v(LOG_TAG, "Url " + builtUri);


            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // Create the request to OpenWeatherMap, and open the connection
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


                mAdapter = new ImageAdapter(getBaseContext(),result);
                mGridView.setAdapter(mAdapter);


            }else
            {
                Toast.makeText(getBaseContext(),"Error Fetching Movie",Toast.LENGTH_LONG).show();
            }

        }
    }


}
