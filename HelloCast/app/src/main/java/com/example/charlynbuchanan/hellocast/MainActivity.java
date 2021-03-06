package com.example.charlynbuchanan.hellocast;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity;
import com.google.android.gms.common.images.WebImage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity  {

    private static String urlString = "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/f.json";
    public static final String BASE_URL = "https://commondatastorage.googleapis.com/";
    public static ResponseBody rawJSON;
    public static String retrofitJson;
    public static SharedPreferences jsonData;


    /* SessionManagerListener monitors sessions events (creation, suspension, resumption, termination)
    and automatically attempts to resume interrupted sessions. a Session ends when user stops casting or
    begins to cast something else to the same device
     */
    private CastSession castSession;
    private SessionManager sessionManager;
    MSessionManagerListner sessionManagerListener;
    private Retrofit retrofit;

    private static ArrayList<MediaItem> movies = new ArrayList<>();

    private class MSessionManagerListner extends SimpleSessionManagerListener {

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

        jsonData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                // add other factories here, if needed.
//                .build();
//
//        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
//        Call<ResponseBody> result = apiInterface.getJSON();
//        result.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    retrofitJson = response.body().string();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });


        //castContext is a global singleton that coordinates the framework's interactions. It holds reference
        //to MediaRouter and starts/stops discovery process when app is in foreground/background, respectively
        //(see: CastOptionsProvider.class)
        CastContext castContext = CastContext.getSharedInstance(this);
        sessionManager = castContext.getSessionManager();
        CastOptionsProvider castOptionsProvider = new CastOptionsProvider();
        castOptionsProvider.getCastOptions(getApplicationContext());

        sessionManagerListener = new MSessionManagerListner();


        final ListView listView = (ListView)findViewById(R.id.list);
        final VideoListAdapter adapter = new VideoListAdapter(MainActivity.this, R.layout.movie_row, new ArrayList<MediaItem>());

        getLoaderManager().initLoader(0, savedInstanceState, new LoaderManager.LoaderCallbacks<List<MediaItem>>() {
            @Override
            public Loader<List<MediaItem>> onCreateLoader(int i, Bundle bundle) {

                return new VideoItemLoader(getApplicationContext(), retrofitJson);
            }

            @Override
            public void onLoadFinished(Loader<List<MediaItem>> loader, List<MediaItem> mediaItem) {
                movies.addAll(mediaItem);
                adapter.setData(movies);

            }

            @Override
            public void onLoaderReset(Loader<List<MediaItem>> loader) {

            }
        });
        listView.setAdapter(adapter);

        AdapterView.OnItemClickListener listClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MediaItem current = movies.get(position);
                MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
                movieMetadata.putString("title", current.getTitle());
                movieMetadata.addImage(new WebImage((Uri.parse(current.getImageUrl()))));
                movieMetadata.putString("videoUrl", current.getVideoUrl());
                movieMetadata.putInt("duration", current.getDuration());

                castSession = sessionManager.getCurrentCastSession();
                if (castSession != null) {
                    MediaInfo mediaInfo = new MediaInfo.Builder(current.getVideoUrl())
                            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                            .setMetadata(movieMetadata)
                            .setStreamDuration(current.getDuration() * 1000)
                            .setContentType("videos/mp4")
                            .build();
                    RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
                    remoteMediaClient.load(mediaInfo);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Ths is where the media player was. \nCast this video by selecting the \ncast icon in the upper right corner.", Toast.LENGTH_LONG).show();
                }
            }
        };
        listView.setOnItemClickListener(listClick);




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
        castSession = sessionManager.getCurrentCastSession();
        sessionManager.addSessionManagerListener(sessionManagerListener);
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

    public static String getRetrofitJson() {

        return retrofitJson;
    }



}
