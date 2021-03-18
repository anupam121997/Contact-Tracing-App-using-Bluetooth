package com.example.projectinterface;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

//import com.example.projectinterface.FetchResults;
//import com.example.projectinterface.NotificationWorker;
//import com.example.projectinterface;
//import com.example.projectinterface.NotificationWorker.StateListAdapter;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import kotlinx.coroutines.InternalCoroutinesApi;



public class CovidUpdate extends AppCompatActivity {

    static StateListAdapter stateListAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @InternalCoroutinesApi
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_update);

        final ListView list = (ListView)findViewById(R.id.list);
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_header, list, false));


        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeToRefresh);
        final TextView lastUpdatedTv = (TextView)findViewById(R.id.lastUpdatedTv);

        final TextView confirmedTv = (TextView)findViewById(R.id.confirmedTv);
        final TextView activeTv = (TextView)findViewById(R.id.activeTv);
        final TextView recoveredTv = (TextView)findViewById(R.id.recoveredTv);
        final TextView deceasedTv = (TextView)findViewById(R.id.deceasedTv);

        FetchResults.fetchResults(swipeRefreshLayout, lastUpdatedTv, list, confirmedTv, activeTv, recoveredTv, deceasedTv);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchResults.fetchResults(swipeRefreshLayout, lastUpdatedTv, list, confirmedTv, activeTv, recoveredTv, deceasedTv);
            }
        });

        initWorker();
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) { }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(list.getChildAt(0) != null) {
                    swipeRefreshLayout.setEnabled(list.getFirstVisiblePosition() == 0 && list.getChildAt(0).getTop() == 0);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @InternalCoroutinesApi
    private void initWorker() {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        final PeriodicWorkRequest notificationWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork("JOB_TAG", ExistingPeriodicWorkPolicy.KEEP, notificationWorkRequest);
    }

    static String getTimeAgo(Date past) {
        final Date now = new Date();


        final long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        final long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());

        if (seconds < 60) {
            return "Few seconds ago";
        }
        else if (minutes < 60) {
            return minutes + " minutes ago";
        }
        else if (hours < 24) {
            return hours + " hour " + (minutes % 60) + " min ago";
        }
        else {
            return new SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString();
        }
    }
}


