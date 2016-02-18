package mc.yqt.fixedpowerups.powerups.invoker;

import java.util.List;

import mc.yqt.fixedpowerups.FixedPowerups;

public abstract class InvokerSpell {

	private String name;
	private List<String> lore;
	
	public abstract void spell();
	
	public InvokerSpell(FixedPowerups main, String s1) {
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
}
