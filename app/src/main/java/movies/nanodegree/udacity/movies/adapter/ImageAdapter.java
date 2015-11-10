package movies.nanodegree.udacity.movies.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import movies.nanodegree.udacity.movies.MovieConstants;
import movies.nanodegree.udacity.movies.R;
import movies.nanodegree.udacity.movies.parcel.TMDbMovie;

/**
 * Created by Nikesh-MBP on 05/07/15.
 */
public class ImageAdapter extends ArrayAdapter<TMDbMovie> {
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public TMDbMovie getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(TMDbMovie item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    public ImageAdapter(Context context, ArrayList<TMDbMovie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_item_gridview, parent, false);

        }
        Typeface custom_font_header,custom_font_body;

        custom_font_header = Typeface.createFromAsset(convertView.getContext().getAssets(), MovieConstants.TMDb.FONT_BOLD_PATH);


        TMDbMovie movie = getItem(position);
        ImageView poster = (ImageView)convertView.findViewById(R.id.imageview_item);
        TextView movieTitle = (TextView) convertView.findViewById(R.id.movie_title);
        TextView movie_rating = (TextView) convertView.findViewById(R.id.movie_rating);

        Picasso.with(getContext()).load(movie.getPosterW342()).into(poster);
        movieTitle.setText(movie.getTitle());
        movieTitle.setTypeface(custom_font_header);

        movie_rating.setText(movie.getRateForRatingBar());
        movie_rating.setTypeface(custom_font_header);

        return convertView;

    }

    //    public ImageAdapter(Context context, String[] mobileValues) {
//        this.context = context;
//
//
//        this.movie = new String[mobileValues.length];
//        movie = mobileValues;
//    }


 /*   public View getView(int position, View convertView, ViewGroup parent) {




        View gridView;

        if (convertView == null) {



            gridView = new View(context);

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.list_item_gridview, null);


        } else {
            gridView = new View(context);
            gridView = (View) convertView;


        }

        // set image based on selected text
        ImageView imageView = (ImageView) gridView
                .findViewById(R.id.imageview_item);

        String mobile = movie[position];

        String url = (String) getItem(position);

        Picasso.with(context).load(url).into(imageView);

        return gridView;


    } */




}
