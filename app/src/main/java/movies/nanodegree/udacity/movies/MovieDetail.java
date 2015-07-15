package movies.nanodegree.udacity.movies;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;




public class MovieDetail extends ActionBarActivity {

    String title,description,backdrop_path,vote_avg,total_votes,releasedate;

    ImageView backDropImage;
    TextView releaseDateTV,descriptionTxtView,titleTextView,RatingTextView;

    private static final String FORECAST_SHARE_HASHTAG = " #MoviesApp";


    ShareActionProvider mShareActionProvider;

    private final String LOG_TAG = Home.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        //Initilaising the TextView/ImageView

        backDropImage = (ImageView) findViewById(R.id.backdropImageView);
        releaseDateTV = (TextView) findViewById(R.id.releaseDateTextView);
        descriptionTxtView = (TextView) findViewById(R.id.plotTextView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        RatingTextView = (TextView) findViewById(R.id.RateTextView);

        Bundle intent = getIntent().getExtras();

        if (intent != null) {

            title = intent.getString("title");
            description = intent.getString("description");
            backdrop_path = intent.getString("backdrop_path");
            vote_avg = intent.getString("vote_avg");
            total_votes = intent.getString("total_votes");
            releasedate = intent.getString("releasedate");

            /*
             Backdrop size
             "w300",
              "w780",
              "w1280",
             */

            backdrop_path = "http://image.tmdb.org/t/p/w780/"+backdrop_path;


            //Setting the Values from Intent
            Picasso.with(getBaseContext()).load(backdrop_path).into(backDropImage);
            releaseDateTV.setText(releasedate);
            descriptionTxtView.setText(description);
            titleTextView.setText(title);
            RatingTextView.setText(vote_avg);

            //Setting Title/Rating as Sub
            actionBarSetup(title, vote_avg);


        }


    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup(String title,String rating) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            ab.setTitle(title);
            ab.setSubtitle(rating);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.movie_detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
// Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            Log.d(LOG_TAG, "Share Action Provider is null");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out "+title + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
    



}
