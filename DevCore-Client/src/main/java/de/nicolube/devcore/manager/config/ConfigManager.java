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
package de.nicolube.devcore.manager.config;

import de.nicolube.devcore.LoadClass;
import de.nicolube.devcore.Main;
import de.nicolube.devcore.ModuleBase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Nico Lube
 */
public final class ConfigManager implements LoadClass {
    
    private final Map<String, BaseHolder> configList = new HashMap<>();
    private final File configFolder;

    public ConfigManager(File configFolder) {
        this.configFolder = configFolder;
        setupConfigs();
        
        addConfig("messages");
    }

    @Override
    public void load() {
        reload();
    }

    public FileConfiguration getConfig(String configName) {
        return configList.get(configName.toLowerCase()).getConfig();
    }

    public void save(String configName) {
        try {
            BaseHolder baseHolder = configList.get(configName.toLowerCase());
            baseHolder.getConfig().save(baseHolder.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupConfigs() {
        if (!configFolder.exists()) {
            configFolder.mkdir();
        }
        configList.forEach((s, c) -> {
            setupBase(c);
        });
    }

    public FileConfiguration addConfig(String configName) {
        return addBase(configName, "yml");
    }

    public FileConfiguration addStorrage(String configName) {
        return addBase(configName, "data");
    }

    private FileConfiguration addBase(String configName, String fileEnding) {
        configName = configName.toLowerCase();
        if (configList.containsKey(configName)) {
            return getConfig(configName);
        }
        BaseHolder config = new BaseHolder(configName, fileEnding, this) {
        };
        configList.put(configName, config);
        setupBase(config);
        return config.getConfig();

    }

    private void setupBase(BaseHolder c) {
        if (!c.getFile().exists()) {
            try {
                FileOutputStream out = new FileOutputStream(c.getFile());
                URL url = Main.getPlugin().getClass().getResource("/configs/" + c.getName() + "." + c.getFileEnding());
                if (url == null) {
                    return;
                }
                int read = -1;

                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                InputStream in = connection.getInputStream();
                while ((read = in.read()) != -1) {
                    out.write(read);
                }

                out.flush();
                out.close();
                in.close();
                c.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getConfigFolder() {
        return configFolder;
    }

    public void reload() {
        configList.forEach((key, config) -> {
            config.load();
        });
    }
}
