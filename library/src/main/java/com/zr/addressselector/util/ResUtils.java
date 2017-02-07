package com.zr.addressselector.util;

import android.content.Context;

/**
 * Created by zr on 2017/2/5.
 */

public class ResUtils {
    public static int dp2px (Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue*scale + 0.5f);
    }
}
