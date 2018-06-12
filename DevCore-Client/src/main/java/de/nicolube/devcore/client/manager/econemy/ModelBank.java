/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.manager.econemy;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import de.nicolube.devcore.DevCore;
import de.nicolube.devcore.client.Main;
import de.nicolube.devcore.client.events.AccountRegisterEvent;
import de.nicolube.devcore.client.events.PlayerManagerRegisterEvent;
import de.nicolube.devcore.client.manager.playerManager.PlayerData;
import de.nicolube.devcore.utils.SystemMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Owner
 */
@Table(name = "banks")
@Entity
public class ModelBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 16, unique = true)
    private String name;

    @Column(length = 32)
    private String alias;

    @CreatedTimestamp
    private Date createdAt;

    @UpdatedTimestamp
    private Date updatedAt;

    @Transient
    private final Map<UUID, ModelAccount> accounts;
    @Transient
    private final JavaPlugin plugin;

    public ModelBank() {
        this.plugin = Main.getPlugin();
        this.accounts = new HashMap<>();
    }

    public ModelBank(String name, String alias) {
        this.name = name;
        this.alias = alias;
        this.plugin = Main.getPlugin();
        this.accounts = new HashMap<>();
    }

    // Econemy
    public boolean hasAccount(String name) {
        OfflinePlayer player = Bukkit.getPlayer(name);
        if (player == null) {
            Bukkit.getOfflinePlayer(name);
        }
        if (player == null) {
            return false;
        }
        UUID uuid = player.getUniqueId();
        if (accounts.containsKey(uuid)) {
            return true;
        }
        ModelAccount account = plugin.getDatabase().find(ModelAccount.class)
                .fetch("data")
                .where().eq("t1.uuid", uuid.toString()).where().eq("bank_id", id)
                .findUnique();
        return account != null;
    }

    protected void onJoin(PlayerManagerRegisterEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        ModelAccount account = plugin.getDatabase().find(ModelAccount.class)
                .fetch("data")
                .where().eq("t1.uuid", uuid.toString()).where().eq("bank_id", id)
                .findUnique();
        if (account == null) {
            PlayerData data = DevCore.getPlayerManager().getPlayer(event.getPlayer());
            account = new ModelAccount(data.getId(), id, 500);
            plugin.getDatabase().insert(account);
        }
        accounts.put(uuid, account);
        Bukkit.getPluginManager().callEvent(new AccountRegisterEvent(event.getPlayer(), account));
        SystemMessage.DEBUG.send("Add account for " + event.getPlayer().getName());
        SystemMessage.DEBUG.send("Stats: " + account.toString());
    }

    protected void onQuit(PlayerQuitEvent event) {
        accounts.remove(event.getPlayer().getUniqueId());
    }

    public ModelAccount getAccount(UUID uuid) {
        ModelAccount account = accounts.get(uuid);
        if (account != null) {
            return account;
        }
        account = plugin.getDatabase().find(ModelAccount.class)
                .fetch("data")
                .where().eq("t1.uuid", uuid.toString()).where().eq("bank_id", id)
                .findUnique();
        return account;
    }

    // Getter Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ModelBank{" + "id=" + id + ", name=" + name + ", alias=" + alias + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
