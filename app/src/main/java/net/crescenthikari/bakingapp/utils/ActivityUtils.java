package net.crescenthikari.bakingapp.utils;

import android.app.Activity;
import android.view.View;

/**
 * Created by Muhammad Fiqri Muthohar on 9/28/17.
 */

public class ActivityUtils {
    private ActivityUtils() {
    }

    public static void hideSystemUI(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
                    );
        }
    }
}
