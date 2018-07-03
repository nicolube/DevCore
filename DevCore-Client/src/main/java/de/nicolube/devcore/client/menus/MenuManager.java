/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.menus;

import de.nicolube.devcore.client.manager.config.ConfigManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Nico Lube
 */
public class MenuManager {

    private HashMap<String, Menu> menus;
    private HashMap<String, MenuCollection> menuCollections;
    private final ConfigManager menuCollectionConfs;
    private final ConfigManager menuConfs;
    private final List<ItemProcessor> itemProcessors;

    public MenuManager(JavaPlugin plugin) {
        this.menuConfs = new ConfigManager(plugin, new File(plugin.getDataFolder().getPath() + "/menu/menuCollections"));
        this.menuCollectionConfs = new ConfigManager(plugin, new File(plugin.getDataFolder().getPath() + "/menu/menus"));
        this.itemProcessors = new ArrayList<>();
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        this.menuCollections.forEach((n, m) -> m.onInventoryClick(event));
        this.menus.forEach((n, m) -> m.onInventoryClick(event));
    }

    public ConfigManager getMenuCollectionConfs() {
        return menuCollectionConfs;
    }
    
    public MenuItem loadItem(MenuItem item, ConfigurationSection section) {
        ItemConstructer constructer = new ItemConstructer(item, section);
        this.itemProcessors.forEach(p ->  p.itemProgessor(constructer));
        return constructer.getMenuItem();
    }
    
    public Menu getMenu(String menu) {
        if (menu == null) {
            return null;
        }
        return this.menus.get(menu);
    }

    public MenuCollection getMenuCollection(String menuCollection) {
        if (menuCollection == null) {
            return null;
        }
        return this.menuCollections.get(menuCollection);
    }
    
    public void addItemProgessor(ItemProcessor poItemProcessor) {
        this.itemProcessors.add(poItemProcessor);
    }
}
