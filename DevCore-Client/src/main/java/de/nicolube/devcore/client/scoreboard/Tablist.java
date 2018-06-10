package de.nicolube.devcore.client.scoreboard;

import de.nicolube.devcore.LoadClass;
import de.nicolube.devcore.client.Main;
import de.nicolube.devcore.client.utils.Reflector;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Tablist implements Listener, LoadClass {

    private FileConfiguration messages;
    private PacketPlayOutPlayerListHeaderFooter packet;
    private org.bukkit.scoreboard.Scoreboard scoreboard;

    public Tablist() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        load();
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    @Override
    public void load() {
        messages = Main.getPlugin().getConfigManager().getConfig("messages");
        initHeadderFooter();
        scoreboard.getTeams().forEach(t -> t.unregister());
        Bukkit.getOnlinePlayers().forEach(p -> {
            sendHeaderAndFooter(p);
            PermissionUser permUser = PermissionsEx.getUser(p);
            String teamName = permUser.getParentIdentifiers().get(0);
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
                team.setPrefix(teamName);
            }
            p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', permUser.getOption("tab-prefix") + p.getName()));
            team.addEntry(p.getName());
            p.setScoreboard(scoreboard);
        });

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        sendHeaderAndFooter(player);
        PermissionUser permUser = PermissionsEx.getUser(player);
        String teamName = permUser.getParentIdentifiers().get(0);
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
        }
        player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', permUser.getOption("tab-prefix") + player.getName()));
        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PermissionUser permUser = PermissionsEx.getUser(player);
        String teamName = permUser.getParentIdentifiers().get(0);
        if (teamName != null) {
            teamName = permUser.getOption("priority") + teamName;
        } else {
            teamName = "0000";
        }
        scoreboard.getTeam(teamName).removeEntry(player.getName());
    }

    private void initHeadderFooter() {
        String headerText = ChatColor.translateAlternateColorCodes('&', messages.getString("tablist.header").replace("{server}", Bukkit.getServerName()));
        String footerText = ChatColor.translateAlternateColorCodes('&', messages.getString("tablist.footer").replace("{server}", Bukkit.getServerName()));
        IChatBaseComponent header = ChatSerializer.a("{'color': '" + "', 'text': '" + headerText + "'}");
        IChatBaseComponent footer = ChatSerializer.a("{'color': '" + "', 'text': '" + footerText + "'}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, header);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footer);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.packet = packet;
    }

    private void sendHeaderAndFooter(Player p) {
        Reflector.sendPacket(p, packet);
    }
}
