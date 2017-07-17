package com.example.charlynbuchanan.hellocast.playlist;

import android.content.Context;
import android.util.Log;

import com.example.charlynbuchanan.hellocast.cast.SimpleSessionManagerListener;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import retrofit2.http.Part;

/**
 * Created by charlynbuchanan on 7/13/17.
 */

public class QueueDataProvider {

    private Context context;
    private final List<MediaQueueItem> queue = new CopyOnWriteArrayList<>();
    private static QueueDataProvider queueDataProvider;
    private final Object mLock = new Object();
    private final SimpleSessionManagerListener sessionManagerListener = new SimpleSessionManagerListener();
    private OnQueueDataChangedListener listener;

    private class MSessionManagerListener extends SimpleSessionManagerListener{

        @Override
        public void onSessionStarted(Session session, String s) {
            super.onSessionStarted(session, s);
            syncWithRemoteQueue();
        }

        @Override
        public void onSessionResumed(Session session, boolean b) {
            super.onSessionResumed(session, b);
            syncWithRemoteQueue();
        }

        @Override
        public void onSessionEnded(Session session, int i) {
            super.onSessionEnded(session, i);
            clearQueue();
            if (listener != null) {
                listener.onQueueDataChanged();
            }
        }


    }
    private class MyRemoteMediaClientListener implements RemoteMediaClient.Listener {

        @Override
        public void onStatusUpdated() {

        }

        @Override
        public void onMetadataUpdated() {

        }

        @Override
        public void onQueueStatusUpdated() {
        }

        @Override
        public void onPreloadStatusUpdated() {
            RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
            if (remoteMediaClient == null) {
                return;
            }
            MediaStatus mediaStatus = remoteMediaClient.getMediaStatus();
            if (mediaStatus == null) {
                return;
            }
            upcomingItem = mediaStatus.getQueueItemById(mediaStatus.getPreloadedItemId());
            Log.d("preLoadStatUp", "onRemoteMediaPreloadStatusUpdated() with item=" + upcomingItem);
            if (listener != null) {
                listener.onQueueDataChanged();
            }
        }

        @Override
        public void onSendingRemoteMediaRequest() {

        }

        @Override
        public void onAdBreakStatusUpdated() {

        }
    }

    /**
     * Listener notifies the data of the queue has changed.
     */
    public interface OnQueueDataChangedListener {

        void onQueueDataChanged();
    }

    public void setOnQueueDataChangedListener(OnQueueDataChangedListener listener) {
        this.listener = listener;
    }
    private final RemoteMediaClient.Listener mRemoteMediaClientListener = new RemoteMediaClient.Listener() {
        @Override
        public void onStatusUpdated() {
            updateMediaQueue();
            if (listener != null) {
                listener.onQueueDataChanged();
            }
            Log.d("onStatusUpd", "Queue was updated");
        }

        @Override
        public void onMetadataUpdated() {

        }

        @Override
        public void onQueueStatusUpdated() {
            updateMediaQueue();
            if (listener != null) {
                listener.onQueueDataChanged();
            }
        }

        @Override
        public void onPreloadStatusUpdated() {

        }

        @Override
        public void onSendingRemoteMediaRequest() {

        }

        @Override
        public void onAdBreakStatusUpdated() {

        }
    };
    private int repeatMode;
    private boolean shuffle;
    private MediaQueueItem currentIem;
    private MediaQueueItem upcomingItem;
//    private OnQueueDataChangedListener mListener;
    private boolean mDetachedQueue = true;


    private QueueDataProvider(Context context) {
        context = context.getApplicationContext();
        CastContext.getSharedInstance(context).getSessionManager().addSessionManagerListener(sessionManagerListener);
        syncWithRemoteQueue();

    }

    private void syncWithRemoteQueue() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null) {
            remoteMediaClient.addListener(mRemoteMediaClientListener);
            MediaStatus mediaStatus = remoteMediaClient.getMediaStatus();
            if (mediaStatus != null) {
                List<MediaQueueItem> items = mediaStatus.getQueueItems();
                if (items != null && !items.isEmpty()) {
                    queue.clear();
                    queue.addAll(items);
                    repeatMode = mediaStatus.getQueueRepeatMode();
                    currentIem = mediaStatus.getQueueItemById(mediaStatus.getCurrentItemId());
                    mDetachedQueue = false;
                    upcomingItem = mediaStatus.getQueueItemById(mediaStatus.getPreloadedItemId());
                }
            }
        }
    }

    private RemoteMediaClient getRemoteMediaClient() {
        CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                .getCurrentCastSession();
        if (castSession == null || !castSession.isConnected()) {
            Log.w("getRemoteClient", "Trying to get a RemoteMediaClient when no CastSession is started.");
            return null;
        }
        return castSession.getRemoteMediaClient();
    }

    public void clearQueue() {
        queue.clear();
        mDetachedQueue = true;
        currentIem = null;
    }

    private void updateMediaQueue() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        MediaStatus mediaStatus;
        List<MediaQueueItem> queueItems = null;
        if (remoteMediaClient != null) {
            mediaStatus = remoteMediaClient.getMediaStatus();
            if (mediaStatus != null) {
                queueItems = mediaStatus.getQueueItems();
                repeatMode = mediaStatus.getQueueRepeatMode();
                currentIem = mediaStatus.getQueueItemById(mediaStatus.getCurrentItemId());
            }
        }
        queue.clear();
        if (queueItems == null) {
            Log.d("queuedItems", "Queue is cleared");
        } else {
            Log.d("queuedItems", "Queue is updated with a list of size: " + queueItems.size());
            if (queueItems.size() > 0) {
                queue.addAll(queueItems);
                mDetachedQueue = false;
            } else {
                mDetachedQueue = true;
            }
        }
    }


    public static synchronized QueueDataProvider getInstance(Context context) {
        if (queueDataProvider == null) {
            queueDataProvider = new QueueDataProvider(context);
        }
        return queueDataProvider;
    }
}


