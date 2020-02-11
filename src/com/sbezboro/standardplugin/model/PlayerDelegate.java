package com.sbezboro.standardplugin.model;

import com.sbezboro.standardplugin.persistence.PersistedObject;
import com.sbezboro.standardplugin.persistence.storages.PlayerStorage;
import com.sbezboro.standardplugin.util.MiscUtil;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.*;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.util.*;

@SuppressWarnings("deprecation")
public abstract class PlayerDelegate extends PersistedObject implements Player {
	protected Player player;
	protected OfflinePlayer offlinePlayer;

	public PlayerDelegate(Player player, PlayerStorage storage) {
		super(storage, MiscUtil.getUuidString(player.getUniqueId()));

		this.player = player;
		this.offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
	}

	public PlayerDelegate(OfflinePlayer offlinePlayer, PlayerStorage storage) {
		super(storage, MiscUtil.getUuidString(offlinePlayer.getUniqueId()));

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

	@Override
	public boolean discoverRecipe(NamespacedKey namespacedKey) {
		return player.discoverRecipe(namespacedKey);
	}

	@Override
	public int discoverRecipes(Collection<NamespacedKey> collection) {
		return player.discoverRecipes(collection);
	}

	@Override
	public boolean undiscoverRecipe(NamespacedKey namespacedKey) {
		return player.undiscoverRecipe(namespacedKey);
	}

	@Override
	public int undiscoverRecipes(Collection<NamespacedKey> collection) {
		return player.undiscoverRecipes(collection);
	}

	@Override
	@Deprecated
	public Entity getShoulderEntityLeft() {
		return player.getShoulderEntityLeft();
	}

	@Override
	@Deprecated
	public void setShoulderEntityLeft(Entity entity) {
		player.setShoulderEntityLeft(entity);
	}

	@Override
	@Deprecated
	public Entity getShoulderEntityRight() {
		return player.getShoulderEntityRight();
	}

	@Override
	@Deprecated
	public void setShoulderEntityRight(Entity entity) {
		player.setShoulderEntityRight(entity);
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

	@Deprecated
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

	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i) {
		return player.getLastTwoTargetBlocks(set, i);
	}

	@Override
	public Block getTargetBlockExact(int i) {
		return player.getTargetBlockExact(i);
	}

	@Override
	public Block getTargetBlockExact(int i, FluidCollisionMode fluidCollisionMode) {
		return player.getTargetBlockExact(i, fluidCollisionMode);
	}

	@Override
	public RayTraceResult rayTraceBlocks(double v) {
		return player.rayTraceBlocks(v);
	}

	@Override
	public RayTraceResult rayTraceBlocks(double v, FluidCollisionMode fluidCollisionMode) {
		return player.rayTraceBlocks(v, fluidCollisionMode);
	}

	public int getLevel() {
		return player.getLevel();
	}

	@Override
	public List<Block> getLineOfSight(Set<Material> set, int i) {
		return player.getLineOfSight(set, i);
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

	@Deprecated
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

	@Override
	@Deprecated
	public boolean isPersistent() {
		return player.isPersistent();
	}

	@Override
	@Deprecated
	public void setPersistent(boolean b) {
		player.setPersistent(b);
	}

	public int getSleepTicks() {
		return player.getSleepTicks();
	}

	@Override
	public Block getTargetBlock(Set<Material> set, int i) {
		return player.getTargetBlock(set, i);
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
		return offlinePlayer.getUniqueId();
	}

	public Entity getVehicle() {
		return player.getVehicle();
	}

	public Vector getVelocity() {
		return player.getVelocity();
	}

	@Override
	public double getHeight() {
		return player.getHeight();
	}

	@Override
	public double getWidth() {
		return player.getWidth();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return player.getBoundingBox();
	}

	public float getWalkSpeed() {
		return player.getWalkSpeed();
	}

	public World getWorld() {
		return player.getWorld();
	}

	@Override
	@Deprecated
	public void setRotation(float v, float v1) {
		player.setRotation(v, v1);
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

	@Override
	public PotionEffect getPotionEffect(PotionEffectType potionEffectType) {
		return player.getPotionEffect(potionEffectType);
	}

	@Deprecated
	public void hidePlayer(Player player) {
		this.player.hidePlayer(player);
	}

	@Override
	public void hidePlayer(Plugin plugin, Player player) {
		this.player.hidePlayer(plugin, player);
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

	@Deprecated
	public void resetMaxHealth() {
		player.resetMaxHealth();
	}

	public void resetPlayerTime() {
		player.resetPlayerTime();
	}

	public void resetPlayerWeather() {
		player.resetPlayerWeather();
	}

	@Override
	public boolean isHandRaised() {
		return player.isHandRaised();
	}

	public void saveData() {
		player.saveData();
	}

	@Deprecated
	public void sendBlockChange(Location loc, Material material, byte data) {
		player.sendBlockChange(loc, material, data);
	}

	@Override
	public void sendBlockChange(Location location, BlockData blockData) {
		player.sendBlockChange(location, blockData);
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

	@Override
	public boolean sleep(Location location, boolean b) {
		return player.sleep(location, b);
	}

	@Override
	public void wakeup(boolean b) {
		player.wakeup(b);
	}

	@Override
	public Location getBedLocation() {
		return player.getBedLocation();
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

	@Override
	public PersistentDataContainer getPersistentDataContainer() {
		return player.getPersistentDataContainer();
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

	@Deprecated
	@Override
	public double getMaxHealth() {
		return player.getMaxHealth();
	}

	@Deprecated
	public void setItemInHand(ItemStack arg0) {
		player.setItemInHand(arg0);
	}

	public void setItemOnCursor(ItemStack arg0) {
		player.setItemOnCursor(arg0);
	}

	@Override
	public boolean hasCooldown(Material material) {
		return player.hasCooldown(material);
	}

	@Override
	public int getCooldown(Material material) {
		return player.getCooldown(material);
	}

	@Override
	public void setCooldown(Material material, int i) {
		player.setCooldown(material, i);
	}

	@Deprecated
	public void setLastDamage(int damage) {
		player.setLastDamage(damage);
	}

	public void setLastDamageCause(EntityDamageEvent event) {
		player.setLastDamageCause(event);
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

	@Deprecated
	public boolean setPassenger(Entity passenger) {
		return player.setPassenger(passenger);
	}

	@Override
	public List<Entity> getPassengers() {
		return player.getPassengers();
	}

	@Override
	public boolean addPassenger(Entity entity) {
		return player.addPassenger(entity);
	}

	@Override
	public boolean removePassenger(Entity entity) {
		return player.removePassenger(entity);
	}

	public void setPlayerListName(String name) {
		player.setPlayerListName(name);
	}

	@Override
	public String getPlayerListHeader() {
		return player.getPlayerListHeader();
	}

	@Override
	public String getPlayerListFooter() {
		return player.getPlayerListFooter();
	}

	@Override
	public void setPlayerListHeader(String s) {
		player.setPlayerListHeader(s);
	}

	@Override
	public void setPlayerListFooter(String s) {
		player.setPlayerListFooter(s);
	}

	@Override
	public void setPlayerListHeaderFooter(String s, String s1) {
		player.setPlayerListHeaderFooter(s, s1);
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

	@Override
	public void setResourcePack(String s, byte[] bytes) {
		player.setResourcePack(s, bytes);
	}

	public void setTicksLived(int value) {
		player.setTicksLived(value);
	}

	public void setTotalExperience(int exp) {
		player.setTotalExperience(exp);
	}

	@Override
	public void sendExperienceChange(float v) {
		player.sendExperienceChange(v);
	}

	@Override
	public void sendExperienceChange(float v, int i) {
		player.sendExperienceChange(v, i);
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
	public void showPlayer(Player player) {
		this.player.showPlayer(player);
	}

	@Override
	public void showPlayer(Plugin plugin, Player player) {
		this.player.showPlayer(plugin, player);
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

	@Override
	public double getAbsorptionAmount() {
		return player.getAbsorptionAmount();
	}

	@Override
	public void setAbsorptionAmount(double v) {
		player.setAbsorptionAmount(v);
	}

	public void setLastDamage(double arg0) {
		player.setLastDamage(arg0);
	}

	@Deprecated
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

	@Override
	public void playSound(Location location, Sound sound, SoundCategory soundCategory, float v, float v1) {
		player.playSound(location, sound, soundCategory, v, v1);
	}

	@Override
	public void playSound(Location location, String s, SoundCategory soundCategory, float v, float v1) {
		player.playSound(location, s, soundCategory, v, v1);
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

	@Override
	public void sendSignChange(Location location, String[] strings, DyeColor dyeColor) throws IllegalArgumentException {
		player.sendSignChange(location, strings, dyeColor);
	}

	@Override
	@Deprecated
	public void resetTitle() {
		player.resetTitle();
	}

	@Override
	@Deprecated
	public void sendTitle(String s, String s1) {
		player.sendTitle(s, s1);
	}

	@Override
	public void sendTitle(String s, String s1, int i, int i1, int i2) {
		player.sendTitle(s, s1, i, i1, i2);
	}

	@Override
	public Entity getSpectatorTarget() {
		return player.getSpectatorTarget();
	}

	@Override
	public void setSpectatorTarget(Entity entity) {
		player.setSpectatorTarget(entity);
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int i) {
		player.spawnParticle(particle, location, i);
	}

	@Override
	public void spawnParticle(Particle particle, double v, double v1, double v2, int i) {
		player.spawnParticle(particle, v, v1, v2, i);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int i, T t) {
		player.spawnParticle(particle, location, i, t);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, T t) {
		player.spawnParticle(particle, v, v1, v2, i, t);
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2) {
		player.spawnParticle(particle, location, i, v, v1, v2);
	}

	@Override
	public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
		player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, T t) {
		player.spawnParticle(particle, location, i, v, v1, v2, t);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, T t) {
		player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3) {
		player.spawnParticle(particle, location, i, v, v1, v2, v3);
	}

	@Override
	public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
		player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3, T t) {
		player.spawnParticle(particle, location, i, v, v1, v2, v3, t);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, T t) {
		player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
	}

	@Override
	public AdvancementProgress getAdvancementProgress(Advancement advancement) {
		return player.getAdvancementProgress(advancement);
	}

	@Override
	public int getClientViewDistance() {
		return player.getClientViewDistance();
	}

	@Override
	public String getLocale() {
		return player.getLocale();
	}

	@Override
	public void updateCommands() {
		player.updateCommands();
	}

	@Override
	public void openBook(ItemStack itemStack) {
		player.openBook(itemStack);
	}

	@Override
	public Spigot spigot() {
		return player.spigot();
	}

	@Override
	public InventoryView openMerchant(Villager villager, boolean b) {
		return player.openMerchant(villager, b);
	}

	@Override
	public InventoryView openMerchant(Merchant merchant, boolean b) {
		return player.openMerchant(merchant, b);
	}

	@Override
	public MainHand getMainHand() {
		return player.getMainHand();
	}

	@Override
	public boolean isGliding() {
		return player.isGliding();
	}

	@Override
	public void setGliding(boolean b) {
		player.setGliding(b);
	}

	@Override
	public boolean isSwimming() {
		return player.isSwimming();
	}

	@Override
	public void setSwimming(boolean b) {
		player.setSwimming(b);
	}

	@Override
	public boolean isRiptiding() {
		return player.isRiptiding();
	}

	@Override
	public void setAI(boolean b) {
		player.setAI(b);
	}

	@Override
	public boolean hasAI() {
		return player.hasAI();
	}

	@Override
	public void setCollidable(boolean b) {
		player.setCollidable(b);
	}

	@Override
	public boolean isCollidable() {
		return player.isCollidable();
	}

	@Override
	public <T> T getMemory(MemoryKey<T> memoryKey) {
		return player.getMemory(memoryKey);
	}

	@Override
	public <T> void setMemory(MemoryKey<T> memoryKey, T t) {
		player.setMemory(memoryKey, t);
	}

	@Override
	public AttributeInstance getAttribute(Attribute attribute) {
		return player.getAttribute(attribute);
	}

	@Override
	public void setGlowing(boolean b) {
		player.setGlowing(b);
	}

	@Override
	public boolean isGlowing() {
		return player.isGlowing();
	}

	@Override
	public void setInvulnerable(boolean b) {
		player.setInvulnerable(b);
	}

	@Override
	public boolean isInvulnerable() {
		return player.isInvulnerable();
	}

	@Override
	public boolean isSilent() {
		return player.isSilent();
	}

	@Override
	public void setSilent(boolean b) {
		player.setSilent(b);
	}

	@Override
	public void stopSound(Sound sound) {
		player.stopSound(sound);
	}

	@Override
	public void stopSound(String s) {
		player.stopSound(s);
	}

	@Override
	public void stopSound(Sound sound, SoundCategory soundCategory) {
		player.stopSound(sound, soundCategory);
	}

	@Override
	public void stopSound(String s, SoundCategory soundCategory) {
		player.stopSound(s, soundCategory);
	}

	@Override
	public boolean hasGravity() {
		return player.hasGravity();
	}

	@Override
	public void setGravity(boolean b) {
		player.setGravity(b);
	}

	@Override
	public int getPortalCooldown() {
		return player.getPortalCooldown();
	}

	@Override
	public void setPortalCooldown(int i) {
		player.setPortalCooldown(i);
	}

	@Override
	public Set<String> getScoreboardTags() {
		return player.getScoreboardTags();
	}

	@Override
	public boolean addScoreboardTag(String s) {
		return player.addScoreboardTag(s);
	}

	@Override
	public boolean removeScoreboardTag(String s) {
		return player.removeScoreboardTag(s);
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction() {
		return player.getPistonMoveReaction();
	}

	@Override
	public BlockFace getFacing() {
		return player.getFacing();
	}

	@Override
	public Pose getPose() {
		return player.getPose();
	}
}
