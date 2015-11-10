package movies.nanodegree.udacity.movies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import movies.nanodegree.udacity.movies.R;
import movies.nanodegree.udacity.movies.parcel.MovieVideoDetails;

/**
 * Created by Nikesh-MBP on 09/11/15.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{


    private ArrayList<MovieVideoDetails> mValues;

    public static final String LOG_TAG = VideoAdapter.class.getSimpleName();

    Context ctx;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;

        public final TextView video_name;

        public final ImageView screen;


        public ViewHolder(View view) {
            super(view);

            mView = view;




            video_name = (TextView) view.findViewById(R.id.video_name);
            screen = (ImageView) view.findViewById(R.id.imgUrl);


        }


    }




    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public VideoAdapter(Context context, ArrayList<MovieVideoDetails> items) {

        this.ctx = context;
        mValues = items;
        Log.v(LOG_TAG, "Initialized " + " VideoAdapter " + mValues.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_layout, parent, false);

        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        String imgScreenYoutube = "http://img.youtube.com/vi/";
        final String videoID = mValues.get(position).getVideo_key();

        String finalScreenYoutube = imgScreenYoutube+videoID+"/mqdefault.jpg";

        holder.video_name.setText(mValues.get(position).getName());

        Log.v(LOG_TAG, "ScreenUrl " + " VideoAdapter "+finalScreenYoutube);

        Picasso.with(this.ctx).load(finalScreenYoutube).into(holder.screen);

        Log.v(LOG_TAG, mValues.get(position).getName());

        Log.v(LOG_TAG, "BindView " + " VideoAdapter ");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               watchYoutubeVideo(v,videoID);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public static void watchYoutubeVideo(View v,String id){
        Context context = v.getContext();
        try{


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            context.startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v="+id));
            context.startActivity(intent);
        }
    }
}