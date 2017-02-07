package com.zr.addressselector.util;

import java.util.List;

/**
 * Created by zr on 2017/2/5.
 */

public class ListUtils {
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }
}
