package de.nicolube.devcore.client.scoreboard;

import de.nicolube.devcore.client.utils.Reflectorv1_12_R1;
import java.lang.reflect.Field;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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

/**
 *
 * @author Nico Lube
 */
public class TablistV1_12_R1 extends Tablist {
    
    private PacketPlayOutPlayerListHeaderFooter packet;
    
    @Override
    protected void initHeadderFooter() {
        String headerText = ChatColor.translateAlternateColorCodes('&', messages.getString("tablist.header").replace("{server}", Bukkit.getServerName()));
        String footerText = ChatColor.translateAlternateColorCodes('&', messages.getString("tablist.footer").replace("{server}", Bukkit.getServerName()));
        IChatBaseComponent header = IChatBaseComponent.ChatSerializer.a(footerText.isEmpty() ? "{\"translate\":\"\"}" : "{\"text\":\""+headerText+"\"}");
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer.a(footerText.isEmpty() ? "{\"translate\":\"\"}" : "{\"text\":\""+footerText+"\"}");
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
    
    @Override
    protected void sendHeaderAndFooter(Player p) {
        Reflectorv1_12_R1.sendPacket(p, packet);
    }
    
}
