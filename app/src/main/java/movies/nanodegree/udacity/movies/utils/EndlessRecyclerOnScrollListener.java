package movies.nanodegree.udacity.movies.utils;

/**
 * Created by Nikesh on 11/5/15.
 */
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AbsListView;

public abstract class EndlessRecyclerOnScrollListener implements AbsListView.OnScrollListener {


    private static final String LOG_TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    private int visibleThreshold = 5;
    private int current_page = 1;
    private int previousTotal = 0;
    private boolean loading = true;



    public EndlessRecyclerOnScrollListener() {
    }
    public EndlessRecyclerOnScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            Log.d(LOG_TAG, "onLoadMore " + totalItemCount);
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                current_page++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            Log.d(LOG_TAG, "onLoadMore True" + totalItemCount);
            loading = true;


            onLoadMore(current_page);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }


    public abstract void onLoadMore(int current_page);
}
