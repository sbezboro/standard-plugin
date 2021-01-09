package com.sbezboro.standardplugin.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.TimeZone;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

public class MiscUtil {

	public static String getRankString(int rank) {
		String ending;

		if (rank >= 10 && rank <= 20) {
			ending = "th";
		} else if (rank % 10 == 1) {
			ending = "st";
		} else if (rank % 10 == 2) {
			ending = "nd";
		} else if (rank % 10 == 3) {
			ending = "rd";
		} else {
			ending = "th";
		}
		return rank + ending;
	}

	public static String getNameFromLivingEntity(LivingEntity livingEntity) {
		if (livingEntity == null) {
			return "dispenser";
		} else if (livingEntity instanceof Player) {
			return ((Player) livingEntity).getName();
		} else if (livingEntity instanceof Wolf || livingEntity instanceof Ocelot) {
			return livingEntity.toString().substring(5, livingEntity.toString().indexOf("{"));
		} else if (livingEntity instanceof Horse) {
			Horse horse = (Horse) livingEntity;
			Variant variant = horse.getVariant();

			return variant.name().toLowerCase();
		} else if (livingEntity instanceof Rabbit) {
			return "rabbit";
		} else {
			return livingEntity.toString().substring(5);
		}
	}
	
	public static String locationFormat(Location location) {
		return String.format("([%s] %d, %d, %d)", location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public static String pluralize(String string, int count) {
		return count != 1 ? string + "s" : string;
	}

	public static String pluralize(String string, long count) {
		return count != 1 ? string + "s" : string;
	}

	public static boolean isWrapperType(Class<?> cls) {
		return getWrapperTypes().contains(cls);
	}

	private static HashSet<Class<?>> getWrapperTypes() {
		HashSet<Class<?>> ret = new HashSet<Class<?>>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		ret.add(String.class);
		return ret;
	}
	
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File files[] = path.listFiles();
			
			for (int i = 0; i < files.length; ++i) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		
		return path.delete();
	}
	
	public static String friendlyTimestamp(long timestamp) {
		return friendlyTimestamp(timestamp, "America/New_York");
	}
	
	public static String friendlyTimestamp(long timestamp, String timezone) {
		DateFormat format = new SimpleDateFormat("MMMM d h:mm a zz");
		format.setTimeZone(TimeZone.getTimeZone(timezone));
		return format.format(timestamp);
	}

	public static String getLocationKey(Location location) {
		return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
	}

	public static String getChunkKey(Chunk chunk) {
		return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ();
	}

	public static boolean safeBoolean(Object object) {
		if (object == null) {
			return false;
		} else {
			try {
				return (Boolean) object;
			} catch (ClassCastException e) {
				return false;
			}
		}
	}

	public static String getUuidString(UUID uuid) {
		return uuid.toString().replaceAll("-", "");
	}

	public static UUID getUuid(String uuidString) {
		String uuidWithDashes = uuidString.replaceFirst(
				"([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)",
				"$1-$2-$3-$4-$5");
		return UUID.fromString(uuidWithDashes);
	}

	public static Block[] getAdjacentBlocks(Block block) {
		return new Block[] {
				block.getRelative(BlockFace.NORTH),
				block.getRelative(BlockFace.EAST),
				block.getRelative(BlockFace.SOUTH),
				block.getRelative(BlockFace.WEST)
		};
	}

	public static LivingEntity getLivingEntityFromDamageEvent(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent lastDamageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity damager = lastDamageByEntityEvent.getDamager();

			if (damager instanceof Projectile) {
				Projectile projectile = (Projectile) damager;
				ProjectileSource source = projectile.getShooter();

				if (source instanceof LivingEntity) {
					return (LivingEntity) source;
				}
			} else if (damager instanceof LivingEntity) {
				return (LivingEntity) damager;
			}
		}

		return null;
	}

}
