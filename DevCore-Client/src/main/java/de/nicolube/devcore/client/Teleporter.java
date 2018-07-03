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
package de.nicolube.devcore.client;

import de.nicolube.devcore.client.utils.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Nico Lube
 */
public class Teleporter {

    private static FileConfiguration messages;
    private static Main plugin;
    private static int delay;

    public Teleporter(Main plugin) {
        this.plugin = plugin;
        this.messages = plugin.getConfigManager().getConfig("messages");
        this.delay = plugin.getConfigManager().getConfig("config").getInt("double");

    }

    public static void tepelort(Player player, Location location, boolean hasDelay, String message) {
        if (hasDelay && !player.hasPermission("devcore.teleport.ignoredelay")) {
            PlayerMessage.SYSTEM.send(player, messages.getString("teleport.delay"));
            Location playersLocation = player.getLocation();
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                if (player.isOnline()) {
                    return;
                }
                if (playersLocation.distance(player.getLocation()) > 0.5) {
                    PlayerMessage.SYSTEM.send(player, messages.getString("teleport.cancel"));
                    return;
                }
                teleport(player, location, message);
            }, delay * 20);
            return;
        }
        teleport(player, location, message);
    }

    private static void teleport(Player player, Location location, String message) {
        player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
        if (message != null) {
            PlayerMessage.SYSTEM.send(player, message);
        }
    }
}
