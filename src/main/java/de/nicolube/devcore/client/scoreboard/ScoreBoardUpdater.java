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
package de.nicolube.devcore.client.scoreboard;

import org.bukkit.entity.Player;

/**
 *
 * @author Owner
 */
public abstract class ScoreBoardUpdater {

    public Player player;
    
    public ScoreBoardUpdater() {
    }
    
    public ScoreBoardUpdater(Player player) {
        this.player = player;
    }

    public abstract void update();

    public abstract String replace(String string);

    public abstract ScoreBoardUpdater getNewScoreboard(Player player);
}
