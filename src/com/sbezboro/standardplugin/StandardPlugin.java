package com.sbezboro.standardplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.alecgorge.minecraft.jsonapi.JSONAPI;
import com.sbezboro.standardplugin.commands.EndCommand;
import com.sbezboro.standardplugin.commands.ForumMuteCommand;
import com.sbezboro.standardplugin.commands.GateCommand;
import com.sbezboro.standardplugin.commands.ICommand;
import com.sbezboro.standardplugin.commands.PvpProtectionCommand;
import com.sbezboro.standardplugin.commands.RankCommand;
import com.sbezboro.standardplugin.commands.RegisterCommand;
import com.sbezboro.standardplugin.commands.SetSpawnCommand;
import com.sbezboro.standardplugin.commands.SpawnCommand;
import com.sbezboro.standardplugin.commands.StandardCommand;
import com.sbezboro.standardplugin.commands.TitleCommand;
import com.sbezboro.standardplugin.commands.TitlesCommand;
import com.sbezboro.standardplugin.commands.UnfreezeCommand;
import com.sbezboro.standardplugin.commands.WebchatCommand;
import com.sbezboro.standardplugin.integrations.EssentialsIntegration;
import com.sbezboro.standardplugin.integrations.FactionsIntegration;
import com.sbezboro.standardplugin.integrations.SimplyVanishIntegration;
import com.sbezboro.standardplugin.jsonapi.ForumPostAPICallHandler;
import com.sbezboro.standardplugin.jsonapi.PlayerStatsAPICallHandler;
import com.sbezboro.standardplugin.jsonapi.ServerStatusAPICallHandler;
import com.sbezboro.standardplugin.jsonapi.WebChatAPICallHandler;
import com.sbezboro.standardplugin.listeners.CreatureSpawnListener;
import com.sbezboro.standardplugin.listeners.DeathListener;
import com.sbezboro.standardplugin.listeners.DispenseListener;
import com.sbezboro.standardplugin.listeners.EntityDamageListener;
import com.sbezboro.standardplugin.listeners.EntityDeathListener;
import com.sbezboro.standardplugin.listeners.FactionClaimDenyListener;
import com.sbezboro.standardplugin.listeners.HungerListener;
import com.sbezboro.standardplugin.listeners.PlayerInteractListener;
import com.sbezboro.standardplugin.listeners.PlayerJoinListener;
import com.sbezboro.standardplugin.listeners.PlayerLeaveListener;
import com.sbezboro.standardplugin.listeners.PlayerMoveListener;
import com.sbezboro.standardplugin.listeners.PlayerPortalListener;
import com.sbezboro.standardplugin.listeners.RespawnListener;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.persistence.IStorage;
import com.sbezboro.standardplugin.persistence.LogWriter;
import com.sbezboro.standardplugin.persistence.storages.EndResetStorage;
import com.sbezboro.standardplugin.persistence.storages.GateStorage;
import com.sbezboro.standardplugin.persistence.storages.PlayerStorage;
import com.sbezboro.standardplugin.persistence.storages.TitleStorage;
import com.sbezboro.standardplugin.tasks.EndResetCheckTask;
import com.sbezboro.standardplugin.tasks.EndResetTask;
import com.sbezboro.standardplugin.tasks.PlayerSaverTask;
import com.sbezboro.standardplugin.util.MiscUtil;

public class StandardPlugin extends JavaPlugin {
	private static final String webchatPattern = "[*WC*]";
	private static final String consoleWebchatPattern = "[*CWC*]";
	
	public static final String overworldName = "world";
	private static final String newEndWorldName = "new_the_end";
	
	private static StandardPlugin instance;

	private List<IStorage> storages;
	private List<LogWriter> logs;

	private StandardConfig config;

	private GateStorage gateStorage;
	private TitleStorage titleStorage;
	private PlayerStorage playerStorage;
	private EndResetStorage endResetStorage;
	
	private FactionClaimDenyListener denyListener;
	
	private World newEndWorld;

	private EndResetCheckTask endResetCheckTask;
	private PlayerSaverTask playerSaverTask;

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
		titleStorage = new TitleStorage(this);
		playerStorage = new PlayerStorage(this);
		endResetStorage = new EndResetStorage(this);
		storages.add(gateStorage);
		storages.add(titleStorage);
		storages.add(playerStorage);
		storages.add(endResetStorage);

		logs = new ArrayList<LogWriter>();

		config = new StandardConfig(this);

		reloadPlugin();

		registerCommands();
		registerEvents();

		EssentialsIntegration.init(this);
		SimplyVanishIntegration.init(this);
		FactionsIntegration.init(this);
		
