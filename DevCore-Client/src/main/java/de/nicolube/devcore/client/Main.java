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

import de.nicolube.devcore.DevCore;
import de.nicolube.devcore.client.econemy.EconemyManager;
import de.nicolube.devcore.client.econemy.ModelAccount;
import de.nicolube.devcore.client.econemy.ModelBank;
import de.nicolube.devcore.client.manager.commandManager.CommandManager;
import de.nicolube.devcore.client.manager.config.ConfigManager;
import de.nicolube.devcore.client.playermanager.PlayerData;
import de.nicolube.devcore.client.playermanager.PlayerManager;
import de.nicolube.devcore.scoreboard.Scoreboards;
import de.nicolube.devcore.scoreboard.Tablist;
import de.nicolube.devcore.utils.SystemMessage;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Nico Lube
 */
public class Main extends JavaPlugin {

    private static Main plugin;
    private ConfigManager configManager;
    private CommandManager commandManager;
    private Scoreboards scoreboards;
    private Tablist tablist;
    private PlayerManager playerManager;
    private EconemyManager econemyManager;

    @Override
    public void onLoad() {
        super.onLoad();
        
        SystemMessage.setLogLevel(SystemMessage.ALL);
        logInfoStart();
        
        SystemMessage.INFO.send("Init Databases");
        try {
            getDatabase().createQuery(PlayerData.class).findRowCount();
            getDatabase().createQuery(ModelAccount.class).findRowCount();
            getDatabase().createQuery(ModelBank.class).findRowCount();
        } catch (Exception e) {
            installDDL();
        }

        SystemMessage.INFO.send("Starting player manager");
        this.playerManager = new PlayerManager(plugin);
        DevCore.setPlayerManager(playerManager);
        
        SystemMessage.INFO.send("Starting econemy manager");
        this.econemyManager = new EconemyManager();
        DevCore.setEconemyManager(econemyManager);
        
    }

    
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.plugin = this;
        
        logInfoStart();
        
        SystemMessage.INFO.send("Starting ConfigManager");
        this.configManager = new ConfigManager(this);
        this.configManager.addConfig("config");
        
        SystemMessage.INFO.send("Starting ServerPinger");
        DevCore.setServerPinger(new ServerPingerCliant(plugin));
        
        SystemMessage.INFO.send("Starting CommandManager");
        this.commandManager = new CommandManager();
        
        SystemMessage.INFO.send("Register listener for econemy manager");
        Bukkit.getPluginManager().registerEvents(econemyManager, this);
        
        SystemMessage.INFO.send("Register listener for player manager");
        Bukkit.getPluginManager().registerEvents(playerManager, this);
        
        SystemMessage.INFO.send("Starting TabList");
        this.tablist = new Tablist();
        Bukkit.getPluginManager().registerEvents(tablist, this);
        
        if (getConfig().getBoolean("scoreboard.enable")) {
            SystemMessage.INFO.send("Starting Scorebaords");
            this.scoreboards = new Scoreboards();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        DevCore.onDisable();
    }

    public static Main getPlugin() {
        return plugin;
    }

    private void logInfoStart() {
        SystemMessage.INFO.send("=======================<>======================");
        SystemMessage.INFO.send("");
        SystemMessage.INFO.send("=============<DevCore by nicolube>=============");
        SystemMessage.INFO.send("");
        SystemMessage.INFO.send("=======================<>======================");
        SystemMessage.INFO.send("");
        SystemMessage.INFO.send("Starting... DEBUG Level: " + SystemMessage.getLogLevel());
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Scoreboards getScoreboards() {
        return scoreboards;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Tablist getTablist() {
        return tablist;
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        return Arrays.asList(PlayerData.class, ModelBank.class, ModelAccount.class);
    }

}
