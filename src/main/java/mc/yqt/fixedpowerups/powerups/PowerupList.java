package mc.yqt.fixedpowerups.powerups;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import mc.yqt.fixedpowerups.FixedPowerups;

public class PowerupList implements Iterable<PowerupWrapper> {

	private LinkedList<PowerupWrapper> powerups = new LinkedList<>();
	private FixedPowerups main;
	
	public PowerupList(FixedPowerups main) {
		this.main = main;
		generate();
	}
	
	public void add(PowerupWrapper wrapper) {
		powerups.add(wrapper);
	}
	
	public Collection<PowerupWrapper> all() {
		return powerups;
	}
	
	public Collection<PowerupWrapper> type(PowerupType type) {
		return powerups.stream().filter(p -> p.type() == type).collect(Collectors.toList());
	}
	
	public PowerupWrapper get(String name) {
		return powerups.stream().filter(p -> p.getName().equals(name)).findAny().orElse(null);
	}
	
	private void generate() {
		// find all classes annotated with @GeneratePowerup
    	Reflections ref = new Reflections("mc.yqt.fixedpowerups.powerups");
    	
    	LinkedList<Class<? extends Powerup>> classes = new LinkedList<>();
    	for(Class<?> clazz : ref.getTypesAnnotatedWith(GeneratePowerup.class))
    		try { classes.add((Class<? extends Powerup>) clazz); } catch(Exception e) { }
    	
    	System.out.println(classes.size() + " powerup classes found.");
    	
    	for(Class<? extends Powerup> clazz : classes)
    		wrap(clazz);
    	
    	System.out.println(powerups.size() + " powerups generated.");
	}
	
	private void wrap(Class<? extends Powerup> clazz) {
		try {
			PowerupWrapper wrapper = new PowerupWrapper(clazz);
			wrapper.setMain(main);
			powerups.add(wrapper);
		} catch(Exception e) {
			System.out.println("Powerup creation failed.");
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<PowerupWrapper> iterator() {
		return powerups.iterator();
	}
}
