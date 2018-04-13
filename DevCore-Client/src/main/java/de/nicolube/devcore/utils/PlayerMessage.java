/*
 * Copyright (C) 2018 Nico Lube
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.nicolube.devcore.utils;

import de.nicolube.devcore.ModuleBase;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Nico Lube
 */
public enum PlayerMessage {

    CHAT,
    MONEY,
    SYSTEM,
    PLOT,
    COINS,
    PING,
    MSG,
    TPA,
    ONLINE,
    PAY;
    

    private final String prefix;

    private PlayerMessage() {
        SystemMessage.DEBUG.send(getClass().getName()+" - Init: " + name());
        String tempPrefix;
        try {
            tempPrefix = ModuleBase.messages.getString("chatPrefixes." + name().toLowerCase());
        } catch (Exception e) {
            tempPrefix = "&c&lError";
            SystemMessage.ERROR.send(getClass().getName()+" - Messages: chatPrefixes." + name().toLowerCase() + "not found!");
        }
        this.prefix = tempPrefix;
    }
    
    public void send(CommandSender sender, String msg) {
        String out = ChatColor.translateAlternateColorCodes('&', prefix+msg);
        if (!(sender instanceof Player)) {
            out = ChatColor.stripColor(out);
        }
        sender.sendMessage(out);
    }
    
    public void sendNoPerm(CommandSender sender) {
        send(sender, ModuleBase.messages.getString("noPermission", "&4You do not have permission for that!"));
    }
}
