package de.nicolube.devcore.client.scoreboard;

import de.nicolube.devcore.client.Main;
import de.nicolube.devcore.client.playermanager.PlayerData;
import de.nicolube.devcore.client.utils.Reflector;
import de.nicolube.devcore.utils.SystemMessage;
import java.text.DecimalFormat;
import javax.swing.text.NumberFormatter;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import org.bukkit.Bukkit;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public final class Scoreboard {

    private final Player player;
    private final String[] content;
    private String[] oldContent;
    private final net.minecraft.server.v1_8_R3.Scoreboard board;
    private final ScoreboardObjective objective;
    private final ScoreBoardUpdater scoreboardUpdater;
    private final String[] data;
    private final PlayerData playerData;
    private final DecimalFormat formatter;

    public Scoreboard(Player player, String titel, String[] content) {
        this.player = player;
        this.playerData = Main.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
        this.content = content.clone();
        this.data = new String[9];
        this.formatter = new DecimalFormat("#0.00");
        this.board = new net.minecraft.server.v1_8_R3.Scoreboard();
        this.objective = board.registerObjective("board", IScoreboardCriteria.c);
        this.scoreboardUpdater = Main.getPlugin().getScoreboards().getNewScoreBoardUpdater(player);
        objective.setDisplayName(titel);

        final PacketPlayOutScoreboardObjective objectivPacket = new PacketPlayOutScoreboardObjective(objective, 0);
        final PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, objective);
        Reflector.sendPacket(player, objectivPacket);
        Reflector.sendPacket(player, display);

        update(new String[content.length]);

    }

    protected void update() {
        update(oldContent);
    }

    private void update(String[] oldList) {
        String[] currentContent = content.clone();
        data[0] = Integer.toString(Bukkit.getOnlinePlayers().size());   // online players
        data[1] = Long.toString(playerData.getOntime() / 3600);         // ontime
        data[2] = PermissionsEx.getUser(player).getPrefix();            // rank
        data[3] = playerData.getName();                                 // playername
        data[4] = playerData.getLastip();                               // last ipaddrass
        data[5] = playerData.getUuid();                                 // players uuid
        data[6] = Long.toString(playerData.getVotecoins());             // votecoins
        data[7] = formatter.format(playerData.getCoins());              // coins
        data[8] = Bukkit.getServerName();                               // server-name
        if (scoreboardUpdater != null) {
            scoreboardUpdater.update();
            for (int i = 0; i < currentContent.length; i++) {
                String string = currentContent[i];
                if (string != null) {
                    currentContent[i] = scoreboardUpdater.replace(string);
                }
            }
        }
        for (int i = 0; i < currentContent.length; i++) {
            String string = ChatColor.translateAlternateColorCodes('&', currentContent[i]
                    .replace("{o}", data[0]) // online players
                    .replace("{ot}", data[1]) // ontime
                    .replace("{r}", data[2]) // rank
                    .replace("{n}", data[3]) // playername
                    .replace("{li}", data[4]) // last ip
                    .replace("{u}", data[5]) // players uuid
                    .replace("{vc}", data[6]) // votecoins
                    .replace("{c}", data[7]) // coins
                    .replace("{server}", data[8]) // server-name
            );
            currentContent[i] = string;
            if (string.equals(oldList[i])) {
                continue;
            }
            if (oldList[i] != null) {
                final PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(oldList[i]);
                Reflector.sendPacket(player, packet);
            }
            final ScoreboardScore score = new ScoreboardScore(board, objective, string);
            score.setScore(currentContent.length - i - 1);
            Reflector.sendPacket(player, new PacketPlayOutScoreboardScore(score));

        }
        oldContent = currentContent;
    }
}
