package com.sbezboro.standardplugin.listeners;


import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.events.OreDiscoveryEvent;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class BlockBreakListener extends EventListener implements Listener {
	@SuppressWarnings("serial")
	private static final HashSet<Material> MINABLE_ORES = new HashSet<Material>() {{
		add(Material.DIAMOND_ORE);
		add(Material.DEEPSLATE_DIAMOND_ORE);
		add(Material.EMERALD_ORE);
		add(Material.DEEPSLATE_EMERALD_ORE);
		add(Material.COAL_ORE);
		add(Material.DEEPSLATE_COAL_ORE);
		add(Material.REDSTONE_ORE);
		add(Material.DEEPSLATE_REDSTONE_ORE);
		add(Material.LAPIS_ORE);
		add(Material.DEEPSLATE_LAPIS_ORE);
		add(Material.IRON_ORE);
		add(Material.DEEPSLATE_IRON_ORE);
		add(Material.GOLD_ORE);
		add(Material.DEEPSLATE_GOLD_ORE);
		add(Material.NETHER_QUARTZ_ORE);
	}};
	
	public BlockBreakListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
		Block block = event.getBlock();
		Location location = block.getLocation();
		
		// Don't let players destroy the obsidian spawn platform in the end
		if (location.getWorld().getEnvironment() == Environment.THE_END
				&& location.getBlockX() >= 98 && location.getBlockX() <= 102
				&& location.getBlockZ() >= -2 && location.getBlockZ() <= 2
				&& location.getBlockY() == 48) {
			event.setCancelled(true);
			return;
		}
		
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
		ItemStack tool = player.getItemInHand();

		// Track this ore discovery if breaking this block yields the raw material
		// Note that COPPER_ORE, IRON_ORE, and GOLD_ORE could be added to the leaderboards,
		// (+ DEEPSLATE_ versions) because they now also drop the raw material
		if (!tool.getEnchantments().containsKey(Enchantment.SILK_TOUCH)
				&& MINABLE_ORES.contains(block.getType())) {
			String type = block.getType().toString().replace("DEEPSLATE_", "");
			OreDiscoveryEvent ev = new OreDiscoveryEvent(player, type, location);
			ev.log();
		}
	}

}
