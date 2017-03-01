package com.zr.addressselector.util;

import java.util.ArrayList;
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

    /**
     * 深拷贝list
     * @param src
     * @param dest
     */
    public static void copy(List src,List dest) {
        for (int i = 0; i < src.size(); i++){
            Object obj = src.get(i);
            if (obj instanceof List){
                dest.add(new ArrayList());
                copy((List) obj, (List) ((List) dest).get(i));
            }else{
                dest.add(obj);
            }
        }
    }

}
