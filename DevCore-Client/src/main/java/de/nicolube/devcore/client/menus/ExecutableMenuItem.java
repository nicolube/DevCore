/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.menus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Nico Lube
 */
public abstract class ExecutableMenuItem extends MenuItem {

    public ExecutableMenuItem(ItemStack itemStack, String permission) {
        super(itemStack, permission);
    }

    public ExecutableMenuItem(MenuItem item) {
        super(item.getItemStack(), item.getPermission());
    }
    
    
    
    abstract boolean execute(Player player);
    
    
}
