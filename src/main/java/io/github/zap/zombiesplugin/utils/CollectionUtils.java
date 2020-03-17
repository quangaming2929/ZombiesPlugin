package io.github.zap.zombiesplugin.utils;

public class CollectionUtils {
    public static boolean ReferenceContains(Object[] array, Object element) {
        for(Object object : array) {
            if(array == element) return true;
        }
        return false;
    }
}
