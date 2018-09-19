/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.menus;

import java.io.Serializable;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Nico Lube
 */
public class MenuItem implements Serializable, Cloneable {
    private ItemStack itemStack;
    private String permission;

    public MenuItem(ItemStack itemStack, String permission) {
        this.itemStack = itemStack;
        this.permission = permission;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new MenuItem(itemStack.clone(), permission);
    }

    public String getPermission() {
        return permission;
    }
}
