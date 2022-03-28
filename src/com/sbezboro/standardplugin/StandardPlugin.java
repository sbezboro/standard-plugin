package com.sbezboro.standardplugin;

import com.alecgorge.minecraft.jsonapi.JSONAPI;
import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.standardplugin.commands.*;
import com.sbezboro.standardplugin.integrations.EssentialsIntegration;
import com.sbezboro.standardplugin.integrations.SimplyVanishIntegration;
import com.sbezboro.standardplugin.jsonapi.*;
import com.sbezboro.standardplugin.listeners.*;
import com.sbezboro.standardplugin.managers.EndResetManager;
import com.sbezboro.standardplugin.managers.HoneypotManager;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.AuditLogHttpRequest;
import com.sbezboro.standardplugin.persistence.LogWriter;
import com.sbezboro.standardplugin.persistence.PersistedPropertyDefinition;
import com.sbezboro.standardplugin.persistence.storages.*;
import com.sbezboro.standardplugin.util.MiscUtil;
import cz.jirutka.unidecode.Unidecode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class StandardPlugin extends JavaPlugin {
	@SuppressWarnings("serial")
	public static final HashSet<Material> BED_BLOCKS = new HashSet<Material>() {{
		add(Material.BLACK_BED);
		add(Material.BLUE_BED);
		add(Material.BROWN_BED);
		add(Material.CYAN_BED);
		add(Material.GRAY_BED);
		add(Material.GREEN_BED);
		add(Material.LIGHT_BLUE_BED);
		add(Material.LIGHT_GRAY_BED);
		add(Material.LIME_BED);
		add(Material.MAGENTA_BED);
		add(Material.ORANGE_BED);
		add(Material.PINK_BED);
		add(Material.PURPLE_BED);
		add(Material.RED_BED);
		add(Material.WHITE_BED);
		add(Material.YELLOW_BED);
	}};

	private static final String webchatPattern = "[*WC*]";
	private static final String consoleWebchatPattern = "[*CWC*]";

	public static final String OVERWORLD_NAME = "world";
	public static final String NEW_END_WORLD_NAME = "new_the_end";

	private static StandardPlugin instance;

	private List<IStorage> storages;
	private List<LogWriter> logs;

	private List<SubPlugin> subPlugins;

	private StandardConfig config;

	private GateStorage gateStorage;
	private TitleStorage titleStorage;
	private PlayerStorage playerStorage;
	private EndResetStorage endResetStorage;
	private HoneypotStorage honeypotStorage;

	private EndResetManager endResetManager;
	private HoneypotManager honeypotManager;

	private Unidecode unidecode;

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
		honeypotStorage = new HoneypotStorage(this);
		storages.add(gateStorage);
		storages.add(titleStorage);
		storages.add(playerStorage);
		storages.add(endResetStorage);
		storages.add(honeypotStorage);

		logs = new ArrayList<LogWriter>();

		subPlugins = new ArrayList<SubPlugin>();

		config = new StandardConfig(this);

		reloadPlugin();

		registerCommands();
		registerEvents();

		endResetManager = new EndResetManager(this, endResetStorage);
		honeypotManager = new HoneypotManager(this, honeypotStorage);

		EssentialsIntegration.init(this);
		SimplyVanishIntegration.init(this);

		registerJSONAPIHandlers();

		unidecode = Unidecode.toAscii();
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
	}

	public void reloadPlugin() {
		reloadConfig();

		config.reload();

		for (IStorage storage : storages) {
			storage.reload();
		}

		for (SubPlugin subPlugin : subPlugins) {
			subPlugin.reloadPlugin();
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
		commands.add(new EnablePvpCommand(this));
		commands.add(new PvpProtectionCommand(this));
		commands.add(new TitleCommand(this));
		commands.add(new TitlesCommand(this));
		commands.add(new WebchatCommand(this));
		commands.add(new EndCommand(this));
		commands.add(new ClearBedCommand(this));
		commands.add(new HoneypotCommand(this));
		commands.add(new EndresetCommand(this));
		commands.add(new UsernamesCommand(this));

		for (ICommand command : commands) {
			command.register();
		}
	}

	private void registerEvents() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new DeathListener(this), this);
		pluginManager.registerEvents(new ResurrectListener(this), this);
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
		pluginManager.registerEvents(new BlockBreakListener(this), this);
		pluginManager.registerEvents(new BlockPlaceListener(this), this);
		//pluginManager.registerEvents(new BlockFormListener(this), this);
		pluginManager.registerEvents(new PlayerBucketEmptyListener(this), this);
		pluginManager.registerEvents(new PlayerTeleportListener(this), this);
		pluginManager.registerEvents(new EntityPortalListener(this), this);
		pluginManager.registerEvents(new PlayerChatListener(this), this);
		pluginManager.registerEvents(new PlayerAdvancementListener(this), this);
		// Figure this out later
		//pluginManager.registerEvents(new FurnaceExtractListener(this), this);
	}

	private void registerJSONAPIHandlers() {
		Plugin plugin = this.getServer().getPluginManager().getPlugin("JSONAPI");
		if (plugin != null) {
			JSONAPI jsonapi = (JSONAPI) plugin;

			jsonapi.registerAPICallHandler(new ForumPostAPICallHandler(this));
			jsonapi.registerAPICallHandler(new NewMessageAPICallHandler(this));
			jsonapi.registerAPICallHandler(new PlayerActionAPICallHandler(this));
			jsonapi.registerAPICallHandler(new ServerSayAPICallHandler(this));
			jsonapi.registerAPICallHandler(new ServerStatusAPICallHandler(this));
			jsonapi.registerAPICallHandler(new StatsAPICallHandler(this));
			jsonapi.registerAPICallHandler(new WebChatAPICallHandler(this));
		}
	}

	public void registerSubPlugin(SubPlugin subPlugin) {
		getLogger().info("Registering sub-plugin '" + subPlugin.getSubPluginName() + "'");

		for (ICommand command : subPlugin.getCommands()) {
			command.register();
		}

		getConfig().addDefaults(subPlugin.getConfig());
		getConfig().options().copyDefaults(true);
		saveConfig();

		subPlugins.add(subPlugin);
	}

	public List<PersistedPropertyDefinition> getExtraPlayerPropertyDefinitions() {
		List<PersistedPropertyDefinition> properties = new ArrayList<PersistedPropertyDefinition>();

		for (SubPlugin subPlugin : getSubPlugins()) {
			properties.addAll(subPlugin.extraPlayerPropertyDefinitions());
		}

		return properties;
	}

	public boolean isNearActiveEndPortal(Location location, int blocks) {
		return endResetStorage.isNearActivePortal(location, blocks);
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

	public static void consoleWebchatMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(consoleWebchatPattern + message);
	}

	public static void webchatMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(webchatPattern + message);
	}

	public static void sendTitleMessage(StandardPlayer player, String title, String subtitle, int fadeIn, int time, int fadeOut) {
		// TODO: fix with 1.17
		return;
		/*
		PlayerConnection connection = ((CraftPlayer) player.getPlayer()).getHandle().b;

		Packet packet = new Packet(Packet.EnumTitleAction.TIMES, null, fadeIn, time, fadeOut);
		connection.sendPacket(packet);

		if (title != null) {
			IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
			packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, component);
			connection.sendPacket(packet);
		}

		if (subtitle != null) {
			IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
			packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, component);
			connection.sendPacket(packet);
		}*/
	}

	public List<StandardPlayer> getOnlinePlayers() {
		List<StandardPlayer> result = new ArrayList<StandardPlayer>();

		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		for (Player player : onlinePlayers) {
			result.add(getStandardPlayer(player));
		}

		return result;
	}

	/**
	 * Matches the given username to a currently online player's display name or
	 * an offline player's username that has played before
	 *
	 * @param username The username to query
	 * @return StandardPlayer instance if online or has played before, null
	 * otherwise
	 */
	public StandardPlayer matchPlayer(String username) {
		StandardPlayer player = null;

		String usernameLower = username.toLowerCase();

		for (StandardPlayer onlinePlayer : getOnlinePlayers()) {
			if (onlinePlayer.getDisplayName(false).toLowerCase().startsWith(usernameLower) ||
					onlinePlayer.getName().toLowerCase().startsWith(usernameLower)) {
				// Return a player with a display name that directly matches the query
				if (onlinePlayer.getDisplayName(false).equalsIgnoreCase(usernameLower) ||
						onlinePlayer.getName().equalsIgnoreCase(usernameLower)) {
					return onlinePlayer;
				}

				// Otherwise find the shortest length player name that the query is a prefix to
				if (player == null || onlinePlayer.getDisplayName(false).length() < player.getDisplayName(false).length()) {
					player = onlinePlayer;
				}
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

	public int getSpawnKillTimeout() {
		return config.getSpawnKillTimeout();
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

	public boolean shouldGenerateEndPortals() {
		return config.shouldGenerateEndPortals();
	}

	public int getPvpLogThreshold() {
		return config.getPvpLogThreshold();
	}

	public boolean getNerfEndermenDrops() {
		return config.getNerfEndermenDrops();
	}

	public boolean getNerfPigzombieDrops() {
		return config.getNerfPigzombieDrops();
	}

	public List<String> getMutedWords() {
		return config.getMutedWords();
	}

	public int getAnimalChunkCap() {
		return config.getAnimalChunkCap();
	}

	public int getDeathMessageTimeout() {
		return config.getDeathMessageTimeout();
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

	public HoneypotManager getHoneypotManager() {
		return honeypotManager;
	}

	public EndResetManager getEndResetManager() {
		return endResetManager;
	}

	public List<SubPlugin> getSubPlugins() {
		return subPlugins;
	}

	public StandardPlayer getStandardPlayerByUUID(String uuid) {
		return playerStorage.getPlayerByUUID(uuid);
	}

	public boolean shouldBlockMessage(String message) {
		String decodedMessage = unidecodeString(message).toLowerCase();

		for (String str : getMutedWords()) {
			// TODO: use regex patterns
			if (decodedMessage.contains(str.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	public void logAlert(String type, StandardPlayer player, Location location) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("x", location.getX());
		data.put("y", location.getY());
		data.put("z", location.getZ());
		data.put("world", location.getWorld().getName());

		logAlert(type, player, data);
	}

	public void logAlert(String type, StandardPlayer player, String message) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", message);

		logAlert(type, player, data);
	}

	public void logAlert(String type, StandardPlayer player, Map<String, Object> data) {
		String uuidString = null;
		if (player != null) {
			uuidString = player.getUuidString();
		}

		HttpRequestManager.getInstance().startRequest(new AuditLogHttpRequest(type, uuidString, data, null));
	}

	public String unidecodeString(String string) {
		return unidecode.decode(string);
	}

	@Deprecated
	public StandardPlayer getStandardPlayer(String username) {
		OfflinePlayer offlinePlayer = getServer().getOfflinePlayer(username);
		return getStandardPlayer(offlinePlayer);
	}

	public StandardPlayer getStandardPlayer(Object object) {
		if (!(object instanceof OfflinePlayer)) {
			return null;
		}

		String uuid = MiscUtil.getUuidString(((OfflinePlayer) object).getUniqueId());
		return getStandardPlayerByUUID(uuid);
	}
}
