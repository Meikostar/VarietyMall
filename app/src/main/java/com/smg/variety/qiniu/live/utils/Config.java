package com.smg.variety.qiniu.live.utils;

import android.content.pm.ActivityInfo;

public class Config {
    public static final boolean DEBUG_MODE = false;
    public static final boolean FILTER_ENABLED = false;
    public static final int SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    /**文章 单图显示*/
    public static final int     TYPE_SIMPLE      = 1;
    /**文章 多图显示*/
    public static final int TYPE_MULTIPLE = 2;
    /**文章 带视频*/
    public static final int TYPE_VIDEO    = 3;
    /**二维码扫描*/
    public static final String HINT_ENCODING_ORIENTATION_CHANGED =
            "Encoding orientation had been changed. Stop streaming first and restart streaming will take effect";
}
