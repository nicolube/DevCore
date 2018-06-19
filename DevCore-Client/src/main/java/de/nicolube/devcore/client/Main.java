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
package de.nicolube.devcore.client;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import de.nicolube.devcore.DevCore;
import de.nicolube.devcore.client.manager.econemy.EconemyManager;
import de.nicolube.devcore.client.manager.econemy.ModelAccount;
import de.nicolube.devcore.client.manager.econemy.ModelBank;
import de.nicolube.devcore.client.manager.commandManager.CommandManager;
import de.nicolube.devcore.client.manager.config.ConfigManager;
import de.nicolube.devcore.client.manager.playerManager.PlayerData;
import de.nicolube.devcore.client.manager.playerManager.PlayerManager;
import de.nicolube.devcore.client.scoreboard.Scoreboards;
import de.nicolube.devcore.client.scoreboard.Tablist;
import de.nicolube.devcore.client.scoreboard.TablistV1_12_R1;
import de.nicolube.devcore.client.scoreboard.TablistV1_8_R3;
import de.nicolube.devcore.utils.SystemMessage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Nico Lube
 */
public class Main extends JavaPlugin {

    private static Main plugin;
    private ConfigManager configManager;
    private CommandManager commandManager;
    private Scoreboards scoreboards;
    private Tablist tablist;
    private PlayerManager playerManager;
    private EconemyManager econemyManager;
    private EbeanServer ebean;

    @Override
    public void onLoad() {
        super.onLoad();
        this.plugin = this;

        SystemMessage.setLogLevel(SystemMessage.ALL);
        logInfoStart();
        
        SystemMessage.INFO.send("Setup Databases");
        setupDB();

        SystemMessage.INFO.send("Init Databases");
        try {
            getDatabase().createQuery(ModelBank.class).findRowCount();
            getDatabase().createQuery(PlayerData.class).findRowCount();
            getDatabase().createQuery(ModelAccount.class).findRowCount();
        } catch (Exception e) {
            SpiEbeanServer serv = (SpiEbeanServer) getDatabase();
            DdlGenerator gen = serv.getDdlGenerator();
            gen.runScript(false, gen.generateCreateDdl());
        }

        SystemMessage.INFO.send("Starting ConfigManager");
        this.configManager = new ConfigManager(this);
        this.configManager.addConfig("config");

        SystemMessage.INFO.send("Starting player manager");
        this.playerManager = new PlayerManager(plugin);
        DevCore.setPlayerManager(playerManager);

        SystemMessage.INFO.send("Starting econemy manager");
        this.econemyManager = new EconemyManager(plugin);
        DevCore.setEconemyManager(econemyManager);

    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.plugin = this;

        logInfoStart();

        SystemMessage.INFO.send("Starting ServerPinger");
        DevCore.setServerPinger(new ServerPingerCliant(plugin));

        SystemMessage.INFO.send("Starting CommandManager");
        this.commandManager = new CommandManager();

        SystemMessage.INFO.send("Register listener for player manager");
        Bukkit.getPluginManager().registerEvents(playerManager, this);

        SystemMessage.INFO.send("Register listener for econemy manager");
        Bukkit.getPluginManager().registerEvents(econemyManager, this);

        SystemMessage.INFO.send("Starting TabList");
        if (Bukkit.getVersion().startsWith("1.8")) {
            this.tablist = new TablistV1_8_R3();
        } else if (Bukkit.getBukkitVersion().startsWith("1.12")) {
            this.tablist = new TablistV1_12_R1();
        }
        
        Bukkit.getPluginManager().registerEvents(tablist, this);

        if (getConfig().getBoolean("scoreboard.enable")) {
            SystemMessage.INFO.send("Starting Scorebaords");
            this.scoreboards = new Scoreboards();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        DevCore.onDisable();
    }

    public static Main getPlugin() {
        return plugin;
    }

    private void logInfoStart() {
        SystemMessage.INFO.send("=======================<>======================");
        SystemMessage.INFO.send("");
        SystemMessage.INFO.send("=============<DevCore by nicolube>=============");
        SystemMessage.INFO.send("");
        SystemMessage.INFO.send("=======================<>======================");
        SystemMessage.INFO.send("");
        SystemMessage.INFO.send("Starting... DEBUG Level: " + SystemMessage.getLogLevel());
        SystemMessage.INFO.send("Server Version: "+Bukkit.getBukkitVersion());
        SystemMessage.INFO.send("");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Scoreboards getScoreboards() {
        return scoreboards;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Tablist getTablist() {
        return tablist;
    }
    
    public EbeanServer getDatabase() {
        return ebean;
    }

    public List<Class<?>> getDatabaseClasses() {
        return Arrays.asList(PlayerData.class, ModelBank.class, ModelAccount.class);
    }

    private void setupDB() {
        YamlConfiguration bukkitYaml = new YamlConfiguration();
        try {
            bukkitYaml.load(new File("bukkit.yml"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ServerConfig db = new ServerConfig();
        db.setDefaultServer(false);
        db.setRegister(false);
        db.setClasses(getDatabaseClasses());
        db.setName(Bukkit.getServer().getServerId());

        DataSourceConfig ds = db.getDataSourceConfig();
        ds.setUrl(bukkitYaml.getString("database.url"));
        ds.setUsername(bukkitYaml.getString("database.username"));
        ds.setPassword(bukkitYaml.getString("database.password"));
        ds.setDriver(bukkitYaml.getString("database.driver"));
        db.setDataSourceConfig(ds);

        this.ebean = EbeanServerFactory.create(db);
    }

}
