package movies.nanodegree.udacity.movies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import movies.nanodegree.udacity.movies.R;
import movies.nanodegree.udacity.movies.parcel.MovieVideoDetails;
import movies.nanodegree.udacity.movies.parcel.Reviews;

/**
 * Created by Nikesh-MBP on 10/11/15.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{


    private ArrayList<Reviews> mValues;

    public static final String LOG_TAG = ReviewsAdapter.class.getSimpleName();

    Context ctx;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;

        public final TextView user_name,user_comments;


        public ViewHolder(View view) {
            super(view);

            mView = view;
            user_name = (TextView) view.findViewById(R.id.user_name);
            user_comments  = (TextView) view.findViewById(R.id.user_comments);




        }


    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public ReviewsAdapter(Context context, ArrayList<Reviews> items) {

        this.ctx = context;
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comments_layout, parent, false);

        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.user_name.setText(mValues.get(position).getReviewAuthor());
        holder.user_comments.setText(mValues.get(position).getReviewContent());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


}
