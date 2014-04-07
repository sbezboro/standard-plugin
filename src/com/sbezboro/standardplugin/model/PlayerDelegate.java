package com.sbezboro.standardplugin.model;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.sbezboro.standardplugin.persistence.PersistedObject;
import com.sbezboro.standardplugin.persistence.storages.PlayerStorage;

@SuppressWarnings("deprecation")
public abstract class PlayerDelegate extends PersistedObject implements Player {
	protected Player player;
	protected OfflinePlayer offlinePlayer;

	public PlayerDelegate(Player player, PlayerStorage storage) {
		super(storage, player.getName());

		this.player = player;
		this.offlinePlayer = Bukkit.getOfflinePlayer(player.getName());
	}

	public PlayerDelegate(OfflinePlayer offlinePlayer, PlayerStorage storage) {
		super(storage, offlinePlayer.getName());

		this.player = null;
		this.offlinePlayer = offlinePlayer;
	}

	public Player getBasePlayer() {
		return player;
	}

	public OfflinePlayer getBaseOfflinePlayer() {
		return offlinePlayer;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setOfflinePlayer(OfflinePlayer offlinePlayer) {
		this.offlinePlayer = offlinePlayer;
	}

	public Location getBedSpawnLocation() {
		return offlinePlayer.getBedSpawnLocation();
	}

	public long getFirstPlayed() {
		return offlinePlayer.getFirstPlayed();
	}

	public long getLastPlayed() {
		return offlinePlayer.getLastPlayed();
	}

	public String getName() {
		return offlinePlayer.getName();
	}

	public Player getPlayer() {
		return offlinePlayer.getPlayer();
	}

	public boolean hasPlayedBefore() {
		return offlinePlayer.hasPlayedBefore();
	}

	public boolean isBanned() {
		return offlinePlayer.isBanned();
	}

	public boolean isOnline() {
		return offlinePlayer.isOnline();
	}

	public boolean isOp() {
		return offlinePlayer.isOp();
	}

	public boolean isWhitelisted() {
		return offlinePlayer.isWhitelisted();
	}

	public Map<String, Object> serialize() {
		return offlinePlayer.serialize();
	}

	@Deprecated
	public void setBanned(boolean banned) {
		offlinePlayer.setBanned(banned);
	}

	public void setOp(boolean value) {
		offlinePlayer.setOp(value);
	}

	public void setWhitelisted(boolean value) {
		offlinePlayer.setWhitelisted(value);
	}

	public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
		player.abandonConversation(conversation, details);
	}

	public void abandonConversation(Conversation conversation) {
		player.abandonConversation(conversation);
	}

	public void acceptConversationInput(String input) {
		player.acceptConversationInput(input);
	}

