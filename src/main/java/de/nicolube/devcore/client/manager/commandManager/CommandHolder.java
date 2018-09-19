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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nico Lube
 */
public class CommandHolder {

    private Map<String, Command> commandList = new HashMap<>();
    public void onEnable() {
        
    }
    
    public final void addCommand(Command command) {
        commandList.put(command.getName(), command);
    }

    public Map<String, Command> getCommandList() {
        return commandList;
    }
}
