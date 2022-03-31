package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.util.MiscUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerAdvancementListener extends EventListener implements Listener {
	public PlayerAdvancementListener(StandardPlugin plugin) {
		super(plugin);
	}
	
	static HashMap<String, String> advancementNames = new HashMap<String, String>(){{
		put("adventure/adventuring_time", "Adventuring Time");
		put("adventure/arbalistic", "Arbalistic");
		put("adventure/hero_of_the_village", "Hero of the Village");
		put("adventure/kill_a_mob", "Monster Hunter");
		put("adventure/kill_all_mobs", "Monsters Hunted");
		put("adventure/ol_betsy", "Ol' Betsy");
		put("adventure/root", "Adventure");
		put("adventure/shoot_arrow", "Take Aim");
		put("adventure/sleep_in_bed", "Sweet Dreams");
		put("adventure/sniper_duel", "Sniper Duel");
		put("adventure/summon_iron_golem", "Hired Help");
		put("adventure/throw_trident", "A Throwaway Joke");
		put("adventure/totem_of_undying", "Postmortal");
		put("adventure/trade", "What a Deal!");
		put("adventure/two_birds_one_arrow", "Two Birds, One Arrow");
		put("adventure/very_very_frightening", "Very Very Frightening");
		put("adventure/voluntary_exile", "Voluntary Exile");
		put("adventure/whos_the_pillager_now", "Who's the Pillager Now?");
		put("end/dragon_breath", "You Need a Mint");
		put("end/dragon_egg", "The Next Generation");
		put("end/elytra", "Sky's the Limit");
		put("end/enter_end_gateway", "Remote Getaway");
		put("end/find_end_city", "The City at the End of the Game");
		put("end/kill_dragon", "Free the End");
		put("end/levitate", "Great View From Up Here");
		put("end/respawn_dragon", "The End... Again...");
		put("end/root", "The End");
		put("husbandry/balanced_diet", "A Balanced Diet");
		put("husbandry/break_diamond_hoe", "Serious Dedication");
		put("husbandry/breed_all_animals", "Two by Two");
		put("husbandry/breed_an_animal", "The Parrots and the Bats");
		put("husbandry/complete_catalogue", "A Complete Catalogue");
		put("husbandry/fishy_business", "Fishy Business");
		put("husbandry/plant_seed", "A Seedy Place");
		put("husbandry/root", "Husbandry");
		put("husbandry/tactical_fishing", "Tactical Fishing");
		put("husbandry/tame_an_animal", "Best Friends Forever");
		put("nether/all_effects", "How Did We Get Here?");
		put("nether/all_potions", "A Furious Cocktail");
		put("nether/brew_potion", "Local Brewery");
		put("nether/create_beacon", "Bring Home the Beacon");
		put("nether/create_full_beacon", "Beaconator");
		put("nether/fast_travel", "Subspace Bubble");
		put("nether/find_fortress", "A Terrible Fortress");
		put("nether/get_wither_skull", "Spooky Scary Skeleton");
		put("nether/obtain_blaze_rod", "Into Fire");
		put("nether/return_to_sender", "Return to Sender");
		put("nether/root", "Nether");
		put("nether/summon_wither", "Withering Heights");
		put("nether/uneasy_alliance", "Uneasy Alliance");
		put("story/cure_zombie_villager", "Zombie Doctor");
		put("story/deflect_arrow", "Not Today, Thank You");
		put("story/enchant_item", "Enchanter");
		put("story/enter_the_end", "The End?");
		put("story/enter_the_nether", "We Need to Go Deeper");
		put("story/follow_ender_eye", "Eye Spy");
		put("story/form_obsidian", "Ice Bucket Challenge");
		put("story/iron_tools", "Isn't It Iron Pick");
		put("story/lava_bucket", "Hot Stuff");
		put("story/mine_diamond", "Diamonds!");
		put("story/mine_stone", "Stone Age");
		put("story/obtain_armor", "Suit Up");
		put("story/root", "Minecraft");
		put("story/shiny_gear", "Cover Me With Diamonds");
		put("story/smelt_iron", "Acquire Hardware");
		put("story/upgrade_tools", "Getting an Upgrade");
	}};

    @EventHandler
	public void onAdvancementDone(PlayerAdvancementDoneEvent event){
		StandardPlayer player = plugin.getStandardPlayer(event.getPlayer());

		// example keys:
		// recipes/brewing/cauldron (are ignored)
		// husbandry/tactical_fishing

		String advancementKey = event.getAdvancement().getKey().getKey();
		if (!advancementKey.startsWith("recipes")) {
			String name = advancementNames.get(advancementKey);
			if (name == null) {
				name = advancementKey.substring(advancementKey.indexOf('/') + 1).replaceAll("_", " ");
			}

			StandardPlugin.webchatMessage(String.format("%s%s%s has made the advancement %s[%s]", ChatColor.AQUA, player.getDisplayName(), ChatColor.WHITE, ChatColor.GREEN, name));
		}
	}
}
