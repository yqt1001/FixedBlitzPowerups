package mc.yqt.fixedpowerups.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import mc.yqt.fixedpowerups.FixedPowerups;

public class ProtocolListeners {

	//EID of wither used in witherwarrior
	public static int interceptWitherDetachPacket = 0;
	
	private static ProtocolManager PM;
	
	/**
	 * Create protocollib listeners
	 */
	public static void onEnable() {
		PM = ProtocolLibrary.getProtocolManager();
		
		//Listener for interceptWitherDetachPacket
		PM.addPacketListener(new PacketAdapter(FixedPowerups.getThis(), PacketType.Play.Server.ATTACH_ENTITY) {
			@Override
			public void onPacketSending(PacketEvent e) 
			{
				if(interceptWitherDetachPacket == e.getPacket().getIntegers().read(0) && e.getPacket().getIntegers().read(1) == -1) {
					e.setCancelled(true);
					interceptWitherDetachPacket = 0;
				}
			}
		});
	}
}
