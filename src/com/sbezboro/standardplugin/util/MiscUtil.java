package com.sbezboro.standardplugin.util;

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
		} else {
            return livingEntity.toString().substring(5);
        }
    }
}
