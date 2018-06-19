/*
 * Copyright (C) 2018 Owner
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
package de.nicolube.devcore.client.manager.playerManager;

import de.nicolube.devcore.client.Main;
import de.nicolube.devcore.client.events.PlayerManagerRegisterEvent;
import de.nicolube.devcore.utils.SystemMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Owner
 */
public class PlayerManager implements Listener {

    private Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private Main plugin;

    public PlayerManager(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        SystemMessage.DEBUG.send(event.getKickMessage());
        if (!event.getKickMessage().equals("")) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = event.getPlayer();
            String uuid = player.getUniqueId().toString();
            SystemMessage.DEBUG.send("Check DB from " + player.getName() + ", UUID: " + uuid);
            PlayerData playerData = this.plugin.getDatabase().find(PlayerData.class)
                    .where()
                    .eq("uuid", uuid)
                    .findUnique();
            SystemMessage.DEBUG.send("Is table null: " + (playerData == null));
            if (playerData == null) {
                playerData = new PlayerData(player, 0, 500, 0);
                plugin.getDatabase().insert(playerData);
            }

            playerDataMap.put(player.getUniqueId(), playerData);
            Bukkit.getPluginManager().callEvent(new PlayerManagerRegisterEvent(player, playerData));
        });
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        playerDataMap.remove(event.getPlayer().getUniqueId());
    }

    public void savePlayer(Player player) {
        plugin.getDatabase().update(playerDataMap.get(player.getUniqueId()));
    }

    public void savePlayer(UUID uuid) {
        plugin.getDatabase().update(playerDataMap.get(uuid));
    }

    public PlayerData getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    public PlayerData getPlayer(UUID uuid) {
        PlayerData playerData = playerDataMap.get(uuid);
        if (playerData != null) {
            return playerData;
        }
        playerData = this.plugin.getDatabase().find(PlayerData.class)
                .where()
                .eq("uuid", uuid)
                .findUnique();
        return playerData;
    }

}
