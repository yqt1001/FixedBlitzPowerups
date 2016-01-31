package mc.yqt.fixedpowerups.powerups.invoker;

import java.util.LinkedList;

public abstract class InvokerSpell {

	public abstract String getName();
	public abstract String[] getLore();
	
	public abstract void spell();
	
	/* static methods to manage spells */
	private static LinkedList<InvokerSpell> spells;
	
	public static InvokerSpell getSpell(String spell) {
		for(InvokerSpell p : spells)
			if(p.getName().equals(spell))
				return p;
		
		return null;
	}
}
