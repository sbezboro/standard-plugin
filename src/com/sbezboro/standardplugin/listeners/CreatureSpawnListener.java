package com.sbezboro.standardplugin.listeners;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.util.MiscUtil;

import java.util.HashSet;
import java.util.Set;

public class CreatureSpawnListener extends EventListener implements Listener {
	@SuppressWarnings("serial")
	private static final Set<EntityType> CONTROLLED_ENTITIES = new HashSet<EntityType>() {{
		add(EntityType.COW);
		add(EntityType.CHICKEN);
		add(EntityType.PIG);
		add(EntityType.SHEEP);
		add(EntityType.TURTLE);  // turtle farms can become very large
		add(EntityType.COD);
		add(EntityType.SALMON);
	}};

	public CreatureSpawnListener(StandardPlugin plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getLocation().getWorld();
		
		// Only allow withers to be spawned in the end
		if (entity.getType() == EntityType.WITHER) {
			if (world.getEnvironment() == Environment.THE_END) {
				plugin.getLogger().info("Wither spawned at " + MiscUtil.locationFormat(entity.getLocation()));
			} else {
				event.setCancelled(true);
				return;
			}
		}

		if (false && CONTROLLED_ENTITIES.contains(entity.getType())) {
			Location location = entity.getLocation();
			Chunk chunk = location.getChunk();

			int totalInChunk = 0;

			for (Entity otherEntity : chunk.getEntities()) {
				if (otherEntity.getType() == entity.getType()) {
					totalInChunk++;
				}

				if (totalInChunk > plugin.getAnimalChunkCap()) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}

}
