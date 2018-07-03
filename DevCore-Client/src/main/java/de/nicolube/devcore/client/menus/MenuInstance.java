/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Nico Lube
 */
public class MenuInstance extends Menu {

    private Inventory inventory;
    private final Player player;

    public MenuInstance(Player player, Menu menu) {
        super(menu.getSize() / 9, menu.getTitel(), menu.getContent());
        this.player = player;
        createMenu();
        player.openInventory(inventory);
    }

    public final void createMenu() {
        this.inventory = Bukkit.createInventory(null, size, title);
        for (int i = 0; i < this.content.length; i++) {
            inventory.setItem(i, this.content[i].getItemStack().clone());
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        MenuItem item = this.content[event.getSlot()];
        if (event.getInventory() != inventory) {
            InventoryAction action = event.getAction();
            if (action == InventoryAction.COLLECT_TO_CURSOR || action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        if (item == null) {
            return;
        }
        if (item instanceof ExecutableMenuItem) {
            if (((ExecutableMenuItem) item).execute(player)) {
                player.closeInventory();
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}
