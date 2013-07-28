package com.sbezboro.standardplugin.util;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

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
}
