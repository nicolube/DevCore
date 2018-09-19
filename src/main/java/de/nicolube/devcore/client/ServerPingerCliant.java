/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client;

import de.nicolube.devcore.PingContainer;
import de.nicolube.devcore.ServerPinger;
import java.util.HashMap;
import org.bukkit.Bukkit;

/**
 *
 * @author Nico Lube
 */
public class ServerPingerCliant implements ServerPinger {

    private HashMap<String, PingContainer> pingerMap;
    private final Main plugin;

    public ServerPingerCliant(Main plugin) {
        pingerMap = new HashMap<>();
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            pingerMap.forEach((n, p) -> p.update());
        }, 100, 100);
    }

    @Override
    public String addPinger(String address, int port) {
        pingerMap.put(address + port, new PingContainer(address, port));
        return address + port;
    }

    @Override
    public PingContainer getPinger(String name) {
        return pingerMap.get(name);
    }
}