	public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
		return player.addAttachment(arg0, arg1);
	}

	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
		return player.addAttachment(arg0, arg1, arg2, arg3);
	}

	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
		return player.addAttachment(arg0, arg1, arg2);
	}

	public PermissionAttachment addAttachment(Plugin arg0) {
		return player.addAttachment(arg0);
	}

	public boolean addPotionEffect(PotionEffect effect, boolean force) {
		return player.addPotionEffect(effect, force);
	}

	public boolean addPotionEffect(PotionEffect effect) {
		return player.addPotionEffect(effect);
	}

	public boolean addPotionEffects(Collection<PotionEffect> effects) {
		return player.addPotionEffects(effects);
	}

	public void awardAchievement(Achievement achievement) {
		player.awardAchievement(achievement);
	}

	@Override
	public void removeAchievement(Achievement achievement) {
		player.removeAchievement(achievement);
	}

	@Override
	public boolean hasAchievement(Achievement achievement) {
		return player.hasAchievement(achievement);
	}

	public boolean beginConversation(Conversation conversation) {
		return player.beginConversation(conversation);
	}

	public boolean canSee(Player player) {
		return this.player.canSee(player);
	}

	public void chat(String msg) {
		player.chat(msg);
	}

	public void closeInventory() {
		player.closeInventory();
	}

	@Deprecated
	public void damage(int arg0, Entity arg1) {
		player.damage(arg0, arg1);
	}

	@Override
	public double getHealth() {
		return player.getHealth();
	}

	@Deprecated
	public void damage(int arg0) {
		player.damage(arg0);
	}

	public boolean eject() {
		return player.eject();
	}

	public Collection<PotionEffect> getActivePotionEffects() {
		return player.getActivePotionEffects();
	}

	public InetSocketAddress getAddress() {
		return player.getAddress();
	}

	public boolean getAllowFlight() {
		return player.getAllowFlight();
	}

	public boolean getCanPickupItems() {
		return player.getCanPickupItems();
	}

	public Location getCompassTarget() {
		return player.getCompassTarget();
	}

	public String getCustomName() {
		return player.getCustomName();
	}

	public String getDisplayName() {
		return player.getDisplayName();
	}

	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return player.getEffectivePermissions();
	}

	public Inventory getEnderChest() {
		return player.getEnderChest();
	}

	public int getEntityId() {
		return player.getEntityId();
	}

	public EntityEquipment getEquipment() {
		return player.getEquipment();
	}

	public float getExhaustion() {
		return player.getExhaustion();
	}

	public float getExp() {
		return player.getExp();
	}

	public int getExpToLevel() {
		return player.getExpToLevel();
	}

	public double getEyeHeight() {
		return player.getEyeHeight();
	}

	public double getEyeHeight(boolean ignoreSneaking) {
		return player.getEyeHeight(ignoreSneaking);
	}

	public Location getEyeLocation() {
		return player.getEyeLocation();
	}

	public float getFallDistance() {
		return player.getFallDistance();
	}

	public int getFireTicks() {
		return player.getFireTicks();
	}

	public float getFlySpeed() {
		return player.getFlySpeed();
	}

	public int getFoodLevel() {
		return player.getFoodLevel();
	}

	public GameMode getGameMode() {
		return player.getGameMode();
	}

	public PlayerInventory getInventory() {
		return player.getInventory();
	}

	public ItemStack getItemInHand() {
		return player.getItemInHand();
	}

	public ItemStack getItemOnCursor() {
		return player.getItemOnCursor();
	}

	public Player getKiller() {
		return player.getKiller();
	}

	public EntityDamageEvent getLastDamageCause() {
		return player.getLastDamageCause();
	}

	@Deprecated
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
		return player.getLastTwoTargetBlocks(transparent, maxDistance);
	}

	public int getLevel() {
		return player.getLevel();
	}

	@Deprecated
	public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
		return player.getLineOfSight(transparent, maxDistance);
	}

	public Set<String> getListeningPluginChannels() {
		return player.getListeningPluginChannels();
	}

	public Location getLocation() {
		return player.getLocation();
	}

	public Location getLocation(Location loc) {
		return player.getLocation(loc);
	}

	public int getMaxFireTicks() {
		return player.getMaxFireTicks();
	}

	public int getMaximumAir() {
		return player.getMaximumAir();
	}

	public int getMaximumNoDamageTicks() {
		return player.getMaximumNoDamageTicks();
	}

	public List<MetadataValue> getMetadata(String arg0) {
		return player.getMetadata(arg0);
	}

	public List<Entity> getNearbyEntities(double x, double y, double z) {
		return player.getNearbyEntities(x, y, z);
	}

	public int getNoDamageTicks() {
		return player.getNoDamageTicks();
	}

	public InventoryView getOpenInventory() {
		return player.getOpenInventory();
	}

	public Entity getPassenger() {
		return player.getPassenger();
	}

	public String getPlayerListName() {
		return player.getPlayerListName();
	}

	public long getPlayerTime() {
		return player.getPlayerTime();
	}

	public long getPlayerTimeOffset() {
		return player.getPlayerTimeOffset();
	}

	public WeatherType getPlayerWeather() {
		return player.getPlayerWeather();
	}

	public int getRemainingAir() {
		return player.getRemainingAir();
	}

	public boolean getRemoveWhenFarAway() {
		return player.getRemoveWhenFarAway();
	}

	public float getSaturation() {
		return player.getSaturation();
	}

	public Server getServer() {
		return player.getServer();
	}

	public int getSleepTicks() {
		return player.getSleepTicks();
	}

	@Deprecated
	public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
		return player.getTargetBlock(transparent, maxDistance);
	}

	public int getTicksLived() {
		return player.getTicksLived();
	}

	public int getTotalExperience() {
		return player.getTotalExperience();
	}

	public EntityType getType() {
		return player.getType();
	}

	public UUID getUniqueId() {
		return player.getUniqueId();
	}

	public Entity getVehicle() {
		return player.getVehicle();
	}

	public Vector getVelocity() {
		return player.getVelocity();
	}

	public float getWalkSpeed() {
		return player.getWalkSpeed();
	}

	public World getWorld() {
		return player.getWorld();
	}

	public void giveExp(int amount) {
		player.giveExp(amount);
	}

	public void giveExpLevels(int amount) {
		player.giveExpLevels(amount);
	}

	public boolean hasLineOfSight(Entity other) {
		return player.hasLineOfSight(other);
	}

	public boolean hasMetadata(String arg0) {
		return player.hasMetadata(arg0);
	}

	public boolean hasPermission(Permission arg0) {
		return player.hasPermission(arg0);
	}

	public boolean hasPermission(String arg0) {
		return player.hasPermission(arg0);
	}

	public boolean hasPotionEffect(PotionEffectType type) {
		return player.hasPotionEffect(type);
	}

	public void hidePlayer(Player player) {
		this.player.hidePlayer(player);
	}

	public void incrementStatistic(Statistic statistic, int amount) {
		player.incrementStatistic(statistic, amount);
	}

	@Override
	public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
		player.decrementStatistic(statistic, i);
	}

	@Override
	public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {
		player.setStatistic(statistic, i);
	}

	@Override
	public int getStatistic(Statistic statistic) throws IllegalArgumentException {
		return player.getStatistic(statistic);
	}

	public void incrementStatistic(Statistic statistic, Material material, int amount) {
		player.incrementStatistic(statistic, material, amount);
	}

	@Override
	public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
		player.decrementStatistic(statistic, material, i);
	}

	@Override
	public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
		player.setStatistic(statistic, material, i);
	}

	@Override
	public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		player.incrementStatistic(statistic, entityType);
	}

	@Override
	public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		player.decrementStatistic(statistic, entityType);
	}

	@Override
	public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		return player.getStatistic(statistic, entityType);
	}

	@Override
	public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {
		player.incrementStatistic(statistic, entityType, i);
	}

	@Override
	public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {
		player.decrementStatistic(statistic, entityType, i);
	}

	@Override
	public void setStatistic(Statistic statistic, EntityType entityType, int i) {
		player.setStatistic(statistic, entityType, i);
	}

	public void incrementStatistic(Statistic statistic, Material material) {
		player.incrementStatistic(statistic, material);
	}

	@Override
	public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		player.decrementStatistic(statistic, material);
	}

	@Override
	public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		return player.getStatistic(statistic, material);
	}

	public void incrementStatistic(Statistic statistic) {
		player.incrementStatistic(statistic);
	}

	@Override
	public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
		player.decrementStatistic(statistic);
	}

	public boolean isBlocking() {
		return player.isBlocking();
	}

	public boolean isConversing() {
		return player.isConversing();
	}

	public boolean isCustomNameVisible() {
		return player.isCustomNameVisible();
	}

	public boolean isDead() {
		return player.isDead();
	}

	public boolean isEmpty() {
		return player.isEmpty();
	}

	public boolean isFlying() {
		return player.isFlying();
	}

	public boolean isInsideVehicle() {
		return player.isInsideVehicle();
	}

	@Deprecated
	public boolean isOnGround() {
		return player.isOnGround();
	}

	public boolean isPermissionSet(Permission arg0) {
		return player.isPermissionSet(arg0);
	}

	public boolean isPermissionSet(String arg0) {
		return player.isPermissionSet(arg0);
	}

	public boolean isPlayerTimeRelative() {
		return player.isPlayerTimeRelative();
	}

	public boolean isSleeping() {
		return player.isSleeping();
	}

	public boolean isSleepingIgnored() {
		return player.isSleepingIgnored();
	}

	public boolean isSneaking() {
		return player.isSneaking();
	}

	public boolean isSprinting() {
		return player.isSprinting();
	}

	public boolean isValid() {
		return player.isValid();
	}

	public void kickPlayer(String message) {
		player.kickPlayer(message);
	}

	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
		return player.launchProjectile(projectile);
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector) {
		return player.launchProjectile(aClass, vector);
	}

	public boolean leaveVehicle() {
		return player.leaveVehicle();
	}

	public void loadData() {
		player.loadData();
	}

	public InventoryView openEnchanting(Location arg0, boolean arg1) {
		return player.openEnchanting(arg0, arg1);
	}

	public InventoryView openInventory(Inventory arg0) {
		return player.openInventory(arg0);
	}

	public void openInventory(InventoryView arg0) {
		player.openInventory(arg0);
	}

	public InventoryView openWorkbench(Location arg0, boolean arg1) {
		return player.openWorkbench(arg0, arg1);
	}

	public boolean performCommand(String command) {
		return player.performCommand(command);
	}

	public void playEffect(EntityEffect type) {
		player.playEffect(type);
	}

	@Deprecated
	public void playEffect(Location loc, Effect effect, int data) {
		player.playEffect(loc, effect, data);
	}

	public <T> void playEffect(Location loc, Effect effect, T data) {
		player.playEffect(loc, effect, data);
	}

	@Deprecated
	public void playNote(Location loc, byte instrument, byte note) {
		player.playNote(loc, instrument, note);
	}

	public void playNote(Location loc, Instrument instrument, Note note) {
		player.playNote(loc, instrument, note);
	}

	public void playSound(Location location, Sound sound, float volume, float pitch) {
		player.playSound(location, sound, volume, pitch);
	}

	public void recalculatePermissions() {
		player.recalculatePermissions();
	}

	public void remove() {
		player.remove();
	}

	public void removeAttachment(PermissionAttachment arg0) {
		player.removeAttachment(arg0);
	}

	public void removeMetadata(String arg0, Plugin arg1) {
		player.removeMetadata(arg0, arg1);
	}

	public void removePotionEffect(PotionEffectType type) {
		player.removePotionEffect(type);
	}

	public void resetMaxHealth() {
		player.resetMaxHealth();
	}

	public void resetPlayerTime() {
		player.resetPlayerTime();
	}

	public void resetPlayerWeather() {
		player.resetPlayerWeather();
	}

	public void saveData() {
		player.saveData();
	}

	@Deprecated
	public void sendBlockChange(Location loc, int material, byte data) {
		player.sendBlockChange(loc, material, data);
	}

	@Deprecated
	public void sendBlockChange(Location loc, Material material, byte data) {
		player.sendBlockChange(loc, material, data);
	}

	@Deprecated
	public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
		return player.sendChunkChange(loc, sx, sy, sz, data);
	}

	public void sendMap(MapView map) {
		player.sendMap(map);
	}

	public void sendMessage(String arg0) {
		player.sendMessage(arg0);
	}

	public void sendMessage(String[] arg0) {
		player.sendMessage(arg0);
	}

	public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
		player.sendPluginMessage(arg0, arg1, arg2);
	}

	public void sendRawMessage(String message) {
		player.sendRawMessage(message);
	}

	public void setAllowFlight(boolean flight) {
		player.setAllowFlight(flight);
	}

	public void setBedSpawnLocation(Location location, boolean force) {
		player.setBedSpawnLocation(location, force);
	}

	public void setBedSpawnLocation(Location location) {
		player.setBedSpawnLocation(location);
	}

	public void setCanPickupItems(boolean pickup) {
		player.setCanPickupItems(pickup);
	}

	public void setCompassTarget(Location loc) {
		player.setCompassTarget(loc);
	}

	public void setCustomName(String arg0) {
		player.setCustomName(arg0);
	}

	public void setCustomNameVisible(boolean arg0) {
		player.setCustomNameVisible(arg0);
	}

	public void setDisplayName(String name) {
		player.setDisplayName(name);
	}

	public void setExhaustion(float value) {
		player.setExhaustion(value);
	}

	public void setExp(float exp) {
		player.setExp(exp);
	}

	public void setFallDistance(float distance) {
		player.setFallDistance(distance);
	}

	public void setFireTicks(int ticks) {
		player.setFireTicks(ticks);
	}

	public void setFlySpeed(float value) throws IllegalArgumentException {
		player.setFlySpeed(value);
	}

	public void setFlying(boolean value) {
		player.setFlying(value);
	}

	public void setFoodLevel(int value) {
		player.setFoodLevel(value);
	}

	public void setGameMode(GameMode arg0) {
		player.setGameMode(arg0);
	}

	@Deprecated
	public void setHealth(int arg0) {
		player.setHealth(arg0);
	}

	@Override
	public double getMaxHealth() {
		return player.getMaxHealth();
	}

	public void setItemInHand(ItemStack arg0) {
		player.setItemInHand(arg0);
	}

	public void setItemOnCursor(ItemStack arg0) {
		player.setItemOnCursor(arg0);
	}

	@Deprecated
	public void setLastDamage(int damage) {
		player.setLastDamage(damage);
	}

	public void setLastDamageCause(EntityDamageEvent event) {
		player.setLastDamageCause(event);
	}

	@Override
	@Deprecated
	public int _INVALID_getLastDamage() {
		return player._INVALID_getLastDamage();
	}

	@Override
	@Deprecated
	public void _INVALID_setLastDamage(int i) {
		player._INVALID_setLastDamage(i);
	}

	@Override
	@Deprecated
	public void _INVALID_setMaxHealth(int i) {
		player._INVALID_setMaxHealth(i);
	}

	@Override
	@Deprecated
	public int _INVALID_getMaxHealth() {
		return player._INVALID_getMaxHealth();
	}

	@Override
	@Deprecated
	public void _INVALID_setHealth(int i) {
		player._INVALID_setHealth(i);
	}

	@Override
	@Deprecated
	public int _INVALID_getHealth() {
		return player._INVALID_getHealth();
	}

	@Override
	@Deprecated
	public void _INVALID_damage(int i, Entity entity) {
		player._INVALID_damage(i, entity);
	}

	@Override
	@Deprecated
	public void _INVALID_damage(int i) {
		player._INVALID_damage(i);
	}

	public void setLevel(int level) {
		player.setLevel(level);
	}

	@Deprecated
	public void setMaxHealth(int arg0) {
		player.setMaxHealth(arg0);
	}

	public void setMaximumAir(int ticks) {
		player.setMaximumAir(ticks);
	}

	public void setMaximumNoDamageTicks(int ticks) {
		player.setMaximumNoDamageTicks(ticks);
	}

	@Override
	public double getLastDamage() {
		return player.getLastDamage();
	}

	public void setMetadata(String arg0, MetadataValue arg1) {
		player.setMetadata(arg0, arg1);
	}

	public void setNoDamageTicks(int ticks) {
		player.setNoDamageTicks(ticks);
	}

	public boolean setPassenger(Entity passenger) {
		return player.setPassenger(passenger);
	}

	public void setPlayerListName(String name) {
		player.setPlayerListName(name);
	}

	public void setPlayerTime(long time, boolean relative) {
		player.setPlayerTime(time, relative);
	}

	public void setPlayerWeather(WeatherType arg0) {
		player.setPlayerWeather(arg0);
	}

	public void setRemainingAir(int ticks) {
		player.setRemainingAir(ticks);
	}

	public void setRemoveWhenFarAway(boolean remove) {
		player.setRemoveWhenFarAway(remove);
	}

	public void setSaturation(float value) {
		player.setSaturation(value);
	}

	public void setSleepingIgnored(boolean isSleeping) {
		player.setSleepingIgnored(isSleeping);
	}

	public void setSneaking(boolean sneak) {
		player.setSneaking(sneak);
	}

	public void setSprinting(boolean sprinting) {
		player.setSprinting(sprinting);
	}

	@Deprecated
	public void setTexturePack(String url) {
		player.setTexturePack(url);
	}

	@Override
	public void setResourcePack(String s) {
		player.setResourcePack(s);
	}

	public void setTicksLived(int value) {
		player.setTicksLived(value);
	}

	public void setTotalExperience(int exp) {
		player.setTotalExperience(exp);
	}

	public void setVelocity(Vector velocity) {
		player.setVelocity(velocity);
	}

	public void setWalkSpeed(float value) throws IllegalArgumentException {
		player.setWalkSpeed(value);
	}

	public boolean setWindowProperty(Property arg0, int arg1) {
		return player.setWindowProperty(arg0, arg1);
	}

	@Deprecated
	public Arrow shootArrow() {
		return player.shootArrow();
	}

	public void showPlayer(Player player) {
		this.player.showPlayer(player);
	}

	public boolean teleport(Entity destination, TeleportCause cause) {
		return player.teleport(destination, cause);
	}

	public boolean teleport(Entity destination) {
		return player.teleport(destination);
	}

	public boolean teleport(Location location, TeleportCause cause) {
		return player.teleport(location, cause);
	}

	public boolean teleport(Location location) {
		return player.teleport(location);
	}

	@Deprecated
	public Egg throwEgg() {
		return player.throwEgg();
	}

	@Deprecated
	public Snowball throwSnowball() {
		return player.throwSnowball();
	}

	@Deprecated
	public void updateInventory() {
		player.updateInventory();
	}

	public Scoreboard getScoreboard() {
		return player.getScoreboard();
	}

	public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
		player.setScoreboard(scoreboard);
	}

	public void damage(double arg0, Entity arg1) {
		player.damage(arg0, arg1);
	}

	public void damage(double arg0) {
		player.damage(arg0);
	}

	public void setHealth(double arg0) {
		player.setHealth(arg0);
	}

	public void setLastDamage(double arg0) {
		player.setLastDamage(arg0);
	}

	public void setMaxHealth(double arg0) {
		player.setMaxHealth(arg0);
	}

	public double getHealthScale() {
		return player.getHealthScale();
	}

	public Entity getLeashHolder() throws IllegalStateException {
		return player.getLeashHolder();
	}

	public boolean isHealthScaled() {
		return player.isHealthScaled();
	}

	public boolean isLeashed() {
		return player.isLeashed();
	}

	@Deprecated
	public void playSound(Location arg0, String arg1, float arg2, float arg3) {
		player.playSound(arg0, arg1, arg2, arg3);
	}

	public void setHealthScale(double arg0) throws IllegalArgumentException {
		player.setHealthScale(arg0);
	}

	public void setHealthScaled(boolean arg0) {
		player.setHealthScaled(arg0);
	}

	public boolean setLeashHolder(Entity arg0) {
		return player.setLeashHolder(arg0);
	}

	@Override
	public void sendSignChange(Location location, String[] strings) throws IllegalArgumentException {
		player.sendSignChange(location, strings);
	}

}
