package movies.nanodegree.udacity.movies.parcel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Nikesh-MBP on 23/08/15.
 */


public class TMDbMovie implements Parcelable {

    public String mid;
    public String mTitle;
    public String mDescription;
    public String mPoster;
    public String mBackdrop;
    public String rate;
    public String releaseDate;

    public String vote_count;


    protected TMDbMovie() {

        initializeValue();

    }

    public TMDbMovie(String id, String poster, String title, String date, String rate, String synopsis,
                 String vote_count,String backdrop) {
        this.mid = id;
        this.mPoster = poster;
        this.mTitle = title;
        this.releaseDate = date;
        this.rate = rate;
        this.mDescription = synopsis;
        this.vote_count = vote_count;
        this.mBackdrop = backdrop;

        Log.v("Parcelable", "Movies " + vote_count + " Backdrop" + mBackdrop);

    }

    public TMDbMovie(String id, String poster, String title, String rate) {
        this.mid = id;
        this.mPoster = poster;
        this.mTitle = title;
        this.rate = rate;
    }

    public String getId() {
        return mid;
    }

    public String getPosterW92() {
        return "http://image.tmdb.org/t/p/w92/" + mPoster;
    }

    public String getPosterW154() {
        return "http://image.tmdb.org/t/p/w154/" + mPoster;
    }
    public String getPosterW185() {
        return "http://image.tmdb.org/t/p/w185/" + mPoster;
    }

    public String getPosterW342() {
        return "http://image.tmdb.org/t/p/w342/" + mPoster;
    }

    public String getPosterW500() {
        return "http://image.tmdb.org/t/p/w500/" + mPoster;
    }

    public String getPosterW780() {
        return "http://image.tmdb.org/t/p/w780/" + mPoster;
    }

    public String getTitle() {
        return mTitle;
    }


    public String getBackdropW300() {
        return "http://image.tmdb.org/t/p/w300/" + mBackdrop;
    }

    public String getBackdropW780() {
        return "http://image.tmdb.org/t/p/w780/" + mBackdrop;
    }
    public String getBackdropW1280() {
        return "http://image.tmdb.org/t/p/w1280/" + mBackdrop;
    }

    public String getDate() {
        try {
            return releaseDate;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getmBackdrop() {
        return mBackdrop;
    }

    public void setmBackdrop(String mBackdrop) {
        this.mBackdrop = mBackdrop;
    }

    public String getRateForRatingBar() {
        return rate;
    }

    public String getRate() {
        return String.valueOf(rate) + "/10";
    }

    public String getSynopsis() {
        return mDescription;
    }

    public String getDuration() {
        return vote_count + " min";
    }

    public TMDbMovie(Parcel in) {
        this.mid = in.readString();
        this.mPoster = in.readString();
        this.mTitle = in.readString();
        this.releaseDate = in.readString();
        this.rate = in.readString();
        this.mDescription = in.readString();
        this.vote_count = in.readString();
        this.mBackdrop = in.readString();
    }

    public static final Creator<TMDbMovie> CREATOR = new Creator<TMDbMovie>() {
        @Override
        public TMDbMovie createFromParcel(Parcel in) {
            return new TMDbMovie(in);
        }

        @Override
        public TMDbMovie[] newArray(int size) {
            return new TMDbMovie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mid);
        dest.writeString(this.mPoster);
        dest.writeString(this.mTitle);
        dest.writeString(this.releaseDate);
        dest.writeString(this.rate);
        dest.writeString(this.mDescription);
        dest.writeString(this.vote_count);
        dest.writeString(this.mBackdrop);

    }

    private void initializeValue(){

        this.mid = "";
        this.mPoster = "";
        this.mTitle = "";
        this.releaseDate = "Unknown";
        this.rate = "";
        this.mDescription = "";
        this.vote_count = "0";

    }

}
