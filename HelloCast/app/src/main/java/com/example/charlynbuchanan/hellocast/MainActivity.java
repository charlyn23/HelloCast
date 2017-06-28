package com.example.charlynbuchanan.hellocast;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String urlString = "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/f.json";

    /* SessionManagerListener monitors sessions events (creation, suspension, resumption, termination)
    and automatically attempts to resume interrupted sessions. a Session ends when user stops casting or
    begins to cast something else to the same device
     */
    private CastSession castSession;
    private SessionManager sessionManager;
    private final SessionManagerListener sessionManagerListener = new SessionManagerListenerImpl();
    private class SessionManagerListenerImpl implements SessionManagerListener {
        @Override
        public void onSessionStarting(Session session) {}

        @Override
        public void onSessionStarted(Session session, String sessionId) {
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStartFailed(Session session, int i) {}

        @Override
        public void onSessionEnding(Session session) {}

        @Override
        public void onSessionResumed(Session session, boolean wasSuspended) {
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumeFailed(Session session, int i) {}

        @Override
        public void onSessionSuspended(Session session, int i) {}

        @Override
        public void onSessionEnded(Session session, int error) {
            finish();
        }
        @Override
        public void onSessionResuming(Session session, String s) {}
    }
    private static ArrayList<MediaItem> movies = new ArrayList<>();
    

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //castContext is a global singleton that coordinates the framework's interactions. It holds reference
        //to MediaRouter and starts/stops discovery process when app is in foreground/background, respectively
        //(see: CastOptionsProvider.class)
        CastContext castContext = CastContext.getSharedInstance(this);
        sessionManager = castContext.getSessionManager();
        CastOptionsProvider castOptionsProvider = new CastOptionsProvider();
        castOptionsProvider.getCastOptions(getApplicationContext());

        //This part is weird. it's like the listView ceases to exist by line 135
        final ListView listView = (ListView)findViewById(R.id.list);
        final VideoListAdapter adapter = new VideoListAdapter(MainActivity.this, R.layout.movie_row, new ArrayList<MediaItem>());

        getLoaderManager().initLoader(0, savedInstanceState, new LoaderManager.LoaderCallbacks<List<MediaItem>>() {
            @Override
            public Loader<List<MediaItem>> onCreateLoader(int i, Bundle bundle) {
                return new VideoItemLoader(getApplicationContext(), urlString);
            }

            @Override
            public void onLoadFinished(Loader<List<MediaItem>> loader, List<
                    MediaItem> mediaItem) {
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Ths is where the media player was. \nCast this video by selecting the \ncast icon in the upper right corner.", Toast.LENGTH_LONG).show();
                Log.d("ItemClick", "item clicked");

            }
        };
        listView.setOnItemClickListener(listClick);
        //This seems redundant and weird...







//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MediaItem current = movies.get(position);
//                MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
//                movieMetadata.putString("title", current.getTitle());
//                movieMetadata.addImage(new WebImage((Uri.parse(current.getImageUrl()))));
//                movieMetadata.putString("videoUrl", current.getVideoUrl());
//                movieMetadata.putInt("duration", current.getDuration());
//
////                castSession = sessionManager.getCurrentCastSession();
//
//                    Toast.makeText(getApplicationContext(), "Ths is where the media player was. \nCast this video by selecting the \ncast icon in the upper right corner.", Toast.LENGTH_LONG).show();
////                    showExpandedController();
//                    MediaInfo mediaInfo = new MediaInfo.Builder(current.getVideoUrl())
//                            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
//                            .setMetadata(movieMetadata)
//                            .setStreamDuration(current.getDuration() * 1000)
//                            .setContentType("videos/mp4")
//                            .build();
//                    RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
//                    remoteMediaClient.load(mediaInfo);
//
//            }
//        });
    }

    //cast button lives in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        return true;
    }

    public void showExpandedController() {
        Intent intent = new Intent(this, ExpandedControllerActivity.class);
        startActivity(intent);
    }

}
