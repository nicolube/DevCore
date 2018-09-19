/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.menus;

import de.nicolube.devcore.LoadClass;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 *
 * @author Nico Lube
 */
public class Menu implements Cloneable, Serializable, LoadClass {

    protected MenuItem[] content;
    protected String title;
    protected int size;

    private transient HashMap<UUID, MenuInstance> runningMenus;
    private transient File file;

    public Menu(int rows, String titel, MenuItem[] content) {
        this.content = content;
        this.title = titel;
        this.size = rows * 9;
    }

    @Override
    public void load() {
        runningMenus = new HashMap<>();
    }

    public MenuInstance createInstance(Player player) {
        return new MenuInstance(player, this);
    }

    public void onInventoryClick(InventoryClickEvent event) {
        MenuInstance menuInstance = runningMenus.get(event.getWhoClicked().getUniqueId());
        if (menuInstance != null) {
            menuInstance.onInventoryClick(event);
        }
    }

    public String getTitel() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public MenuItem[] getContent() {
        return content;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public MenuInstance open(Player player) {
        MenuInstance instance = new MenuInstance(player, this);
        runningMenus.put(player.getUniqueId(), instance);
        return instance; 
    }
}