		denyListener = new FactionClaimDenyListener(this);
		FactionsIntegration.addClaimDenyListener(denyListener);

		registerJSONAPIHandlers();

		playerSaverTask = new PlayerSaverTask(this);
		playerSaverTask.runTaskTimer(this, 1200, 1200);
		
		if (isEndResetEnabled()) {
			getLogger().info("End resets enabled");
			
			createNewEndWorld();
			
			if (endResetStorage.getNextReset() == 0) {
				scheduleNextEndReset();
			}
			
			if (isEndResetScheduled()) {
				endResetCheckTask = new EndResetCheckTask(this);
				endResetCheckTask.runTaskTimerAsynchronously(this, 20, 20);
				
				getLogger().info("End reset scheduled to be on "
						+ MiscUtil.friendlyTimestamp(endResetStorage.getNextReset(), "America/Los_Angeles"));
			} else {
				getLogger().info("No end resets scheduled since the Ender Dragon is still alive");
			}
		} else {
			getLogger().info("End resets disabled");
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();

		for (LogWriter writer : logs) {
			writer.unload();
		}

		for (IStorage storage : storages) {
			storage.unload();
		}

		Bukkit.getScheduler().cancelTasks(this);
		
		FactionsIntegration.removeClaimDenyListener(denyListener);
	}

	public void reloadPlugin() {
		reloadConfig();

		config.reload();

		for (IStorage storage : storages) {
			storage.reload();
		}
	}

	private void registerCommands() {
		List<ICommand> commands = new ArrayList<ICommand>();
		commands.add(new RegisterCommand(this));
		commands.add(new RankCommand(this));
		commands.add(new UnfreezeCommand(this));
		commands.add(new GateCommand(this));
		commands.add(new SetSpawnCommand(this));
		commands.add(new SpawnCommand(this));
		commands.add(new ForumMuteCommand(this));
		commands.add(new StandardCommand(this));
		commands.add(new PvpProtectionCommand(this));
		commands.add(new TitleCommand(this));
		commands.add(new TitlesCommand(this));
		commands.add(new WebchatCommand(this));
		commands.add(new EndCommand(this));

		for (ICommand command : commands) {
			command.register();
		}
	}

