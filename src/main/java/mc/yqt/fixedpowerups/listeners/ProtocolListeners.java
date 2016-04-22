package mc.yqt.fixedpowerups.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import mc.yqt.fixedpowerups.FixedPowerups;

public class ProtocolListeners {

    //EID of wither used in witherwarrior
    private int interceptWitherDetachPacket = 0;

    private ProtocolManager PM;
    private FixedPowerups main;

    public ProtocolListeners(FixedPowerups main) {
        this.main = main;
        this.PM = ProtocolLibrary.getProtocolManager();

        //Listener for interceptWitherDetachPacket
        PM.addPacketListener(new PacketAdapter(this.main, PacketType.Play.Server.ATTACH_ENTITY) {
            @Override
            public void onPacketSending(PacketEvent e) {
                if (interceptWitherDetachPacket == e.getPacket().getIntegers().read(0) && e.getPacket().getIntegers().read(1) == -1)
                    e.setCancelled(true);
            }
        });
    }

    /**
     * Sets the wither EID to intercept the dismount packets sent by the server
     *
     * @param eid
     */
    public void setWitherEID(int eid) {
        this.interceptWitherDetachPacket = eid;
    }
}
