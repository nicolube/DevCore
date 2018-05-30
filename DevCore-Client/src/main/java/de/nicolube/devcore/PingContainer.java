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
package de.nicolube.devcore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import net.md_5.bungee.api.ChatColor;

/**
 *
 * @author Nico Lube
 */
public class PingContainer {
        
        private String address;
        private int port;
        private String motd = null;
        private String maxPlayers = "0";
        private String onlinePlayers = "0";
        
        public PingContainer(String address, int port) {
            this.address = address;
            this.port = port;
            update();
        }

        /*
         * Return a array with Motd, players, maxPlayers
         */
        public final void update() {
            try {
                Socket socket = new Socket();
                socket.setTcpNoDelay(true);
                socket.connect(new InetSocketAddress(address, port), 100);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                dos.write(0xFE);
                int b;
                StringBuffer str = new StringBuffer();
                while ((b = dis.read()) != -1) {
                    if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                        str.append((char) b);
                    }
                }
                socket.close();
                String[] data = str.toString().split("§");
                motd = data[0];
                onlinePlayers = data[1];
                maxPlayers = data[2];
            } catch (SocketTimeoutException e) {
                if (!maxPlayers.equals("0")) {
                    maxPlayers = "unknown";
                }
                onlinePlayers = ChatColor.translateAlternateColorCodes('&', "&coffline");
            } catch (IOException e) {
                if (!maxPlayers.equals("0")) {
                    maxPlayers = "unknown";
                }
                onlinePlayers = ChatColor.translateAlternateColorCodes('&', "&coffline");
            }
        }
        
        public String getAddress() {
            return address;
        }
        
        public int getPort() {
            return port;
        }
        
        public String getMotd() {
            return motd;
        }
        
        public String getMaxPlayers() {
            return maxPlayers;
        }
        
        public String getOnlinePlayers() {
            return onlinePlayers;
        }
    }