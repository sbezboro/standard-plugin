package com.sbezboro.standardplugin.listeners;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.events.CraftEvent;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CraftListener extends EventListener implements Listener {
    @SuppressWarnings("serial")
    private static final HashMap<Material,Integer> REPORT_OUTPUTS = new HashMap<Material,Integer>() {{
        // This map allows us to only track certain crafting recipes that
        // are particularly valuable or special.
        // It is essential that the recipes in this map are irreversible
        // ones, otherwise players can craft back and forth forever and
        // the leader board is meaningless.
        // For netherite ingots, they can be converted back and forth
        // to netherite blocks, but that recipe yields 9 ingots, so here
        // we only include the recipe that yields 1 ingot, because that
        // recipe is irreversible.
        put(Material.NETHERITE_INGOT, 1);
        put(Material.TNT, 1);
        put(Material.FIREWORK_ROCKET, 3);
        put(Material.SHULKER_BOX, 1);
        put(Material.ANVIL, 1);
        put(Material.GOLDEN_APPLE, 1);
        put(Material.ENDER_CHEST, 1);
        // Add more based on player demand
    }};

    public CraftListener(StandardPlugin plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled=true)
    public void onCraftItem(CraftItemEvent event) {
        // Only do this logic when the feature is enabled
        if (! plugin.getRecordCrafting()) return;

        ItemStack output = event.getCurrentItem();
        Material itemType = output.getType();
        if (! REPORT_OUTPUTS.containsKey(itemType) || REPORT_OUTPUTS.get(itemType) != output.getAmount()) {
            // This is not a recipe we are reporting
            return;
        }

        int multiple = 0;
        InventoryAction action = event.getAction();
        switch (action) {
        case NOTHING:
        case HOTBAR_MOVE_AND_READD:
            // These actions do occur in practice when crafting, but result
            // in nothing actually happening.
            // NOTHING: If the cursor cannot pick up the craft output because it's
            // a different item type or the combination of the stack in the cursor
            // plus the craft output would exceed a full stack.
            // HOTBAR_MOVE_AND_READD: If the player tries to put the craft output
            // on the hotbar directly, and an item already exists in that slot.
            multiple = 0;
            break;
        case PICKUP_ALL:
        case PICKUP_HALF:
        case HOTBAR_SWAP:
            // These actions all result in the craft recipe being produced exactly
            // once.
            // PICKUP_ALL: When left clicking on the craft output and able to pick up
            // the whole stack
            // PICKUP_HALF: When right clicking on the craft output and able to pick up
            // the whole stack. You can never get half of a craft output so the action
            // end up being same as left click.
            // HOTBAR_SWAP: When moving the craft output directly into an empty hotbar
            // slot.
            multiple = 1;
            break;
        case MOVE_TO_OTHER_INVENTORY:
            // This action occurs when the player shift-clicks on the craft output.
            // This causes the craft recipe to be produced as many times as possible.
            // The number of times is the lesser of:
            // 1) The amount of material in the crafting grid
            // 2) The amount of free storage space in the player's inventory
            // If there is a fractional amount of space available in the player's
            // inventory, then round up to the next whole number of recipe copies.
            // For example, if the recipe produces 3 firework rockets, and the
            // inventory has space for 5 firework rockets, then as long as there is
            // enough crafting material in the grid for 2 copies of the recipe, the
            // craft action will produce 2 copies (5 / 3 rounded up) and add 6
            // firework rockets, overflowing the inventory and dropping 1 rocket
            // on the ground.

            // First scan the inventory and see how much space there is for items of
            // the current type.
            int spaceAvailable = 0;
            ItemStack[] storage = event.getView().getBottomInventory().getStorageContents();
            for (int i = 0; i < storage.length; i++) {
                if (null == storage[i]) {
                    spaceAvailable += output.getMaxStackSize();
                } else if (output.isSimilar(storage[i])) {
                    spaceAvailable += (output.getMaxStackSize() - storage[i].getAmount());
                }
            }
            // Divide the available space by the amount produced by each copy
            // (rounding up) to see how many time the recipe could execute.
            int canHoldCopies = (spaceAvailable + output.getAmount() - 1) / output.getAmount();

            // Examine the craft matrix for see how much material there is.
            // Because crafting recipes always take exactly 1 of each item
            // in the grid, and unused grid cells must be empty, then the
            // smallest item stack in the grid determines how many copies
            // of the recipe could be produced.
            ItemStack[] matrix = event.getInventory().getMatrix();
            int smallestAmount = 64;
            for (int i = 0; i < matrix.length; i++) {
                if (null != matrix[i]) {
                    int amount = matrix[i].getAmount();
                    if (amount < smallestAmount) {
                        smallestAmount = amount;
                    }
                }
            }

            // Take the minimum of the two values
            multiple = Math.min(smallestAmount, canHoldCopies);
            break;
        default:
            // The other actions can't occur when clicking on craft output slots.
            plugin.getLogger().warning("Unexpected action in CraftItemEvent: " + action);
            break;
        }

        if (0 == multiple) {
            // No point in reporting craft events with no output
            return;
        }

        StandardPlayer player = plugin.getStandardPlayer(event.getWhoClicked());

        CraftEvent ev = new CraftEvent(player, itemType.toString(), output.getAmount() * multiple);
        ev.log();
    }

    @EventHandler(ignoreCancelled=true)
    public void onClickInventory(InventoryClickEvent event) {
        // Only do this logic when the feature is enabled
        if (! plugin.getRecordSmithing()) return;

        if (InventoryType.SMITHING != event.getInventory().getType() ||
                InventoryType.SlotType.RESULT != event.getSlotType()) {
            // We're only interested in clicks on the result slot of a
            // smithing table.
            return;
        }

        // There is no filtering of the item types here because smithing tables
        // can only produce netherite items and we're interested in recording
        // all netherite item crafting.

        InventoryAction action = event.getAction();
        switch (action) {
        case NOTHING:
        case HOTBAR_MOVE_AND_READD:
        case PLACE_ALL:
        case PLACE_ONE:
            // These 4 actions result in nothing.
            return;
        case PICKUP_ALL:
        case PICKUP_HALF:
        case HOTBAR_SWAP:
            // These 3 actions result in the output of the smithing table
            // getting picked up and the input consumed.
            break;
        case MOVE_TO_OTHER_INVENTORY:
            // This action results in the output getting moved to the player's
            // inventory if there is space for it, otherwise nothing.
            boolean spaceAvailable = false;
            ItemStack[] storage = event.getView().getBottomInventory().getStorageContents();
            for (int i = 0; i < storage.length; i++) {
                if (null == storage[i]) {
                    spaceAvailable = true;
                    break;
                }
            }
            if (! spaceAvailable) {
                // No space, so the smithing attempt fails
                return;
            }
            break;
        default:
            // The other actions can't occur when clicking on smithing table
            // output slots.
            plugin.getLogger().warning("Unexpected action in InventoryClickEvent on smithing table output: " + action);
            return;
        }

        ItemStack output = event.getCurrentItem();
        Material itemType = output.getType();
        StandardPlayer player = plugin.getStandardPlayer(event.getWhoClicked());

        CraftEvent ev = new CraftEvent(player, itemType.toString(), 1);
        ev.log();
    }
}