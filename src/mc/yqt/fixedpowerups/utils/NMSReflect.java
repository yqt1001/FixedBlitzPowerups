package mc.yqt.fixedpowerups.utils;

import java.lang.reflect.Field;

import mc.yqt.fixedpowerups.FixedPowerups;

public class NMSReflect {

	
	public static Object getPrivateField(String name, Class< ?> clazz, Object o) {
		
		Field f = null;
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
}
