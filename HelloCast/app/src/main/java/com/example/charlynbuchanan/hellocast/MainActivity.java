package com.example.charlynbuchanan.hellocast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.Menu;

import com.example.charlynbuchanan.hellocast.api.ApiService;
import com.example.charlynbuchanan.hellocast.api.ApiUtils;
import com.example.charlynbuchanan.hellocast.model.ApiAnswerResponse;
import com.example.charlynbuchanan.hellocast.model.Category;
import com.example.charlynbuchanan.hellocast.model.Movie;
import com.example.charlynbuchanan.hellocast.model.Source;
import com.example.charlynbuchanan.hellocast.model.Video;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static String urlString = "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/f.json";
    public static final String BASE_URL = "https://commondatastorage.googleapis.com/";
    public static ResponseBody rawJSON;
    public static String jsonResponse;
    public static SharedPreferences jsonData;
    public CastContext castContext;
    public static ArrayList<Video> responseMovieObjects;
    public static String videoUrl;
    public static String imageUrl;
    public static int duration;
    public static String title;
    public static String imageUrlTail;
    public static String videoUrlTail;


    /* SessionManagerListener monitors sessions events (creation, suspension, resumption, termination)
    and automatically attempts to resume interrupted sessions. a Session ends when user stops casting or
    begins to cast something else to the same device
     */
    public CastSession castSession;
    private SessionManager sessionManager;
    private MSessionManagerListener sessionManagerListener;
    private Retrofit retrofit;
    public static RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private VideoListAdapter adapter;
    private static ArrayList<MediaItem> movies = new ArrayList<>();
    private static ApiService service;

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
                    for (int i = 0; i < categoryList.size(); i++){
                        imageUrl = categoryList.get(i).images;
                        videoUrl = categoryList.get(i).getHls();
                        Log.d("imgVid", imageUrl + " " + videoUrl);
                        List<Video> videos = categoryList.get(i).videos;
                        for (int j = 0; j < videos.size(); j++){
                            Video current = videos.get(i);
                            String title = current.title;
                            int duration  = current.duration;
                            String imageTail = current.image480x270;
                            
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiAnswerResponse> call, Throwable t) {
                Log.e("loadData", "error loading API" + t.getMessage());

            }
        });
        service.getVideoUrl().enqueue(new Callback<List<Source>>() {
            @Override
            public void onResponse(Call<List<Source>> call, Response<List<Source>> response) {
                List<Source> sources = response.body();
                Source correctVidFormatUrl = sources.get(0);
                videoUrlTail = correctVidFormatUrl.url;
                Log.d("vidTail", videoUrlTail);
            }

            @Override
            public void onFailure(Call<List<Source>> call, Throwable t) {

            }
        });

//        service.getJSON().enqueue(new Callback<ArrayList<ApiAnswerResponse>>() {
//            //imageUrl tail-end nested in videos
//            @Override
//            public void onResponse(Call<ArrayList<ApiAnswerResponse>> call, Response<ArrayList<ApiAnswerResponse>> response) {
//                if (response.isSuccessful()) {
//                    ArrayList<ApiAnswerResponse> responsesList = response.body();
//                    List<Category> categories = responsesList.get(0).getCategories();
//                    for (int i = 0; i < categories.size(); i++) {
//                        String videoBaseUrl = categories.get(i).getHls();
//                        String imageBaseUrl = categories.get(i).getImages();
//                        Log.d("urls", videoBaseUrl + " " + imageBaseUrl);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<ApiAnswerResponse>> call, Throwable t) {
//                Log.e("loadData", "error loading API");
//            }

//            @Override
//            public void onFailure(Call<ArrayList<Video>> call, Throwable t) {
//
//            }
//        });
//        //videoUrl tail-end nested in videos
//        service.getVideoUrl().enqueue(new Callback<Source>() {
//            @Override
//            public void onResponse(Call<Source> call, Response<Source> response) {
//                if (response.isSuccessful()) {
//                    Source source = response.body();
//                    videoUrlTail = source.url;                                                      //video url tail
//                    Log.d("videoUrlTail", videoUrlTail);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Source> call, Throwable t) {
//
//            }
////        });
//
//
//        });
//    }
    }
}
