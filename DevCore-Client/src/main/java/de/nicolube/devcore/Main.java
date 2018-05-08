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

import de.nicolube.devcore.manager.commandManager.CommandManager;
import de.nicolube.devcore.manager.config.ConfigManager;
import de.nicolube.devcore.utils.SystemMessage;
import de.nicolube.devcore.utils.Scheduler.Scheduler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Nico Lube
 */
public class Main extends JavaPlugin {
    
    private static Main plugin;
    private Scheduler scheduler;
    private ConfigManager configManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        super.onEnable();
        this.plugin = this;

        SystemMessage.setLogLevel(SystemMessage.DEBUG);
        logInfoStart();

        SystemMessage.INFO.send("Starting Scheduler");
        this.scheduler = new Scheduler();
        
        SystemMessage.INFO.send("Starting ConfigManager");
        this.configManager = new ConfigManager(getDataFolder());
        
        SystemMessage.INFO.send("Starting CommandManager");        
        this.commandManager = new CommandManager();
        
        
    }

    @Override
    public void onDisable() {
        super.onDisable();
        scheduler.onDisable();
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
        SystemMessage.INFO.send("Starting... DEBUG Level: "+SystemMessage.getLogLevel());
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
    
    
}
