package net.crescenthikari.bakingapp.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Muhammad Fiqri Muthohar on 9/20/17.
 */

public class VideoRequestHandler extends RequestHandler {
    private static final String TAG = "VideoRequestHandler";
    private static final String SCHEME_MP4 = "mp4";

    @Override
    public boolean canHandleRequest(Request data) {
        String filename = data.uri.getLastPathSegment();
        String scheme = "";
        try {
            scheme = filename.substring(filename.lastIndexOf('.') + 1);
        } catch (Exception e) {
            Log.e(TAG, "canHandleRequest: ", e);
        }
        return SCHEME_MP4.equalsIgnoreCase(scheme);
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(request.uri.toString(), new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return new Result(bitmap, Picasso.LoadedFrom.DISK);
    }
}
