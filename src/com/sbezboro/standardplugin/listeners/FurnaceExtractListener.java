package com.sbezboro.standardplugin.listeners;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.events.OreSmeltEvent;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class FurnaceExtractListener extends EventListener implements Listener {
	@SuppressWarnings("serial")
	private static final HashSet<Material> SMELT_ORES = new HashSet<Material>() {{
		add(Material.IRON_INGOT);
		add(Material.GOLD_INGOT);
	}};

	public FurnaceExtractListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onFurnaceExtract(FurnaceExtractEvent event) {
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());
		
		Material itemType = event.getItemType();
		
		if (SMELT_ORES.contains(itemType)) {
			OreSmeltEvent ev = new OreSmeltEvent(player, itemType.toString(), event.getItemAmount());
			ev.log();
		}
	}

}
