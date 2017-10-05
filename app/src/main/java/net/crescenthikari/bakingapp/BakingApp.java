package net.crescenthikari.bakingapp;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;

import com.squareup.picasso.Picasso;

import net.crescenthikari.bakingapp.di.component.DaggerApplicationComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;

/**
 * Created by Muhammad Fiqri Muthohar on 9/16/17.
 */

public class BakingApp extends Application
        implements HasActivityInjector, HasBroadcastReceiverInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidActivityInjector;

    @Inject
    DispatchingAndroidInjector<BroadcastReceiver> dispatchingAndroidBroadcastInjector;

    @Inject
    Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
        setupPicassoGlobalConfig();
    }

    private void setupPicassoGlobalConfig() {
        // set the global instance to use this Picasso object
        // all following Picasso (with Picasso.with(Context context) requests will use this Picasso object
        // you can only use the setSingletonInstance() method once!
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException ignored) {
            // Picasso instance was already set
            // cannot set it after Picasso.with(Context) was already in use
        }
    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidActivityInjector;
    }

    @Override
    public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return dispatchingAndroidBroadcastInjector;
    }
}
