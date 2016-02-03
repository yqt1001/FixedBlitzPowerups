package mc.yqt.fixedpowerups.powerups.invoker;

import java.util.HashMap;
import java.util.List;

public abstract class InvokerSpell {

	private String name;
	private List<String> lore;
	
	public abstract void spell();
	
	public InvokerSpell(String s1, List<String> s2) {
		this.name = s1;
		this.lore = s2;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	/* Static methods to manage spells */
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
