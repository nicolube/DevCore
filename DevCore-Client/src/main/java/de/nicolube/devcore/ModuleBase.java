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


import de.nicolube.devcore.utils.Scheduler.Scheduler;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Nico Lube
 */
public class ModuleBase {
    
    public final static Main plugin;
    public final static Server server;
    public final static Scheduler scheduler;
    public final static FileConfiguration messages;
    
    static {
        plugin = Main.getPlugin();
        server = plugin.getServer();
        scheduler = plugin.getScheduler();
        messages = plugin.getConfigManager().getConfig("messages");
    }
}
