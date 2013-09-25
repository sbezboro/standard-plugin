package com.sbezboro.standardplugin.tasks;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.FactionsIntegration;
import com.sbezboro.standardplugin.managers.EndResetManager;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.persistence.storages.EndResetStorage;
import com.sbezboro.standardplugin.util.MiscUtil;

public class EndResetTask extends BaseTask {
	private EndResetStorage endResetStorage;
	private EndResetManager endResetManager;
	
	private World overworld;
	
	public EndResetTask(StandardPlugin plugin, World overworld) {
		super(plugin);
		
		this.endResetStorage = plugin.getEndResetStorage();
		this.endResetManager = plugin.getEndResetManager();
		this.overworld = overworld;
	}
	
	private boolean testEndPortalLocation(World world, int newX, int newY, int newZ) {
		// Faction territory, ignore
		if (!FactionsIntegration.isWilderness(new Location(world, newX, newY, newZ))
				|| !FactionsIntegration.isWilderness(new Location(world, newX + 8, newY, newZ + 8))) {
			return false;
		}
		
		for (int x = newX; x < newX + 9; ++x) {
			for (int z = newZ; z < newZ + 9; ++z) {
				for (int y = newY; y < newY + 8; ++y) {
					Block block = world.getBlockAt(x, y, z);
					
					if (block.getType() != Material.STONE && block.getType() != Material.DIRT
							&& block.getType() != Material.GRAVEL && block.getType() != Material.COAL_ORE
							&& block.getType() != Material.IRON_ORE) {
						return false;
					}
					
					if (block.getBiome() == Biome.OCEAN) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	private void generateEndPortal(World world, int newX, int newY, int newZ) {
		for (int x = newX; x < newX + 9; ++x) {
			for (int z = newZ; z < newZ + 9; ++z) {
				for (int y = newY; y < newY + 8; ++y) {
					Block block = world.getBlockAt(x, y, z);
					
					if (y <= newY + 2 || y == newY + 7 || x == newX || x == newX + 8 || z == newZ || z == newZ + 8) {
						block.setType(Material.SMOOTH_BRICK);
					} else {
						block.setType(Material.AIR);
					}
					
					if ((y == newY + 1 || y == newY + 2) && x >= newX + 3 && x <= newX + 5 && z >= newZ + 3 && z <= newZ + 5) {
						if (y == newY + 1) {
							block.setType(Material.LAVA);
						} else {
							block.setType(Material.AIR);
						}
					}
					
					if (y == newY + 3) {
						if (z >= newZ + 3 && z <= newZ + 5) {
							if (x == newX + 2) {
								block.setType(Material.ENDER_PORTAL_FRAME);
								block.setData((byte) 1); // West
							} else if (x == newX + 6) {
								block.setType(Material.ENDER_PORTAL_FRAME);
								block.setData((byte) 3); // East
							}
						} else if (x >= newX + 3 && x <= newX + 5) {
							if (z == newZ + 2) {
								block.setType(Material.ENDER_PORTAL_FRAME);
								block.setData((byte) 0); // South
							} else if ( z == newZ + 6) {
								block.setType(Material.ENDER_PORTAL_FRAME);
								block.setData((byte) 2); // North
							}
						}
					}
				}
			}
		}
	}
	
	private void destroyEndPortal(Location location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		
		for (int x2 = x - 4; x2 < x + 4; ++x2) {
			for (int z2 = z - 4; z2 < z + 4; ++z2) {
				Block block = location.getWorld().getBlockAt(x2, y, z2);
				
				if (block.getType() == Material.ENDER_PORTAL_FRAME
						|| block.getType() == Material.ENDER_PORTAL) {
					block.setType(Material.AIR);
				}
			}
		}
	}
	
	private void createNewEndPortal() {
		Location spawn = overworld.getSpawnLocation();
		
		int x = spawn.getBlockX();
		int z = spawn.getBlockZ();
		
		int newX;
		int newY;
		int newZ;
		
		do {
			do {
				newX = (int) (spawn.getBlockX() + (Math.random() * 4000.0) - 2000);
				newY = (int) (26 + Math.random() * 10);
				newZ = (int) (spawn.getBlockZ() + (Math.random() * 4000.0) - 2000);
			} while(Math.abs(newX - x) < 1000 || Math.abs(newZ - z) < 1000);
		} while(!testEndPortalLocation(overworld, newX, newY, newZ));

		generateEndPortal(overworld, newX, newY, newZ);
		
		// Saved location is the center of the room
		Location portalLocation = new Location(overworld, newX + 4, newY + 3, newZ + 4);
		endResetStorage.addActivePortalLocation(portalLocation);
		
		plugin.getLogger().info("New end portal generated at " + MiscUtil.locationFormat(portalLocation));
	}

	@Override
	public void run() {
		StandardPlugin.broadcast(String.format("%s%sAttention! The end is resetting!", ChatColor.BLUE, ChatColor.BOLD));
		
		World endWorld = endResetManager.getNewEndWorld();
		
		endResetStorage.incrementEndId();
		
		// Delete old portals
		for (Location location : endResetStorage.getActivePortals()) {
			destroyEndPortal(location);
			endResetStorage.removeActivePortalLocation(location);
			endResetStorage.addInactivePortalLocation(location);
		}
		
		// Send players home if they are in the end
		for (Player p : endWorld.getPlayers()) {
			StandardPlayer player = plugin.getStandardPlayer(p);
			player.sendHome(overworld);
		}
		
		// Set new end ID for online players
		for (StandardPlayer player : plugin.getOnlinePlayers()) {
			player.setEndId(endResetStorage.getCurrentEndId());
		}

		// Delete current end world from disk
		plugin.getServer().unloadWorld(endWorld, true);
		MiscUtil.deleteDirectory(endWorld.getWorldFolder());
		
		// Regenerate new end world
		endResetManager.linkNewEndWorld();
		
		// Find a suitable location and generate the portal room
		createNewEndPortal();

		StandardPlugin.broadcast(String.format("%s%sThe end has reset! Use eyes of ender to find the new end portal!", ChatColor.BLUE, ChatColor.BOLD));
	}

}
