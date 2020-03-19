package io.github.zap.zombiesplugin.utils;

import java.lang.reflect.Array;
import java.util.Collection;

public class CollectionUtils {
    public static boolean ReferenceContains(Object[] array, Object element) {
        for(Object object : array) {
            if(array == element) return true;
        }
        return false;
    }
}
