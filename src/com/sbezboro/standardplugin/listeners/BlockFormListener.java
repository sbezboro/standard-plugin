package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class BlockFormListener extends EventListener implements Listener {

    public BlockFormListener(StandardPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onCobbleMonster(BlockFormEvent event) {
        Block block = event.getNewState().getBlock();
        Material m = block.getType();
        if (m.equals(Material.LAVA)) {
            Levelled l = (Levelled) block.getBlockData();
            if (l.getLevel() != 0) {
                event.setCancelled(true);
            }
        } else if (m.equals(Material.WATER)) {
            Levelled l = (Levelled) block.getBlockData();
            if (l.getLevel() == 0) {
                event.setCancelled(true);
            }
        }
    }
}