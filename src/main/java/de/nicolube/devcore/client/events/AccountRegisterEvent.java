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
package de.nicolube.devcore.client.events;

import de.nicolube.devcore.client.manager.econemy.ModelAccount;
import de.nicolube.devcore.client.manager.playerManager.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Nico Lube
 */
public class AccountRegisterEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    private  Player player;
    private ModelAccount account;

    public AccountRegisterEvent(Player player, ModelAccount account) {
        this.player = player;
        this.account = account;
    }
    
    public Player getPlayer() {
        return player;
    }

    public ModelAccount getAccount() {
        return account;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
