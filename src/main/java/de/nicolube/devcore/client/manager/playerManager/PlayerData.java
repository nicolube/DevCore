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
package de.nicolube.devcore.client.manager.playerManager;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import de.nicolube.devcore.client.Main;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Owner
 */

@Entity(name = "GlobalStats")
@Table(name = "GlobalStats")
public class PlayerData implements Serializable {

    @Id
    @Column(name = "id")
    private long id;

    @NotNull
    @NotEmpty
    @Column(length = 16)
    private String name;

    @NotNull
    @NotEmpty
    @Column(length = 36, unique = true)
    private String uuid;

    @NotNull
    @NotEmpty
    @Column(length = 16, name = "lastIP")
    private String lastip;

    @Column
    private long votecoins;

    @Column
    private double coins;

    @Column
    private long ontime;
    
    @Transient
    private long lastCheckt;

    public PlayerData() {
        lastCheckt = System.currentTimeMillis();
    }

    public PlayerData(Player player, long votecoins, double coins, long ontime) {
        this.name = player.getName();
        this.uuid = player.getUniqueId().toString();
        this.lastip = player.getAddress().getHostString();
        this.votecoins = votecoins;
        this.coins = coins;
        this.ontime = ontime;
        lastCheckt = System.currentTimeMillis();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
    
    public double getCoins() {
        checkUpdate();
        return coins;
    }
    
    public void addCoins(double coins) {
        checkUpdate();
        this.coins += coins;
        update();
    }
    
    public boolean takeCoins(double coins) {
        checkUpdate();
        coins = this.coins-coins;
        if (coins<0) {
            return false;
        }
        this.coins = coins;
        update();
        return true;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public String getLastip() {
        return lastip;
    }

    public void setLastip(String lastip) {
        this.lastip = lastip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOntime() {
        checkUpdate();
        return ontime;
    }

    public void setOntime(long ontime) {
        this.ontime = ontime;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(uuid));
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setVotecoins(long votecoins) {
        this.votecoins = votecoins;
    }

    public long getVotecoins() {
        checkUpdate();
        return votecoins;
    }
    
    private void checkUpdate() {
        long cm = System.currentTimeMillis();
        if (lastCheckt+100<cm) {
            PlayerData data = Main.getPlugin().getDatabase().find(PlayerData.class).where().eq("uuid", uuid).findUnique();
            this.ontime = data.ontime;
            this.votecoins = data.votecoins;
            this.coins = data.coins;
        }
    }
    
    public void update() {
        Main.getPlugin().getDatabase().update(this);
    }

    @Override
    public String toString() {
        return "PlayerData{" + "id=" + id + ", name=" + name + ", uuid=" + uuid + ", lastip=" + lastip + ", votecoins=" + votecoins + ", coins=" + coins + ", ontime=" + ontime + '}';
    }
}
