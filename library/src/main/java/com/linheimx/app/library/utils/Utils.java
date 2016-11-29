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


    public static float textWidth(Paint paint, String txt) {
        return paint.measureText(txt);
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

    /**
     * 将杂乱的数字变成里程碑。
     *
     * @param number
     * @return
     */
    public static float roundNumber2One(double number) {
        if (Double.isInfinite(number) ||
                Double.isNaN(number) ||
                number == 0.0)
            return 0;

        final float d = (float) Math.ceil((float) Math.log10(number < 0 ? -number : number));//有几位？
        final int pw = 1 - (int) d;
        final float magnitude = (float) Math.pow(10, pw);
        final long shifted = Math.round(number * magnitude);
        return shifted / magnitude;
    }


}
