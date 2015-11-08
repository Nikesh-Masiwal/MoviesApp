package movies.nanodegree.udacity.movies;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import movies.nanodegree.udacity.movies.fragments.*;
import movies.nanodegree.udacity.movies.fragments.MovieDetail;
import movies.nanodegree.udacity.movies.parcel.TMDbMovie;


public class Home extends ActionBarActivity implements movies.nanodegree.udacity.movies.fragments.Home.Callbacks {

    private final String LOG_TAG = Home.class.getSimpleName();
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            Toast.makeText(getBaseContext(),"Two Pane",Toast.LENGTH_SHORT).show();
            mTwoPane = true;





        }else{
            Toast.makeText(getBaseContext(),"False",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onItemSelected(ArrayList<TMDbMovie> movie,int position) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(MovieDetail.ARG_ITEM_ID, id);
//
//            Toast.makeText(getBaseContext(),"Two Pane True"+id,Toast.LENGTH_SHORT).show();
//            MovieDetail fragment = new MovieDetail();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.item_detail_container, fragment)
//                    .commit();

            // Create fragment and give it an argument specifying the article it should show
            MovieDetail newFragment = new MovieDetail();
            Bundle args = new Bundle();
            args.putParcelable(MovieDetail.ARG_ITEM_ID, movie.get(position));
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.item_detail_container, newFragment);
            transaction.addToBackStack(null);

// Commit the transaction
            transaction.commit();


        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Toast.makeText(getBaseContext(),"False , Detail"+movie.get(position).getTitle(),Toast.LENGTH_SHORT).show();
            Intent detailIntent = new Intent(this, movies.nanodegree.udacity.movies.MovieDetail.class);

            detailIntent.putExtra(MovieDetail.ARG_ITEM_ID,movie.get(position));


            startActivity(detailIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        super.onSaveInstanceState(outState);
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


}
