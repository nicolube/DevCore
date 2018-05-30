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
package de.nicolube.devcore.utils;

/**
 *
 * @author Nico Lube
 */
public enum SystemMessage {
    
    INFO,
    WARN,
    ERROR,
    NORMAL,
    DEBUG,
    ALL;
    
    public static SystemMessage logLevel = NORMAL;
    private String prefix;
    
    private SystemMessage() {
        this.prefix = "[DevCore] ["+name()+"] ";
    }
    
    public void send(String message) {
        if (ordinal() <= logLevel.ordinal())
        System.out.println(prefix+message);
    }

    public static void setLogLevel(SystemMessage logLevel) {
        SystemMessage.logLevel = logLevel;
    }

    public static SystemMessage getLogLevel() {
        return logLevel;
    }
}
