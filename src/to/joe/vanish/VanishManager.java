package to.joe.vanish;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet29DestroyEntity;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;

import to.joe.vanish.sniffers.Sniffer17;
import to.joe.vanish.sniffers.Sniffer18ArmAnimation;
import to.joe.vanish.sniffers.Sniffer19EntityAction;
import to.joe.vanish.sniffers.Sniffer20NamedEntitySpawn;
import to.joe.vanish.sniffers.Sniffer28EntityVelocity;
import to.joe.vanish.sniffers.Sniffer30Entity;
import to.joe.vanish.sniffers.Sniffer31RelEntityMove;
import to.joe.vanish.sniffers.Sniffer32EntityLook;
import to.joe.vanish.sniffers.Sniffer33RelEntityMoveLook;
import to.joe.vanish.sniffers.Sniffer34EntityTeleport;
import to.joe.vanish.sniffers.Sniffer38EntityStatus;
import to.joe.vanish.sniffers.Sniffer39AttachEntity;
import to.joe.vanish.sniffers.Sniffer5EntityEquipment;

/**
 * It's the vanishing manager!
 * 
 * @author mbaxter
 * 
 */
public class VanishManager {

    public int RANGE = 512;

    private final VanishPlugin plugin;
    private final Object syncEID = new Object();
    private final Object syncLogin = new Object();
    private ArrayList<Integer> listOfEntityIDs;
    private HashMap<String, String> playerLoginStatements;

    public VanishManager(VanishPlugin plugin) {
        this.reset();
        this.plugin = plugin;
        SpoutManager.getPacketManager().addListener(5, new Sniffer5EntityEquipment(this));
        SpoutManager.getPacketManager().addListener(17, new Sniffer17(this));
        SpoutManager.getPacketManager().addListener(18, new Sniffer18ArmAnimation(this));
        SpoutManager.getPacketManager().addListener(19, new Sniffer19EntityAction(this));
        SpoutManager.getPacketManager().addListener(20, new Sniffer20NamedEntitySpawn(this));
        SpoutManager.getPacketManager().addListener(28, new Sniffer28EntityVelocity(this));
        // SpoutManager.getPacketManager().addListener(29, new Sniffer29DestroyEntity(this));
        /*
         * All players will receive the DestroyEntity packet Rather minor
         */
        SpoutManager.getPacketManager().addListener(30, new Sniffer30Entity(this));
        SpoutManager.getPacketManager().addListener(31, new Sniffer31RelEntityMove(this));
        SpoutManager.getPacketManager().addListener(32, new Sniffer32EntityLook(this));
        SpoutManager.getPacketManager().addListener(33, new Sniffer33RelEntityMoveLook(this));
        SpoutManager.getPacketManager().addListener(34, new Sniffer34EntityTeleport(this));
        SpoutManager.getPacketManager().addListener(38, new Sniffer38EntityStatus(this));
        SpoutManager.getPacketManager().addListener(39, new Sniffer39AttachEntity(this));
    }

    public void addLoginLine(String player, String message) {
        synchronized (this.syncLogin) {
            this.playerLoginStatements.put(player, message);
        }
    }

    public boolean hasLoginLineStored(String player) {
        synchronized (this.syncLogin) {
            return this.playerLoginStatements.containsKey(player);
        }
    }

    /**
     * Is the player vanished?
     * 
     * @param player
     * @return true if vanished
     */
    public boolean isVanished(Player player) {
        synchronized (this.syncEID) {
            return this.listOfEntityIDs.contains(((CraftPlayer) player).getEntityId());
        }
    }

