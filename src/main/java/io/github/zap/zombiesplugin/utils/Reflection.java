package io.github.zap.zombiesplugin.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Reflection utils added by SkylineX
 */
public class Reflection {

    public static Class getNMSClass(String str) {
        return getClass("net.minecraft.server." + getVersion() + "." + str);
    }

    public static Class getCraftClass(String str) {
        return getClass("org.bukkit.craftbukkit." + getVersion() + "." + str);
    }

    public static String getVersion() {
        /** get version of the server, as a string.*/
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }


    public static String getRawVersion() {
        /** get version of the server*/
        String str = Bukkit.getServer().getClass().getPackage().getName();
        return str.substring(str.lastIndexOf(".") + 1);
    }

    public static String getNMSVersion() {
        return "net.minecraft.server." + getRawVersion() + ".";
    }

    public static Class wrapperToPrimitive(Class var0) {
        return var0 == Boolean.class?Boolean.TYPE:(var0 == Integer.class?Integer.TYPE:(var0 == Double.class?Double.TYPE:(var0 == Float.class?Float.TYPE:(var0 == Long.class?Long.TYPE:(var0 == Short.class?Short.TYPE:(var0 == Byte.class?Byte.TYPE:(var0 == Void.class?Void.TYPE:(var0 == Character.class?Character.TYPE:var0))))))));
    }

    public static Class[] toParamTypes(Object ... var0) {
        Class[] var1 = new Class[var0.length];

        for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = wrapperToPrimitive(var0[var2].getClass());
        }

        return var1;
    }

    public static Enum getEnum(String var0) {
        String[] var1 = var0.split("\\.(?=[^\\.]+$)");
        if(var1.length == 2) {
            String var2 = var1[0];
            String var3 = var1[1];
            Class var4 = getClass(var2);
            return Enum.valueOf(var4, var3);
        } else {
            return null;
        }
    }

    public static Class getClass(String var0) {
        try {
            return Class.forName(var0);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static void setValue(Object var0, String var1, Object var2) {
        try {
            Field var3 = var0.getClass().getDeclaredField(var1);
            var3.setAccessible(true);
            var3.set(var0, var2);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static Object getValue(Object var0, String var1) {
        try {
            Field var2 = var0.getClass().getDeclaredField(var1);
            if(!var2.isAccessible()) {
                var2.setAccessible(true);
            }

            return var2.get(var0);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Object callMethod(Object var0, String var1, Object ... var2) {
        try {
            Class var3 = var0.getClass();
            Method var4 = var3.getDeclaredMethod(var1, toParamTypes(var2));
            var4.setAccessible(true);
            return var4.invoke(var0, var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(Object var0, String var1, Class ... var2) {
        try {
            Method var3 = var0.getClass().getMethod(var1, var2);
            if(!var3.isAccessible()) {
                var3.setAccessible(true);
            }

            return var3;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static Field getField(Field var0) {
        var0.setAccessible(true);
        return var0;
    }
}