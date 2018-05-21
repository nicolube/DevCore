package de.nicolube.devcore.scoreboard;

import de.nicolube.devcore.LoadClass;
import de.nicolube.devcore.Main;
import de.nicolube.devcore.utils.SystemMessage;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Scoreboards implements Listener, LoadClass {

    private final Map<String, Scoreboard> scoreboards = new HashMap<>();
    private String[] content;
    private String titel;
    private FileConfiguration config;
    private Class<? extends ScoreBoardUpdater> scoreBoardUpdater;

    public Scoreboards() {
        load();
    }
    
    @Override
    public void load() {
        SystemMessage.DEBUG.send("Start ScoreBoard Loading");
        this.config = Main.getPlugin().getConfigManager().getConfig("config");
        List<String> list = config.getStringList("scoreboard.content");
        this.content = list.toArray(new String[list.size()]);
        this.titel = ChatColor.translateAlternateColorCodes('&', config.getString("scoreboard.titel"));
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
        scoreboards.clear();
        Bukkit.getOnlinePlayers().forEach(player -> scoreboards.put(player.getUniqueId().toString(), new Scoreboard(player, titel, content)));
        SystemMessage.DEBUG.send("Start Updater for the ScoreBoards");
        autoUpdate();
        SystemMessage.DEBUG.send("End ScoreBoard Loading");
    }

    private void autoUpdate() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), () -> {
            scoreboards.values().forEach(s -> s.update());
        }, 100, 100);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SystemMessage.DEBUG.send("Add ScoreBoard to "+player.getName());
        scoreboards.put(player.getUniqueId().toString(), new Scoreboard(player, titel, content));
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        remove(event.getPlayer());
    }

    private void remove(Player player) {
        String uuid = player.getUniqueId().toString();
        if (scoreboards.containsKey(uuid)) {
            scoreboards.remove(uuid);
        }
    }

    private void update(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!scoreboards.containsKey(uuid)) {
            scoreboards.put(uuid, new Scoreboard(player, titel, content));
        }
        scoreboards.get(uuid).update();
    }

    public void setScoreBoardUpdater(Class<? extends ScoreBoardUpdater> scoreBoardUpdater) {
        this.scoreBoardUpdater = scoreBoardUpdater;
    }

    public ScoreBoardUpdater getNewScoreBoardUpdater(Player player) {
        try {
            return scoreBoardUpdater.getConstructor(Player.class).newInstance(new Object[]{player});
        } catch (InstantiationException ex) {
            Logger.getLogger(Scoreboards.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Scoreboards.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Scoreboards.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Scoreboards.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Scoreboards.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Scoreboards.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
