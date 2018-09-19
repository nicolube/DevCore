/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Nico Lube
 */
public class MenuCollectionInstance extends MenuCollection {

    private MenuInstance menuInstance;
    private Player player;
    private int currentNavigationRow;

    public MenuCollectionInstance(Player player, MenuCollection menuCollection) {
        super(menuCollection.getMenus(), menuCollection.getStandardMenu(), menuCollection.getNavigationRow());
        this.player = player;
        openMenu(player, standardMenu);
    }

    public final void openMenu(Player player, int inventoryId) {
        this.menuInstance = menus.get(standardMenu).createInstance(player);
        this.currentNavigationRow = this.navigationRow;
        if (this.navigationRow == -1) {
            this.currentNavigationRow = (this.menuInstance.getSize() - this.navigationRow + 1) / 9;
        }
        Inventory inventory = this.menuInstance.getInventory();
        int startNum = this.currentNavigationRow * 9;
        for (int i = 0; i < navigation.length; i++) {
            inventory.setItem(i + startNum, navigation[i + startNum].getItemStack().clone());;
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() != menuInstance.getInventory()) {
            InventoryAction action = event.getAction();
            if (action == InventoryAction.COLLECT_TO_CURSOR || action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
            }
            return;
        }
        int slot = event.getSlot();
        if (slot / 9 == currentNavigationRow) {
            slot -= currentNavigationRow * 9;
            MenuItem menuItem = navigation[slot];
            if (menuItem instanceof ExecutableMenuItem);
            if (((ExecutableMenuItem) menuItem).execute(player)) {
                player.closeInventory();
            }
            return;
        }
        menuInstance.onInventoryClick(event);
    }

}
