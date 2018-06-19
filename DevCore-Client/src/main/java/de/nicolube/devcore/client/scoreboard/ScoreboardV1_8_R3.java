package de.nicolube.devcore.client.scoreboard;

import de.nicolube.devcore.client.utils.Reflectorv1_8_8;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;

public final class ScoreboardV1_8_R3 extends ScoreBoard{

    private final net.minecraft.server.v1_8_R3.Scoreboard board;
    private final ScoreboardObjective objective;

    public ScoreboardV1_8_R3(Player player, String titel, String[] content) {
        super(player, content);
        this.board = new net.minecraft.server.v1_8_R3.Scoreboard();
        this.objective = board.registerObjective("board", IScoreboardCriteria.c);
        objective.setDisplayName(titel);

        final PacketPlayOutScoreboardObjective objectivPacket = new PacketPlayOutScoreboardObjective(objective, 0);
        final PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, objective);
        Reflectorv1_8_8.sendPacket(player, objectivPacket);
        Reflectorv1_8_8.sendPacket(player, display);

        update(new String[content.length]);
    }

    @Override
    protected void removeScore(String string) {
        final PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(string);
        Reflectorv1_8_8.sendPacket(player, packet);
    }

    @Override
    protected void addScore(String string, int score) {
        final ScoreboardScore sScore = new ScoreboardScore(board, objective, string);
        sScore.setScore(score);
        Reflectorv1_8_8.sendPacket(player, new PacketPlayOutScoreboardScore(sScore));
    }
}
