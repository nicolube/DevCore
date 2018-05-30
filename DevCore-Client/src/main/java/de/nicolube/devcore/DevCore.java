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
package de.nicolube.devcore;

import de.nicolube.devcore.client.Main;
import de.nicolube.devcore.client.econemy.EconemyManager;
import de.nicolube.devcore.client.manager.commandManager.CommandManager;
import de.nicolube.devcore.client.playermanager.PlayerManager;
import de.nicolube.devcore.scoreboard.ScoreBoardUpdater;
import de.nicolube.devcore.utils.SystemMessage;
import java.util.HashMap;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Nico Lube
 */
public class DevCore {
    private static HashMap<String, CorePlugin> pluginList;
    private static PlayerManager playerManager;
    private static ServerPinger serverPinger;
    private static EconemyManager econemyManager;
    
    static {
        pluginList = new HashMap<>();
    }
    
    public static void onDisable() {
        pluginList = new HashMap<>();
    }
    
    public static void registerPlugin(Plugin plugin) {
        String name = plugin.getName();
        SystemMessage.INFO.send("Registered plugin: "+name);
        pluginList.put(name, new CorePlugin());
    }
    
    public static CommandManager getCommandManager() {
        return Main.getPlugin().getCommandManager();
    }
    
    public static void setScoreboardExecuter(ScoreBoardUpdater sbu) {
       Main.getPlugin().getScoreboards().setScoreBoardUpdater(sbu);
    }

    public static void setPlayerManager(PlayerManager playerManager) {
        DevCore.playerManager = playerManager;
    }
    
    public static PlayerManager getPlayerManager() {
        return playerManager;
    }

    public static void setServerPinger(ServerPinger serverPinger) {
        DevCore.serverPinger = serverPinger;
    }
    
    public static ServerPinger getServerPinger() {
        return serverPinger;
    }

    public static EconemyManager getEconemyManager() {
        return econemyManager;
    }

    public static void setEconemyManager(EconemyManager econemyManager) {
        DevCore.econemyManager = econemyManager;
    }
    
    
}
