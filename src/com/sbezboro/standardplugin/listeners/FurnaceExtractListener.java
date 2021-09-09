package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.events.OreSmeltEvent;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.util.HashSet;

public class FurnaceExtractListener extends EventListener implements Listener {
	@SuppressWarnings("serial")
	private static final HashSet<Material> SMELT_ORES = new HashSet<Material>() {{
		add(Material.IRON_INGOT);
		add(Material.GOLD_INGOT);
		add(Material.NETHERITE_SCRAP);
	}};

	public FurnaceExtractListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onFurnaceExtract(FurnaceExtractEvent event) {
		// IMPORTANT NOTE
		// This event only fires when a player receives XP from a furnace.
		// If the output of the furnace is removed by a hopper, or if the
		// furnace has no XP left (because a previous partial removal
		// took the XP) then there will be no event tracking those
		// removals. Partial removals from a furnace can result from right
		// cliking on the furnace output (picking up half the stack) or
		// shift-clicking when the inventory doesn't have space for all
		// the outputs.
		// When this events fires it always contains the exact amount of
		// items removed from the furnace, even if there were more items
		// in the output slot.

		// Only do this logic when the feature is enabled
		if (! plugin.getRecordFurnaceSmelting()) return;

		Material itemType = event.getItemType();

		if (SMELT_ORES.contains(itemType)) {
			StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

			OreSmeltEvent ev = new OreSmeltEvent(player, itemType.toString(), event.getItemAmount());
			ev.log();
		}
	}
}
