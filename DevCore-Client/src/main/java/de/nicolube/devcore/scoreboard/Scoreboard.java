package de.nicolube.devcore.scoreboard;

import de.nicolube.devcore.utils.Reflector;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public final class Scoreboard {

    private final Player player;
    private final String[] content;
    private String[] oldContent;
    private final net.minecraft.server.v1_8_R3.Scoreboard board;
    private final ScoreboardObjective objective;

    public Scoreboard(Player player, String titel, String[] content) {
        this.player = player;
        this.content = content.clone();
        this.board = new net.minecraft.server.v1_8_R3.Scoreboard();
        this.objective = board.registerObjective("board", IScoreboardCriteria.c);
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
        String rank = PermissionsEx.getUser(player).getPrefix();
        String[] currentContent = content.clone();
        for (int i = 0; i < currentContent.length; i++) {
            String string = currentContent[i];
            if (string.equals(oldList[i])) {
                continue;
            }
            /*
            Map<String, Long> stats = playersModule.getPlayerData(player).getStats();
            for (String key : stats.keySet()) {a
                long vaule = stats.get(key);
                if (string.contains("{" + key + "}")) {
                    string = string.replace("{" + key + "}", Long.toString(vaule));
                    break;
                }
            }
            */
            string = string.replace("{rank}", rank);
            string = ChatColor.translateAlternateColorCodes('&', string);
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
