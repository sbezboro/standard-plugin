package com.sbezboro.standardplugin.model;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
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
import org.bukkit.util.Vector;

import com.sbezboro.standardplugin.persistence.PersistedObject;
import com.sbezboro.standardplugin.persistence.PlayerStorage;

@SuppressWarnings("deprecation")
public abstract class PlayerDelegate extends PersistedObject implements Player {
	protected final Player base;

	public PlayerDelegate(final Player player, final PlayerStorage storage) {
		super(storage, player.getName());
		
		this.base = player;
	}

	public void abandonConversation(Conversation conversation,
			ConversationAbandonedEvent details) {
		base.abandonConversation(conversation, details);
	}

	public void abandonConversation(Conversation conversation) {
		base.abandonConversation(conversation);
	}

	public void acceptConversationInput(String input) {
		base.acceptConversationInput(input);
	}

	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return base.addAttachment(plugin, ticks);
	}

	public PermissionAttachment addAttachment(Plugin plugin, String name,
			boolean value, int ticks) {
		return base.addAttachment(plugin, name, value, ticks);
	}

	public PermissionAttachment addAttachment(Plugin plugin, String name,
			boolean value) {
		return base.addAttachment(plugin, name, value);
	}

	public PermissionAttachment addAttachment(Plugin plugin) {
		return base.addAttachment(plugin);
	}

	public boolean addPotionEffect(PotionEffect effect, boolean force) {
		return base.addPotionEffect(effect, force);
	}

	public boolean addPotionEffect(PotionEffect effect) {
		return base.addPotionEffect(effect);
	}

	public boolean addPotionEffects(Collection<PotionEffect> effects) {
		return base.addPotionEffects(effects);
	}

	public void awardAchievement(Achievement achievement) {
		base.awardAchievement(achievement);
	}

	public boolean beginConversation(Conversation conversation) {
		return base.beginConversation(conversation);
	}

	public boolean canSee(Player player) {
		return base.canSee(player);
	}

	public void chat(String msg) {
		base.chat(msg);
	}

	public void closeInventory() {
		base.closeInventory();
	}

	public void damage(int arg0, Entity arg1) {
		base.damage(arg0, arg1);
	}

	public void damage(int arg0) {
		base.damage(arg0);
	}

	public boolean eject() {
		return base.eject();
	}

	public Collection<PotionEffect> getActivePotionEffects() {
		return base.getActivePotionEffects();
	}

	public InetSocketAddress getAddress() {
		return base.getAddress();
	}

	public boolean getAllowFlight() {
		return base.getAllowFlight();
	}

	public Location getBedSpawnLocation() {
		return base.getBedSpawnLocation();
	}

	public boolean getCanPickupItems() {
		return base.getCanPickupItems();
	}

	public Location getCompassTarget() {
		return base.getCompassTarget();
	}

	public String getCustomName() {
		return base.getCustomName();
	}

	public String getDisplayName() {
		return base.getDisplayName();
	}

	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return base.getEffectivePermissions();
	}

	public Inventory getEnderChest() {
		return base.getEnderChest();
	}

	public int getEntityId() {
		return base.getEntityId();
	}

	public EntityEquipment getEquipment() {
		return base.getEquipment();
	}

	public float getExhaustion() {
		return base.getExhaustion();
	}

	public float getExp() {
		return base.getExp();
	}

	public int getExpToLevel() {
		return base.getExpToLevel();
	}

	public double getEyeHeight() {
		return base.getEyeHeight();
	}

	public double getEyeHeight(boolean ignoreSneaking) {
		return base.getEyeHeight(ignoreSneaking);
	}

	public Location getEyeLocation() {
		return base.getEyeLocation();
	}

	public float getFallDistance() {
		return base.getFallDistance();
	}

	public int getFireTicks() {
		return base.getFireTicks();
	}

	public long getFirstPlayed() {
		return base.getFirstPlayed();
	}

	public float getFlySpeed() {
		return base.getFlySpeed();
	}

	public int getFoodLevel() {
		return base.getFoodLevel();
	}

	public GameMode getGameMode() {
		return base.getGameMode();
	}

	public int getHealth() {
		return base.getHealth();
	}

	public PlayerInventory getInventory() {
		return base.getInventory();
	}

	public ItemStack getItemInHand() {
		return base.getItemInHand();
	}

	public ItemStack getItemOnCursor() {
		return base.getItemOnCursor();
	}

	public Player getKiller() {
		return base.getKiller();
	}

	public int getLastDamage() {
		return base.getLastDamage();
	}

	public EntityDamageEvent getLastDamageCause() {
		return base.getLastDamageCause();
	}

	public long getLastPlayed() {
		return base.getLastPlayed();
	}

	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent,
			int maxDistance) {
		return base.getLastTwoTargetBlocks(transparent, maxDistance);
	}

	public int getLevel() {
		return base.getLevel();
	}

	public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
		return base.getLineOfSight(transparent, maxDistance);
	}

	public Set<String> getListeningPluginChannels() {
		return base.getListeningPluginChannels();
	}

	public Location getLocation() {
		return base.getLocation();
	}

	public Location getLocation(Location arg0) {
		return base.getLocation(arg0);
	}

	public int getMaxFireTicks() {
		return base.getMaxFireTicks();
	}

	public int getMaxHealth() {
		return base.getMaxHealth();
	}

	public int getMaximumAir() {
		return base.getMaximumAir();
	}

	public int getMaximumNoDamageTicks() {
		return base.getMaximumNoDamageTicks();
	}

	public List<MetadataValue> getMetadata(String arg0) {
		return base.getMetadata(arg0);
	}

	public String getName() {
		return base.getName();
	}

	public List<Entity> getNearbyEntities(double arg0, double arg1, double arg2) {
		return base.getNearbyEntities(arg0, arg1, arg2);
	}

	public int getNoDamageTicks() {
		return base.getNoDamageTicks();
	}

	public InventoryView getOpenInventory() {
		return base.getOpenInventory();
	}

	public Entity getPassenger() {
		return base.getPassenger();
	}

	public Player getPlayer() {
		return base.getPlayer();
	}

	public String getPlayerListName() {
		return base.getPlayerListName();
	}

	public long getPlayerTime() {
		return base.getPlayerTime();
	}

	public long getPlayerTimeOffset() {
		return base.getPlayerTimeOffset();
	}

	public WeatherType getPlayerWeather() {
		return base.getPlayerWeather();
	}

	public int getRemainingAir() {
		return base.getRemainingAir();
	}

	public boolean getRemoveWhenFarAway() {
		return base.getRemoveWhenFarAway();
	}

	public float getSaturation() {
		return base.getSaturation();
	}

	public Server getServer() {
		return base.getServer();
	}

	public int getSleepTicks() {
		return base.getSleepTicks();
	}

	public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
		return base.getTargetBlock(transparent, maxDistance);
	}

	public int getTicksLived() {
		return base.getTicksLived();
	}

	public int getTotalExperience() {
		return base.getTotalExperience();
	}

	public EntityType getType() {
		return base.getType();
	}

	public UUID getUniqueId() {
		return base.getUniqueId();
	}

	public Entity getVehicle() {
		return base.getVehicle();
	}

	public Vector getVelocity() {
		return base.getVelocity();
	}

	public float getWalkSpeed() {
		return base.getWalkSpeed();
	}

	public World getWorld() {
		return base.getWorld();
	}

	public void giveExp(int amount) {
		base.giveExp(amount);
	}

	public void giveExpLevels(int amount) {
		base.giveExpLevels(amount);
	}

	public boolean hasLineOfSight(Entity other) {
		return base.hasLineOfSight(other);
	}

	public boolean hasMetadata(String arg0) {
		return base.hasMetadata(arg0);
	}

	public boolean hasPermission(Permission perm) {
		return base.hasPermission(perm);
	}

	public boolean hasPermission(String name) {
		return base.hasPermission(name);
	}

	public boolean hasPlayedBefore() {
		return base.hasPlayedBefore();
	}

	public boolean hasPotionEffect(PotionEffectType type) {
		return base.hasPotionEffect(type);
	}

	public void hidePlayer(Player player) {
		base.hidePlayer(player);
	}

	public void incrementStatistic(Statistic statistic, int amount) {
		base.incrementStatistic(statistic, amount);
	}

	public void incrementStatistic(Statistic statistic, Material material,
			int amount) {
		base.incrementStatistic(statistic, material, amount);
	}

	public void incrementStatistic(Statistic statistic, Material material) {
		base.incrementStatistic(statistic, material);
	}

	public void incrementStatistic(Statistic statistic) {
		base.incrementStatistic(statistic);
	}

	public boolean isBanned() {
		return base.isBanned();
	}

	public boolean isBlocking() {
		return base.isBlocking();
	}

	public boolean isConversing() {
		return base.isConversing();
	}

	public boolean isCustomNameVisible() {
		return base.isCustomNameVisible();
	}

	public boolean isDead() {
		return base.isDead();
	}

	public boolean isEmpty() {
		return base.isEmpty();
	}

	public boolean isFlying() {
		return base.isFlying();
	}

	public boolean isInsideVehicle() {
		return base.isInsideVehicle();
	}

	public boolean isOnGround() {
		return base.isOnGround();
	}

	public boolean isOnline() {
		return base.isOnline();
	}

	public boolean isOp() {
		return base.isOp();
	}

	public boolean isPermissionSet(Permission perm) {
		return base.isPermissionSet(perm);
	}

	public boolean isPermissionSet(String name) {
		return base.isPermissionSet(name);
	}

	public boolean isPlayerTimeRelative() {
		return base.isPlayerTimeRelative();
	}

	public boolean isSleeping() {
		return base.isSleeping();
	}

	public boolean isSleepingIgnored() {
		return base.isSleepingIgnored();
	}

	public boolean isSneaking() {
		return base.isSneaking();
	}

	public boolean isSprinting() {
		return base.isSprinting();
	}

	public boolean isValid() {
		return base.isValid();
	}

	public boolean isWhitelisted() {
		return base.isWhitelisted();
	}

	public void kickPlayer(String message) {
		base.kickPlayer(message);
	}

	public <T extends Projectile> T launchProjectile(
			Class<? extends T> projectile) {
		return base.launchProjectile(projectile);
	}

	public boolean leaveVehicle() {
		return base.leaveVehicle();
	}

	public void loadData() {
		base.loadData();
	}

	public InventoryView openEnchanting(Location arg0, boolean arg1) {
		return base.openEnchanting(arg0, arg1);
	}

	public InventoryView openInventory(Inventory arg0) {
		return base.openInventory(arg0);
	}

	public void openInventory(InventoryView arg0) {
		base.openInventory(arg0);
	}

	public InventoryView openWorkbench(Location arg0, boolean arg1) {
		return base.openWorkbench(arg0, arg1);
	}

	public boolean performCommand(String command) {
		return base.performCommand(command);
	}

	public void playEffect(EntityEffect arg0) {
		base.playEffect(arg0);
	}

	public void playEffect(Location loc, Effect effect, int data) {
		base.playEffect(loc, effect, data);
	}

	public <T> void playEffect(Location loc, Effect effect, T data) {
		base.playEffect(loc, effect, data);
	}

	public void playNote(Location loc, byte instrument, byte note) {
		base.playNote(loc, instrument, note);
	}

	public void playNote(Location loc, Instrument instrument, Note note) {
		base.playNote(loc, instrument, note);
	}

	public void playSound(Location location, Sound sound, float volume,
			float pitch) {
		base.playSound(location, sound, volume, pitch);
	}

	public void recalculatePermissions() {
		base.recalculatePermissions();
	}

	public void remove() {
		base.remove();
	}

	public void removeAttachment(PermissionAttachment attachment) {
		base.removeAttachment(attachment);
	}

	public void removeMetadata(String arg0, Plugin arg1) {
		base.removeMetadata(arg0, arg1);
	}

	public void removePotionEffect(PotionEffectType type) {
		base.removePotionEffect(type);
	}

	public void resetMaxHealth() {
		base.resetMaxHealth();
	}

	public void resetPlayerTime() {
		base.resetPlayerTime();
	}

	public void resetPlayerWeather() {
		base.resetPlayerWeather();
	}

	public void saveData() {
		base.saveData();
	}

	public void sendBlockChange(Location loc, int material, byte data) {
		base.sendBlockChange(loc, material, data);
	}

	public void sendBlockChange(Location loc, Material material, byte data) {
		base.sendBlockChange(loc, material, data);
	}

	public boolean sendChunkChange(Location loc, int sx, int sy, int sz,
			byte[] data) {
		return base.sendChunkChange(loc, sx, sy, sz, data);
	}

	public void sendMap(MapView map) {
		base.sendMap(map);
	}

	public void sendMessage(String arg0) {
		base.sendMessage(arg0);
	}

	public void sendMessage(String[] arg0) {
		base.sendMessage(arg0);
	}

	public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
		base.sendPluginMessage(arg0, arg1, arg2);
	}

	public void sendRawMessage(String message) {
		base.sendRawMessage(message);
	}

	public Map<String, Object> serialize() {
		return base.serialize();
	}

	public void setAllowFlight(boolean flight) {
		base.setAllowFlight(flight);
	}

	public void setBanned(boolean arg0) {
		base.setBanned(arg0);
	}

	public void setBedSpawnLocation(Location location, boolean force) {
		base.setBedSpawnLocation(location, force);
	}

	public void setBedSpawnLocation(Location location) {
		base.setBedSpawnLocation(location);
	}

	public void setCanPickupItems(boolean pickup) {
		base.setCanPickupItems(pickup);
	}

	public void setCompassTarget(Location loc) {
		base.setCompassTarget(loc);
	}

	public void setCustomName(String arg0) {
		base.setCustomName(arg0);
	}

	public void setCustomNameVisible(boolean arg0) {
		base.setCustomNameVisible(arg0);
	}

	public void setDisplayName(String name) {
		base.setDisplayName(name);
	}

	public void setExhaustion(float value) {
		base.setExhaustion(value);
	}

	public void setExp(float exp) {
		base.setExp(exp);
	}

	public void setFallDistance(float arg0) {
		base.setFallDistance(arg0);
	}

	public void setFireTicks(int arg0) {
		base.setFireTicks(arg0);
	}

	public void setFlySpeed(float value) throws IllegalArgumentException {
		base.setFlySpeed(value);
	}

	public void setFlying(boolean value) {
		base.setFlying(value);
	}

	public void setFoodLevel(int value) {
		base.setFoodLevel(value);
	}

	public void setGameMode(GameMode arg0) {
		base.setGameMode(arg0);
	}

	public void setHealth(int arg0) {
		base.setHealth(arg0);
	}

	public void setItemInHand(ItemStack arg0) {
		base.setItemInHand(arg0);
	}

	public void setItemOnCursor(ItemStack arg0) {
		base.setItemOnCursor(arg0);
	}

	public void setLastDamage(int damage) {
		base.setLastDamage(damage);
	}

	public void setLastDamageCause(EntityDamageEvent arg0) {
		base.setLastDamageCause(arg0);
	}

	public void setLevel(int level) {
		base.setLevel(level);
	}

	public void setMaxHealth(int arg0) {
		base.setMaxHealth(arg0);
	}

	public void setMaximumAir(int ticks) {
		base.setMaximumAir(ticks);
	}

	public void setMaximumNoDamageTicks(int ticks) {
		base.setMaximumNoDamageTicks(ticks);
	}

	public void setMetadata(String arg0, MetadataValue arg1) {
		base.setMetadata(arg0, arg1);
	}

	public void setNoDamageTicks(int ticks) {
		base.setNoDamageTicks(ticks);
	}

	public void setOp(boolean arg0) {
		base.setOp(arg0);
	}

	public boolean setPassenger(Entity arg0) {
		return base.setPassenger(arg0);
	}

	public void setPlayerListName(String name) {
		base.setPlayerListName(name);
	}

	public void setPlayerTime(long time, boolean relative) {
		base.setPlayerTime(time, relative);
	}

	public void setPlayerWeather(WeatherType arg0) {
		base.setPlayerWeather(arg0);
	}

	public void setRemainingAir(int ticks) {
		base.setRemainingAir(ticks);
	}

	public void setRemoveWhenFarAway(boolean remove) {
		base.setRemoveWhenFarAway(remove);
	}

	public void setSaturation(float value) {
		base.setSaturation(value);
	}

	public void setSleepingIgnored(boolean isSleeping) {
		base.setSleepingIgnored(isSleeping);
	}

	public void setSneaking(boolean sneak) {
		base.setSneaking(sneak);
	}

	public void setSprinting(boolean sprinting) {
		base.setSprinting(sprinting);
	}

	public void setTexturePack(String url) {
		base.setTexturePack(url);
	}

	public void setTicksLived(int arg0) {
		base.setTicksLived(arg0);
	}

	public void setTotalExperience(int exp) {
		base.setTotalExperience(exp);
	}

	public void setVelocity(Vector arg0) {
		base.setVelocity(arg0);
	}

	public void setWalkSpeed(float value) throws IllegalArgumentException {
		base.setWalkSpeed(value);
	}

	public void setWhitelisted(boolean arg0) {
		base.setWhitelisted(arg0);
	}

	public boolean setWindowProperty(Property arg0, int arg1) {
		return base.setWindowProperty(arg0, arg1);
	}

	public Arrow shootArrow() {
		return base.shootArrow();
	}

	public void showPlayer(Player player) {
		base.showPlayer(player);
	}

	public boolean teleport(Entity arg0, TeleportCause arg1) {
		return base.teleport(arg0, arg1);
	}

	public boolean teleport(Entity arg0) {
		return base.teleport(arg0);
	}

	public boolean teleport(Location arg0, TeleportCause arg1) {
		return base.teleport(arg0, arg1);
	}

	public boolean teleport(Location arg0) {
		return base.teleport(arg0);
	}

	public Egg throwEgg() {
		return base.throwEgg();
	}

	public Snowball throwSnowball() {
		return base.throwSnowball();
	}

	public void updateInventory() {
		base.updateInventory();
	}
	
}
