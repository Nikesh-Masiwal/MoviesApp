package movies.nanodegree.udacity.movies;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.logging.Log;


public class MovieDetail extends ActionBarActivity {

    String title,description,backdrop_path,vote_avg,total_votes,releasedate;

    ImageView backDropImage;
    TextView releaseDateTV,descriptionTxtView,titleTextView,RatingTextView;

    RatingBar rate0;

    private final String LOG_TAG = Home.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(movies.nanodegree.udacity.movies.fragments.MovieDetail.ARG_ITEM_ID,
                    getIntent().getParcelableExtra(movies.nanodegree.udacity.movies.fragments.MovieDetail.ARG_ITEM_ID));
            movies.nanodegree.udacity.movies.fragments.MovieDetail fragment = new movies.nanodegree.udacity.movies.fragments.MovieDetail();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.title_frame, fragment)
                    .commit();
        }
        // Show the Up button in the action bar.
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup(String title,String rating) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            ab.setTitle(title);
            ab.setSubtitle(rating);
        }
    }
    



}
