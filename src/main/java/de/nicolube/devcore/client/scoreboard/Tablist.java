package de.nicolube.devcore.client.scoreboard;

import de.nicolube.devcore.LoadClass;
import de.nicolube.devcore.client.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public abstract class Tablist implements Listener, LoadClass {

    protected FileConfiguration messages;
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private FileConfiguration config;

    public Tablist() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        load();
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    @Override
    public void load() {
        messages = Main.getPlugin().getConfigManager().getConfig("messages");
        config = Main.getPlugin().getConfigManager().getConfig("config");
        if (config.getBoolean("tablist.prefixes", false)) {
            initHeadderFooter();
        }
        if (config.getBoolean("tablist.headerFooter", false)) {
            scoreboard.getTeams().forEach(t -> t.unregister());
            Bukkit.getOnlinePlayers().forEach(p -> {
                sendHeaderAndFooter(p);
                PermissionUser permUser = PermissionsEx.getUser(p);
                String teamName = permUser.getParentIdentifiers().get(0);
                String teamPrefix = ChatColor.translateAlternateColorCodes('&', permUser.getOption("tab-prefix"));
                if (teamName != null) {
                    teamName = permUser.getOption("priority") + teamName;
                } else {
                    teamName = "0000";
                }
                Team team = scoreboard.getTeam(teamName);
                if (team == null) {
                    team = scoreboard.registerNewTeam(teamName);
                    team.setAllowFriendlyFire(true);
                    team.setCanSeeFriendlyInvisibles(false);
                    team.setPrefix(ChatColor.getLastColors(teamPrefix));
                }
                p.setPlayerListName(teamPrefix + p.getName());
                team.addEntry(p.getName());
                p.setScoreboard(scoreboard);
            });
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (config.getBoolean("tablist.prefixes", false)) {
            sendHeaderAndFooter(player);
        }
        if (config.getBoolean("tablist.headerFooter", false)) {
            PermissionUser permUser = PermissionsEx.getUser(player);
            String teamName = permUser.getParentIdentifiers().get(0);
            String teamPrefix = ChatColor.translateAlternateColorCodes('&', permUser.getOption("tab-prefix"));
            if (teamName != null) {
                teamName = permUser.getOption("priority") + teamName;
            } else {
                teamName = "0000";
            }
            Team team = scoreboard.getTeam(teamName);
            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
                team.setAllowFriendlyFire(true);
                team.setCanSeeFriendlyInvisibles(false);
                team.setPrefix(ChatColor.getLastColors(teamPrefix));
            }
            player.setPlayerListName(teamPrefix + player.getName());
            team.addEntry(player.getName());
            player.setScoreboard(scoreboard);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (config.getBoolean("tablist.headerFooter", false)) {
            PermissionUser permUser = PermissionsEx.getUser(player);
            String teamName = permUser.getParentIdentifiers().get(0);
            if (teamName != null) {
                teamName = permUser.getOption("priority") + teamName;
            } else {
                teamName = "0000";
            }
            scoreboard.getTeam(teamName).removeEntry(player.getName());
        }
    }

    abstract protected void initHeadderFooter();

    abstract protected void sendHeaderAndFooter(Player p);
}
