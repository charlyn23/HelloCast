package com.example.charlynbuchanan.hellocast.cast;

import android.content.Context;
import android.text.format.DateUtils;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.MediaIntentReceiver;
import com.google.android.gms.cast.framework.media.NotificationOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlynbuchanan on 6/28/17.
 */

public class CastOptionsProvider implements OptionsProvider {
    public static final String CUSTOM_NAMESPACE = "urn:x-cast:com.google.cast.media";

    @Override
    public CastOptions getCastOptions(Context context) {

        //add media controls to notification and lock screen
        List<String> buttonActions = new ArrayList<>();
        buttonActions.add(MediaIntentReceiver.ACTION_REWIND);
        buttonActions.add(MediaIntentReceiver.ACTION_TOGGLE_PLAYBACK);
        buttonActions.add(MediaIntentReceiver.ACTION_FORWARD);
        buttonActions.add(MediaIntentReceiver.ACTION_STOP_CASTING);

        // Showing "play/pause" and "stop casting" in the compat view of the notification.
        int[] compatButtonActionsIndicies = new int[]{ 1, 3 };
        NotificationOptions notificationOptions = new NotificationOptions.Builder()
                .setTargetActivityClassName(ExpandedControlsActivity.class.getName())
                .setActions(buttonActions, compatButtonActionsIndicies)
                .setSkipStepMs(30 * DateUtils.SECOND_IN_MILLIS)
                .build();
        CastMediaOptions mediaOptions = new CastMediaOptions.Builder()
                .setNotificationOptions(notificationOptions)
                .setExpandedControllerActivityClassName(ExpandedControlsActivity.class.getName())
                .build();

        List<String> supportedNamespaces = new ArrayList<>();
//        supportedNamespaces.add(CUSTOM_NAMESPACE);

        //OptionProvider interface supplies the options we need to initialize CastContext; must be
        //declared in manifest
        return  new CastOptions.Builder()
                .setReceiverApplicationId("4F8B3483")
//                .setSupportedNamespaces(supportedNamespaces)
                .setCastMediaOptions(mediaOptions)
                .build();
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}
