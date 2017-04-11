package com.linheimx.app.library.model;

import com.linheimx.app.library.utils.Utils;

/**
 * Created by lijian on 2016/12/5.
 */

public class XAxis extends Axis {

    public static final float AREA_UNIT = 14;// unit 区域的高
    public static final float AREA_LABEL = 14;// label 区域的高


    public XAxis() {
        super();

        labelDimen = Utils.dp2px(AREA_LABEL);
        unitDimen = Utils.dp2px(AREA_UNIT);
    }
}
