package com.linheimx.app.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewConfiguration;

/**
 * Created by Administrator on 2016/11/13.
 */

public class Utils {
    private static DisplayMetrics mMetrics;

    public static void init(Context context) {

        Resources res = context.getResources();
        mMetrics = res.getDisplayMetrics();

    }

    public static float dp2px(float dp) {
        float px = dp * (mMetrics.densityDpi / 160f);
        return px;
    }

    public static float px2dp(float px) {
        float dp = px / (mMetrics.densityDpi / 160f);
        return dp;
    }


}
