package mc.yqt.fixedpowerups.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Title {

	/* Class to manage titles, since for some reason Spigot doesn't support them */

    private static final ProtocolManager PM = ProtocolLibrary.getProtocolManager();
    private String title;
    private String subtitle;
    private int fadeIn;
    private int fadeOut;
    private int time;

    /**
     * Simple constructor, without fade in and out timing
     *
     * @param Title
     *         string
     * @param Subtitle
     *         string
     * @param Display
     *         time
     */
    public Title(String title, String subtitle, int time) {
        this(title, subtitle, time, 0, 0);
    }

    /**
     * Full constructor with all values
     *
     * @param Title
     *         string
     * @param Subtitle
     *         string
     * @param Display
     *         time
     * @param Fade
     *         in time
     * @param Fade
     *         out time
     */
    public Title(String title, String subtitle, int time, int fadeIn, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.time = time;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
    }

    /**
     * Static title creator that doesn't allow for editing
     *
     * @param Title
     *         string
     * @param Subtitle
     *         string
     * @param Display
     *         time
     * @param Fade
     *         in time
     * @param Fade
     *         out time
     * @param Specific
     *         player that the packets are getting sent to
     */
    public static void createTitle(String title, String subtitle, int time, int fadeIn, int fadeOut, Player p) {
        createTitle(title, subtitle, time, fadeIn, fadeOut, new LinkedList<Player>(Collections.singletonList(p)));
    }

    /**
     * Static title creator that doesn't allow for editing
     *
     * @param Title
     *         string
     * @param Subtitle
     *         string
     * @param Display
     *         time
     * @param Fade
     *         in time
     * @param Fade
     *         out time
     * @param Collection
     *         of players that the packets are getting sent to
     */
    public static void createTitle(String title, String subtitle, int time, int fadeIn, int fadeOut, Collection<? extends Player> players) {
        //set title
        PacketContainer p1 = PM.createPacket(PacketType.Play.Server.TITLE);
        p1.getTitleActions().write(0, TitleAction.TITLE);
        p1.getChatComponents().write(0, WrappedChatComponent.fromText(title));

        //set subtitle
        PacketContainer p2 = PM.createPacket(PacketType.Play.Server.TITLE);
        p2.getTitleActions().write(0, TitleAction.SUBTITLE);
        p2.getChatComponents().write(0, WrappedChatComponent.fromText(subtitle));

        //set times
        PacketContainer p3 = PM.createPacket(PacketType.Play.Server.TITLE);
        p3.getTitleActions().write(0, TitleAction.TIMES);
        p3.getIntegers().write(0, fadeIn);
        p3.getIntegers().write(1, time);
        p3.getIntegers().write(2, fadeOut);

        //send packets
        for (Player p : players) {
            try {
                PM.sendServerPacket(p, p1);
                PM.sendServerPacket(p, p2);
                PM.sendServerPacket(p, p3);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update values, use null to keep them unchanged
     *
     * @param Title
     *         string
     * @param Subtitle
     *         string
     * @param Display
     *         time
     * @param Fade
     *         in time
     * @param Fade
     *         out time
     */
    public Title update(String title, String subtitle, Integer time, Integer fadeIn, Integer fadeOut) {
        if (title != null)
            this.title = title;
        if (subtitle != null)
            this.subtitle = subtitle;
        if (time != null)
            this.time = time.intValue();
        if (fadeIn != null)
            this.fadeIn = fadeIn.intValue();
        if (fadeOut != null)
            this.fadeOut = fadeOut.intValue();

        return this;
    }

    /**
     * Sends specified players the packets to create the title
     *
     * @param List
     *         of players
     */
    public void sendCreationPackets(Collection<? extends Player> players) {
        //set title
        PacketContainer p1 = PM.createPacket(PacketType.Play.Server.TITLE);
        p1.getTitleActions().write(0, TitleAction.TITLE);
        p1.getChatComponents().write(0, WrappedChatComponent.fromText(this.title));

        //set subtitle
        PacketContainer p2 = PM.createPacket(PacketType.Play.Server.TITLE);
        p2.getTitleActions().write(0, TitleAction.SUBTITLE);
        p2.getChatComponents().write(0, WrappedChatComponent.fromText(this.subtitle));

        //set times
        PacketContainer p3 = PM.createPacket(PacketType.Play.Server.TITLE);
        p3.getTitleActions().write(0, TitleAction.TIMES);
        p3.getIntegers().write(0, this.fadeIn);
        p3.getIntegers().write(1, this.time);
        p3.getIntegers().write(2, this.fadeOut);

        //send packets
        for (Player p : players) {
            try {
                PM.sendServerPacket(p, p1);
                PM.sendServerPacket(p, p2);
                PM.sendServerPacket(p, p3);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the specified player the packets to create the title
     *
     * @param Player
     */
    public void sendCreationPackets(Player p) {
        this.sendCreationPackets(new LinkedList<Player>(Arrays.asList(p)));
    }

    /**
     * Sends specified players an updated title packet
     *
     * @param List
     *         of players
     * @return
     */
    public Title sendTitlePacket(Collection<? extends Player> players) {
        //set title
        PacketContainer p1 = PM.createPacket(PacketType.Play.Server.TITLE);
        p1.getTitleActions().write(0, TitleAction.TITLE);
        p1.getChatComponents().write(0, WrappedChatComponent.fromText(this.title));

        for (Player p : players) {
            try {
                PM.sendServerPacket(p, p1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    /**
     * Sends the specified player an updated title packet
     *
     * @param Player
     * @return
     */
    public Title sendTitlePacket(Player p) {
        return this.sendTitlePacket(new LinkedList<>(Collections.singletonList(p)));
    }

    /**
     * Send specified players an updated subtitle packet
     *
     * @param List
     *         of players
     * @return
     */
    public Title sendSubtitlePacket(Collection<? extends Player> players) {
        //set subtitle
        PacketContainer p1 = PM.createPacket(PacketType.Play.Server.TITLE);
        p1.getTitleActions().write(0, TitleAction.SUBTITLE);
        p1.getChatComponents().write(0, WrappedChatComponent.fromText(this.subtitle));

        for (Player p : players) {
            try {
                PM.sendServerPacket(p, p1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    /**
     * Sends the specified player an updated subtitle packet
     *
     * @param Player
     * @return
     */
    public Title sendSubtitlePacket(Player p) {
        return this.sendSubtitlePacket(new LinkedList<>(Collections.singletonList(p)));
    }

    /**
     * Sends specified players an updated times packet
     *
     * @param List
     *         of players
     * @return
     */
    public Title sendTimesPacket(Collection<? extends Player> players) {
        //set times
        PacketContainer p1 = PM.createPacket(PacketType.Play.Server.TITLE);
        p1.getTitleActions().write(0, TitleAction.TIMES);
        p1.getIntegers().write(0, this.fadeIn);
        p1.getIntegers().write(1, this.time);
        p1.getIntegers().write(2, this.fadeOut);

        for (Player p : players) {
            try {
                PM.sendServerPacket(p, p1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    /**
     * Sends the specified player an updated times packet
     *
     * @param Player
     * @return
     */
    public Title sendTimesPacket(Player p) {
        return this.sendTimesPacket(new LinkedList<Player>(Collections.singletonList(p)));
    }

	/* Static title creators */

    /**
     * Sends specified players a title clear packet
     *
     * @param List
     *         of players
     */
    public void sendTitleClearPacket(Collection<? extends Player> players) {
        //set packet to clear title
        PacketContainer p1 = PM.createPacket(PacketType.Play.Server.TITLE);
        p1.getTitleActions().write(0, TitleAction.CLEAR);

        for (Player p : players) {
            try {
                PM.sendServerPacket(p, p1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the specified player a title clear packet
     *
     * @param Player
     */
    public void sendTitleClearPacket(Player p) {
        this.sendTitleClearPacket(new LinkedList<>(Collections.singletonList(p)));
    }
}
