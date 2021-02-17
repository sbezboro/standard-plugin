package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.util.MiscUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerPortalListener extends EventListener implements Listener {

    public PlayerPortalListener(StandardPlugin plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        World fromWorld = event.getFrom().getWorld();

        if (event.getCause() == TeleportCause.END_PORTAL) {

            StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

            // Going from the end
            if (fromWorld.getEnvironment() == Environment.THE_END) {
                plugin.getLogger().info(player.getDisplayName(false) + " leaving the End.");

                Location to = getOverworldDestination(player);
                event.setTo(to);
            } else { // Going to the end
                if (player.isPvpProtected()) {
                    plugin.getLogger().info(player.getDisplayName(false) + " pvp protected and trying to go to the end, sending to bed/spawn instead.");

                    player.sendMessage("Sorry, PVP protected players cannot enter the End.");

                    Location to = getOverworldDestination(player);
                    event.setTo(to);
                } else {
                    World newEnd = plugin.getEndResetManager().getNewEndWorld();

                    if (newEnd != null) {
                        event.setTo(new Location(newEnd, 100, 50, 0));
                    }
                    plugin.getLogger().info(player.getDisplayName(false) + " going to the End.");
                }
            }
        }
    }

    private Location getOverworldDestination(StandardPlayer player) {
        // get player's bed location if one exists
        Location to = player.getBedLocationIfValid();

        if (to == null) {
            plugin.getLogger().info("Can't find bed for " + player.getDisplayName(false) + ", sending to spawn.");
            to = plugin.getServer().getWorld(StandardPlugin.OVERWORLD_NAME).getSpawnLocation();

            if (player.getBedLocation() != null) {
                plugin.getLogger().info("Bed location points to (" +
                        MiscUtil.locationFormat(player.getBedLocation()) + ") which is of type " +
                        player.getBedLocation().getBlock().getType());
            }
        }

        return to;

    }
}
