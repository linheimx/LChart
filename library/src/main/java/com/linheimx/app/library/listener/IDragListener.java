package com.linheimx.app.library.listener;

/**
 * Created by LJIAN on 2017/4/5.
 */

public interface IDragListener {
    /**
     * @param xMin 当前图谱中可见的x轴上的最小值
     * @param xMax 当前图谱中可见的x轴上的最大值
     */
    void onDrag(double xMin, double xMax);
}
