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
package de.nicolube.devcore.client.manager.commandManager;

import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Nico Lube
 */
public abstract interface CommandBase
{
  public abstract boolean run(CommandSender sender, String alias, String[] args);
  
  public abstract void onEnable();
  
  public abstract void addSubCommand(SubCommand paramSubCommand);
  
  public abstract SubCommand getNextCompleter(String paramString);
  
  public abstract List<String> getComplets();
  
  public abstract List<String> getComplets(String paramString);
  
  public abstract String getDescription();
  
  public abstract String getName();
  
  public abstract List<String> getAliases();
  
  public abstract Map<String, SubCommand> getSubCommands();
  
  public abstract Map<String, SubCommand> getSubCommandsAliases();
  
  public abstract String getPermission();
  
  public abstract String getAdvanceName();
  
  public abstract String getUsageMessage();
}