	private void registerEvents() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new DeathListener(this), this);
		pluginManager.registerEvents(new PlayerJoinListener(this), this);
		pluginManager.registerEvents(new PlayerLeaveListener(this), this);
		pluginManager.registerEvents(new PlayerInteractListener(this), this);
		pluginManager.registerEvents(new EntityDamageListener(this), this);
		pluginManager.registerEvents(new RespawnListener(this), this);
		pluginManager.registerEvents(new PlayerMoveListener(this), this);
		pluginManager.registerEvents(new CreatureSpawnListener(this), this);
		pluginManager.registerEvents(new HungerListener(this), this);
		pluginManager.registerEvents(new DispenseListener(this), this);
		pluginManager.registerEvents(new PlayerPortalListener(this), this);
		pluginManager.registerEvents(new EntityDeathListener(this), this);
	}

	private void registerJSONAPIHandlers() {
		Plugin plugin = this.getServer().getPluginManager().getPlugin("JSONAPI");
		if (plugin != null) {
			JSONAPI jsonapi = (JSONAPI) plugin;

			jsonapi.registerAPICallHandler(new ForumPostAPICallHandler(this));
			jsonapi.registerAPICallHandler(new ServerStatusAPICallHandler(this));
			jsonapi.registerAPICallHandler(new PlayerStatsAPICallHandler(this));
			jsonapi.registerAPICallHandler(new WebChatAPICallHandler(this));
		}
	}
	
	public void createNewEndWorld() {
		WorldCreator creator = new WorldCreator(newEndWorldName);
		creator.environment(Environment.THE_END);
		newEndWorld = getServer().createWorld(creator);
		newEndWorld.setKeepSpawnInMemory(false);
	}

	public void resetEnd() {
		if (isEndResetEnabled()) {
			World overworld = getServer().getWorld(overworldName);
			
			new EndResetTask(this, overworld).runTask(this);
		} else {
			getLogger().severe("resetEnd() called when end resets aren't enabled!");
		}
	}
	
	public void scheduleNextEndReset() {
		if (!isEndResetEnabled()) {
			return;
		}
		
		long nextReset = decideNextEndReset();
		
		endResetStorage.setNextReset(nextReset);
		
		broadcast(String.format("%s%sThe Ender Dragon has been killed! Next end reset scheduled to be on %s%s", 
				ChatColor.BLUE, ChatColor.BOLD, ChatColor.AQUA, MiscUtil.friendlyTimestamp(nextReset)));
		
		if (endResetCheckTask == null) {
			endResetCheckTask = new EndResetCheckTask(this);
			endResetCheckTask.runTaskTimerAsynchronously(this, 20, 20);
		}
	}
	
	private long decideNextEndReset() {
		// Get x days from now
		long time = System.currentTimeMillis() + getEndResetPeriod() * 86400000;
		// Round up to get the start of the next day in GMT, 5PM Pacific, 8PM Eastern,
		// when there are the most players on more-or-less
		return ((time / 86400000) + 1) * 86400000;
	}
	
	public boolean isEndResetScheduled() {
		return isEndResetEnabled() && endResetStorage.getNextReset() > System.currentTimeMillis();
	}
	
	private static String webchatConsoleGate(String message, boolean webchat, boolean console) {
		if (webchat) {
			if (console) {
				message = consoleWebchatPattern + message;
			} else {
				message = webchatPattern + message;
			}
		}
		
		return message;
	}

	// ------
	// Miscellaneous Player related utilty funcitions mirroring the respective
	// Bukkit versions with a StandardPlayer wrapper
	// ------
	public static void playerBroadcast(Player sender, final String message, boolean webchat, boolean console) {
		for (StandardPlayer player : instance.getOnlinePlayers()) {
			if (player != sender && player.isOnline()) {
				try {
					player.sendMessage(message);
				} catch (Exception e) {
					// Can happen if a player leaves as this handler is running
					instance.getLogger().severe("Exception while broadcasting");
					e.printStackTrace();
				}
			}
		}

		Bukkit.getConsoleSender().sendMessage(webchatConsoleGate(message, webchat, console));
	}
	
	public static void playerBroadcast(Player sender, final String message) {
		playerBroadcast(sender, message, true, true);
	}
	
	public static void broadcast(String message, boolean webchat, boolean console) {
		playerBroadcast(null, message, webchat, console);
	}
	
	public static void broadcast(String message) {
		broadcast(message, true, true);
	}
	
	public static void webchatMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(webchatPattern + message);
	}

	public StandardPlayer[] getOnlinePlayers() {
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		StandardPlayer[] result = new StandardPlayer[onlinePlayers.length];

		for (int i = 0; i < onlinePlayers.length; ++i) {
			result[i] = getStandardPlayer(onlinePlayers[i]);
		}

		return result;
	}

	/**
	 * Matches the given username to a currently online player's display name or
	 * an offline player's username that has played before
	 * 
	 * @param username
	 *            The username to query
	 * @return StandardPlayer instance if online or has played before, null
	 *         otherwise
	 */
	public StandardPlayer matchPlayer(String username) {
		StandardPlayer player = null;

		StandardPlayer[] onlinePlayers = getOnlinePlayers();
		for (StandardPlayer onlinePlayer : onlinePlayers) {
			if (onlinePlayer.getDisplayName(false).toLowerCase().startsWith(username.toLowerCase())) {
				player = onlinePlayer;
				break;
			}
		}

		if (player == null) {
			player = getStandardPlayer(username);
			if (!player.hasPlayedBefore()) {
				player = null;
			}
		}

		return player;
	}

	// ------
	// Configuration getters
	// ------
	public int getServerId() {
		return config.getServerId();
	}

	public String getSecretKey() {
		return config.getSecretKey();
	}

	public boolean isDebug() {
		return config.isDebug();
	}

	public String getEndpoint() {
		return config.getEndpoint();
	}

	public String getRTSAddress() {
		return config.getRTSAddress();
	}

	public int getPvpProtectionTime() {
		return config.getPvpProtectionTime();
	}

	public boolean isPvpProtectionEnabled() {
		return config.getPvpProtectionTime() > 0;
	}

	public int getHungerProtectionTime() {
		return config.getHungerProtectionTime();
	}

	public boolean isHungerProtectionEnabled() {
		return config.getHungerProtectionTime() > 0;
	}

	public int getNewbieStalkerThreshold() {
		return config.getNewbieStalkerThreshold();
	}

	public int getEndResetPeriod() {
		return config.getEndResetPeriod();
	}

	public boolean isEndResetEnabled() {
		return config.getEndResetPeriod() > 0;
	}
	
	public World getNewEndWorld() {
		return newEndWorld;
	}

	public GateStorage getGateStorage() {
		return gateStorage;
	}

	public TitleStorage getTitleStorage() {
		return titleStorage;
	}

	public EndResetStorage getEndResetStorage() {
		return endResetStorage;
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
