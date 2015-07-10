package movies.nanodegree.udacity.movies.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import movies.nanodegree.udacity.movies.R;

/**
 * Created by Nikesh-MBP on 05/07/15.
 */
public class ImageAdapter extends BaseAdapter {

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    private Context context;
    String[] movie;

    public ImageAdapter(Context context, String[] mobileValues) {
        this.context = context;


        this.movie = new String[mobileValues.length];
        movie = mobileValues;
    }

    @Override
    public boolean isEmpty() {

        return super.isEmpty();
    }

    public View getView(int position, View convertView, ViewGroup parent) {




        View gridView;

        if (convertView == null) {



            gridView = new View(context);

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.list_item_gridview, null);

            // set value into textview
//            TextView textView = (TextView) gridView
//                    .findViewById(R.id.grid_item_label);
//            textView.setText(moviesValue[position]);

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.imageview_item);

            String mobile = movie[position];

            String url = (String) getItem(position);

            Picasso.with(context).load(url).into(imageView);

            return gridView;

        } else {

            gridView = (View) convertView;

            return gridView;
        }


    }

    @Override
    public int getCount() {

        return movie.length;
    }

    @Override
    public Object getItem(int position) {
        return movie[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
