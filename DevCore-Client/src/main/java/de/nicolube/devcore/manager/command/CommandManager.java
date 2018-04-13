package de.nicolube.devcore.manager.command;

import de.nicolube.devcore.ModuleBase;
import de.nicolube.devcore.manager.command.bases.Command;
import de.nicolube.devcore.manager.command.bases.CommandBase;
import de.nicolube.devcore.manager.command.bases.CommandHolder;
import de.nicolube.devcore.manager.command.bases.SubCommand;
import de.nicolube.devcore.utils.PlayerMessage;
import de.nicolube.devcore.utils.SystemMessage;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandManager extends ModuleBase {

    private final Map<String, Command> commandsMap = new HashMap<>();

    public void CommandManager() {
        SystemMessage.INFO.send("Start the CommandManager");
        Command.setManager(this);
        SubCommand.setManager(this);

    }

    public void addCommandHolder(CommandHolder holder) {
        SystemMessage.DEBUG.send("CommandManager - add holder: "+holder.getClass().getSimpleName());
        holder.getCommandList().forEach((n, c) -> addCommand(c));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            holder.onEnable();
        }, 1);
    }

    public void addCommand(Command command) {
        SystemMessage.DEBUG.send("CommandManager - add command: "+command.getName());
        commandsMap.put(command.getName(), command);
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            final Field f = commandMap.getClass().getDeclaredField("knownCommands");
            f.setAccessible(true);
            Map<String, Command> cmds = (Map<String, Command>) f.get(commandMap);
            if (cmds.containsKey(command.getName())) {
                cmds.remove(command.getName());
            }
            command.getAliases().forEach(s -> {
                if (cmds.containsKey(s)) {
                    cmds.remove(s);
                }
            });
            f.set(commandMap, cmds);
            commandMap.register("DevCore", command);
            command.onEnable();
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void helpCommand(CommandSender sender, CommandBase commandBase) {
        String command = commandBase.getAdvanceName();
        Map<String, SubCommand> subCommands = commandBase.getSubCommands();
        messages.getStringList("command.helpHeader").forEach(s -> PlayerMessage.SYSTEM.send(sender, s.replace("{command}", command)));
        if (subCommands.size() > 0) {
            subCommands.forEach((k, v) -> {
                String message = messages.getString("command.help");
                message = message.replace("{command}", command + " " + v.getName());
                message = message.replace("{description}", v.getDescription());
                PlayerMessage.SYSTEM.send(sender, message);
            });
        } else {
            String message = messages.getString("command.help");
            message = message.replace("{command}", commandBase.getUsageMessage());
            message = message.replace("{description}", commandBase.getDescription());
            PlayerMessage.SYSTEM.send(sender, message);
        }
        messages.getStringList("command.helpFooter").forEach(s -> PlayerMessage.SYSTEM.send(sender, s.replace("{command}", command)));

    }

    public FileConfiguration getMessages() {
        return messages;
    }

}
