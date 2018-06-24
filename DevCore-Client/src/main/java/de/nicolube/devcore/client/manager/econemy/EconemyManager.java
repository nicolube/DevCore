/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.manager.econemy;

import de.nicolube.devcore.client.Main;
import de.nicolube.devcore.client.events.PlayerManagerRegisterEvent;
import de.nicolube.devcore.utils.SystemMessage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Owner
 */
public class EconemyManager implements Listener {

    private static Map<String, ModelBank> banks;
    private static DecimalFormat formatter;

    public EconemyManager(Main plugin) {
        addBank(Bukkit.getServerId(), Bukkit.getServerName(), plugin.getConfigManager().getConfig("config").getDouble("startbalance.main", 0));
    }

    static {
        EconemyManager.banks = new HashMap<>();
        EconemyManager.formatter = new DecimalFormat("#0.00");
    }

    public static ModelBank addBank(String name, String alias, double  startBalance) {
        SystemMessage.INFO.send("Add Bank " + name);
        ModelBank bank = Main.getPlugin().getDatabase().find(ModelBank.class).where().eq("name", name).findUnique();
        if (bank == null) {
            bank = new ModelBank(name, alias, startBalance);
            Main.getPlugin().getDatabase().insert(bank);
        } else {
            bank.setAlias(alias);
        }
        EconemyManager.banks.put(name, bank);
        return bank;
    }

    public static ModelBank getBank(String name) {
        return banks.get(name);
    }

    public static ModelBank getMainBank() {
        return EconemyManager.banks.get(Bukkit.getServerId());
    }

    public static String format(double balance) {
        return formatter.format(balance);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onJoin(PlayerManagerRegisterEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            banks.forEach((id, b) -> b.onJoin(event));
        });
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        banks.forEach((id, b) -> b.onQuit(event));
    }

}
