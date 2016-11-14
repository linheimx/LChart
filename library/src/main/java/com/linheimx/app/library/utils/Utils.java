package com.linheimx.app.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

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


    public static int textWidth(Paint paint, String demoText) {
        return (int) paint.measureText(demoText);
    }

    private static Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();

    public static int textHeightAsc(Paint paint) {
        paint.getFontMetrics(mFontMetrics);
        return (int) (Math.abs(mFontMetrics.ascent));
    }

    public static int textHeight(Paint paint) {
        paint.getFontMetrics(mFontMetrics);
        return (int) (Math.abs(mFontMetrics.ascent) + Math.abs(mFontMetrics.descent));
    }


}
