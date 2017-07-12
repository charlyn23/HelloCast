package com.example.charlynbuchanan.hellocast;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.charlynbuchanan.hellocast.api.ApiService;
import com.example.charlynbuchanan.hellocast.api.ApiUtils;
import com.example.charlynbuchanan.hellocast.cast.CastOptionsProvider;
import com.example.charlynbuchanan.hellocast.cast.SimpleSessionManagerListener;
import com.example.charlynbuchanan.hellocast.model.ApiAnswerResponse;
import com.example.charlynbuchanan.hellocast.model.Category;
import com.example.charlynbuchanan.hellocast.model.MediaItem;
import com.example.charlynbuchanan.hellocast.model.Source;
import com.example.charlynbuchanan.hellocast.model.Video;
import com.example.charlynbuchanan.hellocast.ui.CustomItemClickListener;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity;
import com.google.android.gms.common.images.WebImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CastContext castContext;
    private String videoUrl;
    private String imageUrl;
    private int duration;
    private String title;
    private String imageUrlTail;
    private String videoUrlTail;
    private String mimeType;
    private List<Video> videos;
    private CustomItemClickListener customItemClickListener;


    /* SessionManagerListener monitors sessions events (creation, suspension, resumption, termination)
    and automatically attempts to resume interrupted sessions. a Session ends when user stops casting or
    begins to cast something else to the same device
     */
    private CastSession castSession;
    private SessionManager sessionManager;
    private MSessionManagerListener sessionManagerListener;
    private VideoListAdapter adapter;
    private ArrayList<MediaItem> movies = new ArrayList<>();
    private ApiService service;

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
        service = ApiUtils.getApiService();

        try {
            loadResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    //Each Cast Media item requires a title, videoUrl, imageUrl and duration. The urls must be concatenated
    //from different parts of the json response, thus, multiple calls.
    public void loadResponse() throws IOException {

        service.getJSON().enqueue(new Callback<ApiAnswerResponse>() {
            @Override
            public void onResponse(Call<ApiAnswerResponse> call, Response<ApiAnswerResponse> response) {
                if (response.isSuccessful()) {
                    List<Category> categoryList = response.body().getCategories();
                    buildMedia(categoryList);
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<ApiAnswerResponse> call, Throwable t) {
                Log.e("loadData", "error loading API" + t.getMessage());
            }
        });
    }

    public void buildMedia(List<Category> categories){
        for (Category category : categories){

            imageUrl = category.images;
            videoUrl = category.hls;
            Log.d("imgVid", imageUrl + " " + videoUrl);
            videos = category.videos;
            for (Video video : videos) {
                imageUrlTail = video.image480x270;
                title = video.title;
                duration = video.duration;
                List<Source> sources = video.sources;
                videoUrlTail = sources.get(0).url;
                mimeType = sources.get(0).mime;
                Log.d("videoData", title + " " + imageUrl + imageUrlTail + " " + videoUrl + videoUrlTail + " " + duration);

                //With all of the required MediaItem data collected, we can build MediaItems for cast
                MediaItem mediaItem = new MediaItem(new MediaItem.MediaItemBuilder(title, videoUrl+videoUrlTail, imageUrl+imageUrlTail, duration, mimeType));
                movies.add(mediaItem);
            }
        }
    }

    public void updateUI(){
        adapter = new VideoListAdapter(getApplicationContext(), R.layout.movie_row, movies, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                MediaItem movie = movies.get(position);
                MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
                movieMetadata.putString("title", movie.getTitle());
                movieMetadata.addImage(new WebImage((Uri.parse(movie.getImageUrl()))));
                movieMetadata.putString("videoUrl", movie.getVideoUrl());
                movieMetadata.putInt("duration", movie.getDuration());

                final CastContext castContext = CastContext.getSharedInstance(getApplicationContext());
                SessionManager sessionManager = castContext.getSessionManager();
                CastOptionsProvider castOptionsProvider = new CastOptionsProvider();
                castOptionsProvider.getCastOptions(getApplicationContext());

                castSession = sessionManager.getCurrentCastSession();
                if (castSession != null) {
                    MediaInfo mediaInfo = new MediaInfo.Builder(movie.getVideoUrl())
                            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                            .setMetadata(movieMetadata)
                            .setStreamDuration(movie.getDuration() * 1000)
                            .setContentType("videos/mp4")
                            .build();
                    RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
                    remoteMediaClient.load(mediaInfo);
                }
                else {
                    Toast.makeText(getApplicationContext(), "This is where the media player would be.\nPress cast button for now", Toast.LENGTH_SHORT).show();
                }

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
