package com.linheimx.app.library.dataprovider;


import com.linheimx.app.library.utils.Utils;

/**
 * Created by Administrator on 2016/12/5.
 */

public class YAxis extends Axis {

    public static final float AREA_UNIT = 8;// unit 区域的高
    public static final float AREA_LABEL = 22;// label 区域的高


    public YAxis() {
        super();

        labelArea = Utils.dp2px(AREA_LABEL);
        unitArea = Utils.dp2px(AREA_UNIT);
    }

}
