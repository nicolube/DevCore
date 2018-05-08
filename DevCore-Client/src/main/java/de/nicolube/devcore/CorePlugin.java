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

import java.util.ArrayList;

/**
 *
 * @author Nico Lube
 */
public class CorePlugin {

    private final ArrayList<ModuleBase> registeredModules;
    private final ArrayList<LoadClass> registeredLoadClasses;

    public CorePlugin() {
        registeredModules = new ArrayList<>();
        registeredLoadClasses = new ArrayList<>();
    }

    public void addModule(ModuleBase module) {
        registeredModules.add(module);
    }

    public void addLoadClass(LoadClass loadClass) {
        registeredLoadClasses.add(loadClass);
    }
    
    public void load() {
        registeredModules.forEach(m -> {
            if (m instanceof LoadClass) 
                ((LoadClass) m).load();
        });
        registeredLoadClasses.forEach(lc -> lc.load());
    }
}
