package net.crescenthikari.bakingapp.di.module;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import net.crescenthikari.bakingapp.di.AppContext;
import net.crescenthikari.bakingapp.utils.VideoRequestHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Muhammad Fiqri Muthohar on 8/8/17.
 */
@Module
public class PicassoModule {
    @Provides
    @Singleton
    Picasso providePicasso(@AppContext Context context) {
        VideoRequestHandler videoRequestHandler = new VideoRequestHandler();
        return new Picasso.Builder(context)
                .addRequestHandler(videoRequestHandler)
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .memoryCache(new LruCache(100000000))
                .build();
    }
}
