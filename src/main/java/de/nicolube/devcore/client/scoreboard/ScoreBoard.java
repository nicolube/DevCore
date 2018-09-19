/*
 * Copyright (C) 2018 Nico Lube
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.nicolube.devcore.client.scoreboard;

import de.nicolube.devcore.client.Main;
import de.nicolube.devcore.client.manager.playerManager.PlayerData;
import java.text.DecimalFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 *
 * @author Nico Lube
 */
public abstract class ScoreBoard {

    protected final Player player;
    protected final String[] content;
    protected String[] oldContent;
    protected final ScoreBoardUpdater scoreboardUpdater;
    protected final String[] data;
    protected final PlayerData playerData;
    protected final DecimalFormat formatter;

    public ScoreBoard(Player player, String[] content) {
        this.player = player;
        this.playerData = Main.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
        this.content = content.clone();
        this.data = new String[9];
        this.formatter = new DecimalFormat("#0.00");
        this.scoreboardUpdater = Main.getPlugin().getScoreboards().getNewScoreBoardUpdater(player);
    }

    protected void update() {
        update(oldContent);
    }

    protected void update(String[] oldList) {
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
                removeScore(oldList[i]);
            }
            addScore(string, currentContent.length - i - 1);
        }
        oldContent = currentContent;
    }

    abstract protected void removeScore(String string);

    abstract protected void addScore(String string, int score);
}
