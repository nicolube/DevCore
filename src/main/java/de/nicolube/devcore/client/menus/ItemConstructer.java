/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.menus;

import de.nicolube.devcore.client.utils.PlayerMessage;
import de.nicolube.devcore.utils.SystemMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author Nico Lube
 */
public class ItemConstructer {

    private MenuItem menuItem;
    private ConfigurationSection itemConfig;

    public ItemConstructer(MenuItem menuItem, ConfigurationSection itemConfig) {
        this.menuItem = menuItem;
        this.itemConfig = itemConfig;
    }

    public void loadItem(MenuManager manager) {
        menuItem = new MenuItem(menuItem.getItemStack(), itemConfig.getString("permission"));
        String type = itemConfig.getString("type");
        switch (type.toLowerCase()) {
            case "menu":
                menuItem = new ExecutableMenuItem(menuItem) {
                    @Override
                    boolean execute(Player player) {
                        Menu menu = manager.getMenu(itemConfig.getString("menu"));
                        if (menu == null) {
                            String menuName = itemConfig.getString("menu");
                            SystemMessage.WARN.send("Menu not found! Menu: " + menuName);
                            PlayerMessage.SYSTEM.send(player, "&4Error: &cMenu &7" + menuName + " &cnot found! Pls report this to the Support.");
                            return true;
                        }
                        menu.open(player);
                        return false;
                    }
                };
                break;
            case "menucollection":
                menuItem = new ExecutableMenuItem(menuItem) {
                    @Override
                    boolean execute(Player player) {
                        MenuCollection menuCollection = manager.getMenuCollection(itemConfig.getString("menuCollection"));
                        if (menuCollection == null) {
                            String menuName = itemConfig.getString("menu");
                            SystemMessage.WARN.send("Menu not found! MenuCellection: " + menuName);
                            PlayerMessage.SYSTEM.send(player, "&4Error: &cMenuCellection &7" + menuName + " &cnot found! Pls report this to the Support.");
                            return true;
                        }
                        menuCollection.open(player);
                        return false;
                    }
                };
                break;
        }
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}
