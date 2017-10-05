package net.crescenthikari.bakingapp.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Muhammad Fiqri Muthohar on 10/2/17.
 */

public class ViewUtils {
    public static void changeHeightAndWidthToMatchParent(View view) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(layoutParams);
        }
    }
}
