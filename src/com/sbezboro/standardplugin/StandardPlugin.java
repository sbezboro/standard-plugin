package com.sbezboro.standardplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.alecgorge.minecraft.jsonapi.JSONAPI;
import com.sbezboro.standardplugin.commands.BaseCommand;
import com.sbezboro.standardplugin.commands.ForumMuteCommand;
import com.sbezboro.standardplugin.commands.GateCommand;
import com.sbezboro.standardplugin.commands.RankCommand;
import com.sbezboro.standardplugin.commands.RegisterCommand;
import com.sbezboro.standardplugin.commands.SetSpawnCommand;
import com.sbezboro.standardplugin.commands.SpawnCommand;
import com.sbezboro.standardplugin.commands.StandardCommand;
import com.sbezboro.standardplugin.commands.UnfreezeCommand;
import com.sbezboro.standardplugin.jsonapi.ForumPostAPICallHandler;
import com.sbezboro.standardplugin.jsonapi.PlayerTimeAPICallHandler;
import com.sbezboro.standardplugin.jsonapi.ServerStatusAPICallHandler;
import com.sbezboro.standardplugin.jsonapi.WebChatAPICallHandler;
import com.sbezboro.standardplugin.listeners.CreatureSpawnListener;
import com.sbezboro.standardplugin.listeners.DeathListener;
import com.sbezboro.standardplugin.listeners.PlayerInteractListener;
import com.sbezboro.standardplugin.listeners.PlayerJoinListener;
import com.sbezboro.standardplugin.listeners.PlayerMoveListener;
import com.sbezboro.standardplugin.listeners.PlayerLeaveListener;
import com.sbezboro.standardplugin.listeners.RespawnListener;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.persistence.GateStorage;
import com.sbezboro.standardplugin.persistence.IStorage;
import com.sbezboro.standardplugin.persistence.LogWriter;
import com.sbezboro.standardplugin.persistence.PlayerStorage;
import com.sbezboro.standardplugin.util.PlayerSaver;

public class StandardPlugin extends JavaPlugin {
	private static StandardPlugin instance;
	
	private List<BaseCommand> commands;
	private List<IStorage> storages;
	private List<LogWriter> logs;
	
	private GateStorage gateStorage;
	private PlayerStorage playerStorage;
	
	private int serverId = 0;
	private String secretKey = "";
	private boolean debug = false;
	
	private String endpoint;
	
	public StandardPlugin() {
		instance = this;
	}
	
	public static StandardPlugin getPlugin() {
		return instance;
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}

	@Override
	public void onEnable() {
		super.onEnable();

		saveDefaultConfig();

		getConfig().options().copyDefaults(true);
		saveConfig();
		
		storages = new ArrayList<IStorage>();
		gateStorage = new GateStorage(this);
		playerStorage = new PlayerStorage(this);
		storages.add(gateStorage);
		storages.add(playerStorage);
		
		logs = new ArrayList<LogWriter>();
		
		reloadPlugin();
		
		commands = new ArrayList<BaseCommand>();
		commands.add(new RegisterCommand(this));
		commands.add(new RankCommand(this));
		commands.add(new UnfreezeCommand(this));
		commands.add(new GateCommand(this));
		commands.add(new SetSpawnCommand(this));
		commands.add(new SpawnCommand(this));
		commands.add(new ForumMuteCommand(this));
		commands.add(new StandardCommand(this));

		getServer().getPluginManager().registerEvents(new DeathListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
		getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
		getServer().getPluginManager().registerEvents(new CreatureSpawnListener(this), this);
		
		registerJSONAPIHandlers();
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PlayerSaver(), 1200, 1200);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		
		for (LogWriter writer : logs) {
			writer.unload();
		}
		
		playerStorage.unload();

		Bukkit.getScheduler().cancelTasks(this);
	}
	
	public void reloadPlugin() {
		reloadConfig();
		
		serverId = getConfig().getInt("server-id");
		getLogger().info("Server starting with server id " + serverId);
		
		secretKey = getConfig().getString("secret-key");
		
		debug = getConfig().getBoolean("debug");
		if (debug) {
			getLogger().info("Debug mode enabled!");
		}
		
		endpoint = getConfig().getString("endpoint");
		
		for (IStorage storage : storages) {
			storage.reload();
		}
	}
	
	private void registerJSONAPIHandlers() {
		Plugin plugin = this.getServer().getPluginManager().getPlugin("JSONAPI");
		if (plugin != null) {
			JSONAPI jsonapi = (JSONAPI)plugin;
			
			jsonapi.registerAPICallHandler(new ForumPostAPICallHandler(this));
			jsonapi.registerAPICallHandler(new ServerStatusAPICallHandler(this));
			jsonapi.registerAPICallHandler(new PlayerTimeAPICallHandler(this));
			jsonapi.registerAPICallHandler(new WebChatAPICallHandler(this));
		}
	}
	
	public static void playerBroadcast(Player sender, final String message) {
		for (StandardPlayer player : instance.getOnlinePlayers()) {
			if (player != sender) {
				player.sendMessage(message);
			}
		}
	}
	
	public static void playerBroadcast(final String message) {
		playerBroadcast(null, message);
	}
	
	public StandardPlayer[] getOnlinePlayers() {
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		StandardPlayer[] result = new StandardPlayer[onlinePlayers.length];
		
		for (int i = 0; i < onlinePlayers.length; ++i) {
			result[i] = getStandardPlayer(onlinePlayers[i]);
		}
		
		return result;
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
	
	public GateStorage getGateStorage() {
		return gateStorage;
	}
	
	public StandardPlayer getStandardPlayer(String username) {
		return playerStorage.getPlayer(username);
	}
	
	public StandardPlayer getStandardPlayer(Object object) {
		if (!(object instanceof Player)) {
			return null;
		}
		
		return getStandardPlayer(((Player) object).getName());
	}
}
