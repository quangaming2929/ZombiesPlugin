package io.github.zap.zombiesplugin.utils;

import java.util.Collection;

public class CollectionUtils {
    public static boolean referenceContains(Object[] array, Object element) {
        for(Object object : array) {
            if(array == element) return true;
        }
        return false;
    }

    public static boolean referenceContains(Collection<?> collection, Object element) {
        for(Object object : collection) {
            if(object == element) return true;
        }
        return false;
    }
}
