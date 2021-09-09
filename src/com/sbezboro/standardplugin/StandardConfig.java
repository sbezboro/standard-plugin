package com.sbezboro.standardplugin;

import org.bukkit.configuration.Configuration;

import java.util.List;

public class StandardConfig {
	private StandardPlugin plugin;

	private int serverId = 0;
	private String secretKey = "";
	private boolean debug = false;

	private String endpoint;
	private String rtsAddress;
	private List<String> mutedWords;
	private int pvpProtectionTime;
	private int hungerProtectionTime;
	private int newbieStalkerThreshold;

	private int endResetPeriod;
	private boolean generateEndPortals;

	private int pvpLogThreshold;

	private boolean nerfEndermenDrops;
	private boolean nerfPigzombieDrops;

	private int animalChunkCap;

	private int spawnKillTimeout;
	private int deathMessageTimeout;

	private boolean recordFurnaceSmelting;
	private boolean recordCrafting;
	private boolean recordSmithing;

	public StandardConfig(StandardPlugin plugin) {
		this.plugin = plugin;
	}

	public void reload() {
		Configuration config = plugin.getConfig();

		serverId = config.getInt("server-id");
		plugin.getLogger().info("Plugin starting with server id " + serverId);

		secretKey = config.getString("secret-key");

		debug = config.getBoolean("debug");
		if (debug) {
			plugin.getLogger().info("Debug mode enabled!");
		}

		endpoint = config.getString("endpoint");
		rtsAddress = config.getString("rts-address");
		pvpProtectionTime = config.getInt("pvp-protection-time");
		spawnKillTimeout = config.getInt("spawn-kill-timeout");
		deathMessageTimeout = config.getInt("death-message-timeout");
		hungerProtectionTime = config.getInt("hunger-protection-time");
		newbieStalkerThreshold = config.getInt("newbie-stalker-threshold");

		endResetPeriod = config.getInt("end-reset-period");
		generateEndPortals = config.getBoolean("generate-end-portals");

		pvpLogThreshold = config.getInt("pvp-log-threshold");

		nerfEndermenDrops = config.getBoolean("nerf-endermen-drops");
		nerfPigzombieDrops = config.getBoolean("nerf-pigzombie-drops");

		animalChunkCap = config.getInt("animal-chunk-cap");

		mutedWords = config.getStringList("muted-words");

		recordFurnaceSmelting = config.getBoolean("record-furnace-smelting");
		recordCrafting = config.getBoolean("record-crafting");
		recordSmithing = config.getBoolean("record-smithing");
	}

	public int getServerId() {
		return serverId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public boolean isDebug() {
		return debug;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public String getRTSAddress() {
		return rtsAddress;
	}

	public int getPvpProtectionTime() {
		return pvpProtectionTime;
	}

	public int getSpawnKillTimeout() {
		return spawnKillTimeout;
	}

	public int getDeathMessageTimeout() {
		return deathMessageTimeout;
	}

	public int getHungerProtectionTime() {
		return hungerProtectionTime;
	}

	public int getNewbieStalkerThreshold() {
		return newbieStalkerThreshold;
	}

	public int getEndResetPeriod() {
		return endResetPeriod;
	}

	public boolean shouldGenerateEndPortals() {
		return generateEndPortals;
	}

	public int getPvpLogThreshold() {
		return pvpLogThreshold;
	}

	public boolean getNerfEndermenDrops() {
		return nerfEndermenDrops;
	}

	public boolean getNerfPigzombieDrops() {
		return nerfPigzombieDrops;
	}

	public int getAnimalChunkCap() {
		return animalChunkCap;
	}

	public List<String> getMutedWords() {
		return mutedWords;
	}

	public boolean getRecordFurnaceSmelting() {
		return recordFurnaceSmelting;
	}

	public boolean getRecordCrafting() {
		return recordCrafting;
	}
	
	public boolean getRecordSmithing() {
		return recordSmithing;
	}
}