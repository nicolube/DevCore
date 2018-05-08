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

import de.nicolube.devcore.LoadClass;
import de.nicolube.devcore.ModuleBase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Nico Lube
 */
public class PlayerUtils {
    
    
    public static Player getPlayer(String name, CommandSender sender) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            return player;
        }
        PlayerMessage.SYSTEM.send(sender, ModuleBase.messages.getString("player.notFound", "Player not found!"));
        return null;
    }
    
    
}
