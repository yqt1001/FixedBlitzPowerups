package mc.yqt.fixedpowerups.powerups.invoker;

import java.util.HashMap;
import java.util.List;

public abstract class InvokerSpell {

	private String name;
	private List<String> lore;
	
	public abstract void spell();
	
	public InvokerSpell(String s1) {
		this.name = s1;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	/* Static methods to manage spells */
	private static HashMap<String, InvokerSpell> spells = new HashMap<String, InvokerSpell>();
	static {
		
	}
}
