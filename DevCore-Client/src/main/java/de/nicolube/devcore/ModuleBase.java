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
import de.nicolube.devcore.utils.SystemMessage;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Nico Lube
 */
public abstract class ModuleBase {
    
    public static Main plugin;
    public static Server server;
    public static FileConfiguration messages;
    
    static {
        SystemMessage.INFO.send("First load of ModulBase:");
        loadBase();
    }
    
    public static void loadBase() {
        SystemMessage.INFO.send("Loading BaseModule");
        plugin = Main.getPlugin();
        server = plugin.getServer();
        messages = plugin.getConfigManager().getConfig("messages");
    }
    
    
    
    
}
