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
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.logging.Log;


public class MovieDetail extends ActionBarActivity {

    String title,description,backdrop_path,vote_avg,total_votes,releasedate;

    ImageView backDropImage;
    TextView releaseDateTV,descriptionTxtView,titleTextView,RatingTextView;



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
    



}
