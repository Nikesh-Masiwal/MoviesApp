package movies.nanodegree.udacity.movies.parcel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nikesh-MBP on 23/08/15.
 */


public class TMDbMovie implements Parcelable {

    public String mid;
    public String mTitle;
    public String mDescription;
    public String mPoster;
    public String rate;
    public String releaseDate;
    public String vote_count;


    protected TMDbMovie() {

        initializeValue();

    }

    public TMDbMovie(String id, String poster, String title, String date, String rate, String synopsis,
                 String duration) {
        this.mid = id;
        this.mPoster = poster;
        this.mTitle = title;
        this.releaseDate = date;
        this.rate = rate;
        this.mDescription = synopsis;
        this.vote_count = duration;
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

    public String getPosterW185() {
        return "http://image.tmdb.org/t/p/w185/" + mPoster;
    }

    public String getPosterW342() {
        return "http://image.tmdb.org/t/p/w342/" + mPoster;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        try {
            return releaseDate.substring(0, 4);
        } catch (Exception e) {
            return "Unknown";
        }
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
