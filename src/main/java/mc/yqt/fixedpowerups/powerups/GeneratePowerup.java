package mc.yqt.fixedpowerups.powerups;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Blank annotation just to designate classes that are designed to be powerups.
 * 
 */
@Target(value = ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratePowerup {
	
}
