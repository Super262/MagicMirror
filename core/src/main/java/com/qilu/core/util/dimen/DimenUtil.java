package com.qilu.core.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.qilu.core.app.Qilu;

public class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = Qilu.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        //取得屏幕的宽
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = Qilu.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        //取得屏幕的高
        return dm.heightPixels;
    }
}
