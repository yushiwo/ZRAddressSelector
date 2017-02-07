package com.zr.addressselector.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zr on 2017/2/5.
 */

public class ToastUtils {
    /** Show long Toast.
     *
     *  @see Toast#makeText(Context, CharSequence, int)
     *  @see Toast#LENGTH_LONG
     */
    public static void showLong(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /** Show short Toast.
     *
     *  @see Toast#makeText(Context, CharSequence, int)
     *  @see Toast#LENGTH_SHORT
     */
    public static void showShort(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
