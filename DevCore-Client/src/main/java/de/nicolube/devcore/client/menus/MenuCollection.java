/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.menus;

import de.nicolube.devcore.LoadClass;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Is is a Class the hold a List of Menus
 * With the option that you can switch between them
 * 
 * @author Nico Lube
 */
public class MenuCollection implements Cloneable, Serializable, LoadClass {

    protected final LinkedList<Menu> menus;
    protected int standardMenu;

    protected int navigationRow;
    protected MenuItem[] navigation;

    private transient Map<UUID, MenuCollectionInstance> runningMenus;

    /**
     * Create a new MenuCollection 
     * 
     * @param menus Set a list of menues for the MenuCollection
     * @param standardMenu index of the standard menu in menus
     * @param navigationRow -1 where last row 0 the first 1 the secend...
     */
    public MenuCollection(LinkedList<Menu> menus, int standardMenu, int navigationRow) {
        this.menus = menus;
        this.standardMenu = standardMenu;
        this.navigationRow = navigationRow;
        load();
    }

    @Override
    public final void load() {
        runningMenus = new HashMap<>();
    }
    
    public MenuCollectionInstance open(Player player) {
        MenuCollectionInstance  instance = new MenuCollectionInstance(player, this);
       runningMenus.put(player.getUniqueId(), instance);
       return instance;
    }
    
    public void onInventoryClick(InventoryClickEvent event) {
        MenuCollectionInstance menuInstance = runningMenus.get(event.getWhoClicked().getUniqueId());
        if (menuInstance != null) menuInstance.onInventoryClick(event);
    }
    
    /**
     * Add a menu by pos to LinkedList menus.
     *
     * @param pos index of menus where sold be inserted
     * @param menu a menu to insert
     */
    public void addMenu(int pos, Menu menu) {
        menus.add(pos, menu);
    }

    /**
     * Add a menu to LinkedList menus.
     *
     * @param menu index of menu that sold be deleted
     */
    public void addMenu(Menu menu) {
        menus.add(menu);
    }

    /**
     * Delete a menu from LinkedList menus.
     *
     * @param pos index of menu that sold be deleted
     * @return true if menu exists
     */
    public boolean removeMenu(int pos) {
        return menus.remove(pos) != null;
    }

    /**
     * Delete a menu from LinkedList menus.
     *
     * @param menu that sold be deleted
     * @return true if menu exists
     */
    public boolean removeMenu(Menu menu) {
        return menus.remove(menu);
    }

    /**
     * Get a LinkedList of all menus.
     *
     * @return LinkedList with Menu
     */
    public LinkedList<Menu> getMenus() {
        return menus;
    }

    /**
     * Get the by index of LinkedList menus.
     *
     * @param i index of menus
     * @return @see Menu
     */
    public Menu getMenu(int i) {
        return menus.get(i);
    }

    /**
     * Set index of the LinkedList menues that standard open.
     *
     * @return index of menus
     */
    public int getStandardMenu() {
        return standardMenu;
    }

    /**
     * Set index of the LinkedList menues that standard open.
     *
     * @param standardMenu index of menus
     */
    public void setStandardMenu(int standardMenu) {
        this.standardMenu = standardMenu;
    }

    /**
     * Set line where the navigation bar is.
     *
     * @param navigationRow -1 where last row 0 the first 1 the secend...
     */
    public void setNavigationRow(int navigationRow) {
        this.navigationRow = navigationRow;
    }

    /**
     * Get line where the navigation bar is.
     *
     * @return -1 where last row 0 the first 1 the secend...
     */
    public int getNavigationRow() {
        return navigationRow;
    }
    
    /**
     * Get the navigation bar of the menu
     * notmaly it is a MenuItem Array with the lenth 9
     * 
     * @return the navigation bar
     */
    public MenuItem[] getNavigation() {
        return navigation;
    }
    
    

}
