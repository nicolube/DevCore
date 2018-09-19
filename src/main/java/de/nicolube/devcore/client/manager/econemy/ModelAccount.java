/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.manager.econemy;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import de.nicolube.devcore.client.Main;
import de.nicolube.devcore.client.manager.playerManager.PlayerData;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Owner
 */
@Table(name = "accounts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"bank_id", "user_id"})})
@Entity
public class ModelAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column()
    private long bankId;

    @Column
    private double balance;

    @CreatedTimestamp
    private Date createdAt;

    @UpdatedTimestamp
    private Date updatedAt;

    @Transient
    private long lastCheckt;

    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private PlayerData data;

    public ModelAccount() {
        this.lastCheckt = System.currentTimeMillis();
    }

    public ModelAccount(long user_id, long bank_id, double balance) {
        this.lastCheckt = System.currentTimeMillis();
        this.userId = user_id;
        this.bankId = bank_id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBalance(double balance) {
        checkUpdate();
        setBalance(this.balance + balance);
        update();
    }

    public boolean takeBalance(double balance) {
        checkUpdate();
        balance = this.balance - balance;
        if (balance < 0) {
            return false;
        }
        setBalance(balance);
        update();
        return true;
    }

    public boolean hasBalance(double balance) {
        return this.balance >= balance;
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

    public PlayerData getData() {
        return data;
    }

    public void setData(PlayerData data) {
        this.data = data;
    }

    public void checkUpdate() {
        long cm = System.currentTimeMillis();
        if (lastCheckt + 100 < cm) {
            ModelAccount data = Main.getPlugin().getDatabase().find(getClass()).where().eq("id", id).findUnique();
            this.balance = data.balance;
        }
    }

    public void update() {
        setUpdatedAt(new Date());
        Main.getPlugin().getDatabase().update(this);
    }

    @Override
    public String toString() {
        return "ModelAccount{" + "id=" + id + ", userId=" + userId + ", bankId=" + bankId + ", balance=" + balance + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", lastCheckt=" + lastCheckt + '}';
    }
}
