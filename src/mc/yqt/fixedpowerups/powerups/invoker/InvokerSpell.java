package mc.yqt.fixedpowerups.powerups.invoker;

import java.util.HashMap;

public abstract class InvokerSpell {

	public abstract String getName();
	public abstract String[] getLore();
	
	public abstract void spell();
	
	/* static methods to manage spells */
	private static HashMap<String, InvokerSpell> spells = new HashMap<String, InvokerSpell>();
	static {
		
	}
	
	/**
	 * Returns a specified spell from name
	 * @param Spell name
	 * @return
	 */
	public static InvokerSpell getSpell(String spell) {
		if(spells.containsKey(spell))
			return spells.get(spell);
		
		return null;
	}
}
