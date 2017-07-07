package com.example.charlynbuchanan.hellocast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity  {

    private static String urlString = "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/f.json";
    public static final String BASE_URL = "https://commondatastorage.googleapis.com/";
    public static ResponseBody rawJSON;
    public static String retrofitJson;
    public static SharedPreferences jsonData;
    public  CastContext castContext;


    /* SessionManagerListener monitors sessions events (creation, suspension, resumption, termination)
    and automatically attempts to resume interrupted sessions. a Session ends when user stops casting or
    begins to cast something else to the same device
     */
    public  CastSession castSession;
    private SessionManager sessionManager;
    private  MSessionManagerListener sessionManagerListener;
    private Retrofit retrofit;
    public static RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private  VideoListAdapter adapter;
    private static ArrayList<MediaItem> movies = new ArrayList<>();

    private class MSessionManagerListener extends SimpleSessionManagerListener {

        @Override
        public void onSessionStarted(Session session, String s) {
            super.onSessionStarted(session, s);
        }

        @Override
        public void onSessionEnded(Session session, int i) {
            super.onSessionEnded(session, i);
        }

        @Override
        public void onSessionResumed(Session session, boolean b) {
            super.onSessionResumed(session, b);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManagerListener = new MSessionManagerListener();
        castContext = CastContext.getSharedInstance(getApplicationContext());
        sessionManager = castContext.getSessionManager();

        Thread fetchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = VideoFetcher.fetchData();
                    try {
                        movies = (ArrayList<MediaItem>) VideoFetcher.buildMedia(json);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new VideoListAdapter(getApplicationContext(), R.layout.movie_row, movies);
                                layoutManager = new LinearLayoutManager(MainActivity.this);
                                recyclerView = (RecyclerView) findViewById(R.id.list);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        });
        fetchThread.start();

    }



    //cast button lives in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        return true;
    }

    @Override
    protected void onResume() {

        sessionManager.addSessionManagerListener(sessionManagerListener);
        if (castSession != null) {
            castSession = sessionManager.getCurrentCastSession();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sessionManager.removeSessionManagerListener(sessionManagerListener);
        castSession = null;
    }

    public void showExpandedController() {
        Intent intent = new Intent(this, ExpandedControllerActivity.class);
        startActivity(intent);
    }
}
