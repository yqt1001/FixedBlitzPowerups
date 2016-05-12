package mc.yqt.fixedpowerups.utils;

import mc.yqt.fixedpowerups.FixedPowerups;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflect {


    public static Object getPrivateField(String name, Class<?> clazz, Object o) {
        Field f;
        Object obj = null;

        try {
            f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            obj = f.get(o);
        } catch (Exception e) {
            e.printStackTrace();
            FixedPowerups.setNMSState(false);
        }

        return obj;
    }

    public static Method getPrivateMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
        Method m = null;

        try {
            m = clazz.getDeclaredMethod(name, paramTypes);
            m.setAccessible(true);
        } catch (Exception e) {
            FixedPowerups.setNMSState(false);
            e.printStackTrace();
        }

        return m;
    }
}