    public void packetSending(Player vanishingPlayer) {
        final boolean vanishing = !this.isVanished(vanishingPlayer);
        final String vanishingPlayerName = vanishingPlayer.getName();
        if (vanishing) {
            this.addEIDVanished(((CraftPlayer) vanishingPlayer).getEntityId());
            this.plugin.log(vanishingPlayerName + " disappeared.");
        } else {
            this.removeVanished(((CraftPlayer) vanishingPlayer).getEntityId());
            this.plugin.log(vanishingPlayerName + " reappeared.");
        }
        final Player[] playerList = this.plugin.getServer().getOnlinePlayers();
        for (final Player otherPlayer : playerList) {
            if ((this.getDistance(vanishingPlayer, otherPlayer) > this.RANGE) || (otherPlayer.equals(vanishingPlayer))) {
                continue;
            }
            if (!Perms.canSeeAll(otherPlayer)) {
                if (vanishing) {
                    this.destroyEntity(vanishingPlayer, otherPlayer);
                } else {
                    this.undestroyEntity(vanishingPlayer, otherPlayer);
                }
            }
        }
    }

    /**
     * Drop a player from the list of vanished
     * Useful for when they quit
     * 
     * @param player
     */
    public void removeVanished(Player player) {
        this.removeVanished(((CraftPlayer) player).getEntityId());
    }

    /**
     * Smack that vanish list. Smack it hard.
     */
    public void reset() {
        synchronized (this.syncEID) {
            this.listOfEntityIDs = new ArrayList<Integer>();
        }
        synchronized (this.syncLogin) {
            this.playerLoginStatements = new HashMap<String, String>();
        }
    }

    /**
     * If the entity id eid should be hidden from the player
     * 
     * @param player
     * @param eid
     * @return if the eid is vanished and the player shouldn't see it
     */
    public boolean shouldHide(Player player, int eid) {
        if (!Perms.canSeeAll(player)) {
            return this.isVanished(eid);
        }
        return false;
    }

    /**
     * Toggle a player's visibility
     * 
     * @param vanishingPlayer
     *            The player disappearing
     */
    public void toggleVanish(Player vanishingPlayer) {
        this.packetSending(vanishingPlayer);
        final String vanishingPlayerName = vanishingPlayer.getName();
        String messageVanisher;
        String messageJoin = null;
        final String base = ChatColor.YELLOW + vanishingPlayerName + " has ";
        if (this.isVanished(vanishingPlayer)) {
            messageVanisher = base + "vanished. Poof.";
        } else {
            messageVanisher = base + "become visible.";
            messageJoin = this.fetchLoginLine(vanishingPlayerName);
        }
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (player == null) {
                continue;
            }
            if (Perms.canSeeAll(player)) {
                player.sendMessage(messageVanisher);
            }
            if (messageJoin != null) {
                player.sendMessage(messageJoin);
            }
        }
    }

    private void addEIDVanished(int id) {
        synchronized (this.syncEID) {
            this.listOfEntityIDs.add(id);
        }
    }

    private void destroyEntity(Player vanishingPlayer, Player obliviousPlayer) {
        ((CraftPlayer) obliviousPlayer).getHandle().netServerHandler.sendPacket(new Packet29DestroyEntity(((CraftPlayer) vanishingPlayer).getEntityId()));
    }

    private String fetchLoginLine(String player) {
        String message;
        synchronized (this.syncLogin) {
            message = this.playerLoginStatements.get(player);
            this.playerLoginStatements.remove(player);
        }
        return message;
    }

    private double getDistance(Player player1, Player player2) {
        final Location loc1 = player1.getLocation();
        final Location loc2 = player1.getLocation();
        return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2.0D) + Math.pow(loc1.getY() - loc2.getY(), 2.0D) + Math.pow(loc1.getZ() - loc2.getZ(), 2.0D));
    }

    private boolean isVanished(int id) {
        synchronized (this.syncEID) {
            return this.listOfEntityIDs.contains(id);
        }
    }

    private void removeVanished(int id) {
        synchronized (this.syncEID) {
            this.listOfEntityIDs.remove(Integer.valueOf(id));
        }
    }

    private void undestroyEntity(Player revealPlayer, Player nowAwarePlayer) {
        ((CraftPlayer) nowAwarePlayer).getHandle().netServerHandler.sendPacket(new Packet20NamedEntitySpawn(((CraftPlayer) revealPlayer).getHandle()));
    }

}