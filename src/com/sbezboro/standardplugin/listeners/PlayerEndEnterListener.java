package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEndEnterListener extends EventListener implements Listener {

    private final Map<UUID, Long> cooldowns;
    private final DecimalFormat f0x00 = new DecimalFormat("0.00");

    public PlayerEndEnterListener(StandardPlugin plugin) {
        super(plugin);
        cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();

        // Check if the player is in the list and their cooldown time is > 0, cancel.
        if (cooldowns.containsKey(player.getUniqueId())) {
            long time = cooldowns.get(player.getUniqueId()) - System.currentTimeMillis();
            if (time > 0) {
                event.getDamager().sendMessage(String.format("You can not damage that player for another %s seconds.",
                        f0x00.format(time / 1000)));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (5 * 1000));
        }
    }

}