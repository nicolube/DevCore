package de.nicolube.devcore.manager.commandManager;

import de.nicolube.devcore.Main;
import de.nicolube.devcore.utils.PlayerMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class Command extends BukkitCommand implements CommandBase {

    private static CommandManager manager;
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private final Map<String, SubCommand> subCommandsAliases = new HashMap<>();
    private final String permission;
    private final String advanceName;
    //public final Util util;
    public FileConfiguration messages;
    private Main plugin;

    public Command(String name, String description, String usageMessage, String[] aliases, String permission) {
        super(name.toLowerCase(), description, usageMessage, Arrays.asList(aliases));
        this.plugin = Main.getPlugin();
        this.permission = permission;
        this.advanceName = ("/" + name);
        //this.util = Main.getPlugin().getUtils();
    }

    @Override
    public void onEnable() {
        messages = manager.getMessages();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            subCommands.forEach((n, c) -> c.onEnable());
        }, 1);
    }

    @Override
    public boolean execute(CommandSender sender, String labal, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            PlayerMessage.SYSTEM.sendNoPerm(sender);
            return true;
        }
        if (args.length > 0) {
            if (args[0] == null) {
                manager.helpCommand(sender, this);
                return true;
            }
            String arg0 = args[0].toLowerCase();
            if (arg0.equals("help")) {
                manager.helpCommand(sender, this);
            }
            if (subCommands.size() > 0 && subCommandsAliases.containsKey(arg0)) {
                subCommandsAliases.get(arg0).execute(sender, labal, args);
                return true;
            }
            if (!run(sender, labal, args)) {
                manager.helpCommand(sender, this);
                return true;
            }
            return true;
        }
        if (subCommands.size() > 0) {
            manager.helpCommand(sender, this);
            return true;
        }
        if (!run(sender, labal, args)) {
            manager.helpCommand(sender, this);
            return true;
        }
        return true;
    }

    public static void setManager(CommandManager manager) {
        Command.manager = manager;
    }

    @Override
    public void addSubCommand(SubCommand command) {
        List<String> aliases = command.getAliases();
        if (aliases != null) {
            aliases.forEach((alias) -> subCommandsAliases.put(alias.toLowerCase(), command));
        }
        subCommands.put(command.getName(), command);
        subCommandsAliases.put(command.getName(), command);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) throws IllegalArgumentException {
        int length = args.length;
        if (length == 0) {
            return getDefaultComplets(null);
        }
        CommandBase currentCompleter = this;
        CommandBase oldCompleter = this;
        for (int i = 0; i < length; i++) {
            String arg = args[i].toLowerCase();
            currentCompleter = currentCompleter.getNextCompleter(arg);
            if (currentCompleter == null) {
                if (i == length - 1) {
                    return oldCompleter.getComplets(arg);
                }
                if (oldCompleter != null) {
                    return oldCompleter.getComplets();
                }
                break;
            }
            oldCompleter = currentCompleter;
        }
        return getDefaultComplets(args[args.length]);
    }

    @Override
    public SubCommand getNextCompleter(String name) {
        name = name.toLowerCase();
        if (subCommands.containsKey(name)) {
            return subCommands.get(name);
        }
        return null;
    }

    @Override
    public List<String> getComplets(String startsWith) {
        startsWith = startsWith.toLowerCase();
        if (subCommands.isEmpty()) {
            return getDefaultComplets(startsWith);
        }
        return filterStartsWith(new ArrayList<String>(subCommands.keySet()), startsWith);
    }

    @Override
    public List<String> getComplets() {
        if (subCommands.isEmpty()) {
            return getDefaultComplets(null);
        }
        return new ArrayList<String>(subCommands.keySet());
    }

    private List<String> getDefaultComplets(String startsWith) {
        List<String> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
        if (startsWith == null) {
            return players;
        }
        startsWith = startsWith.toLowerCase();
        return filterStartsWith(players, startsWith);
    }

    private List<String> filterStartsWith(List<String> list, String filter) {
        return list.stream().filter(s -> s.startsWith(filter)).collect(Collectors.toList());
    }

    @Override
    public Map<String, SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public Map<String, SubCommand> getSubCommandsAliases() {
        return subCommandsAliases;
    }

    @Override
    public String getAdvanceName() {
        return advanceName;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getUsageMessage() {
        return usageMessage;
    }

    public FileConfiguration getMessages() {
        return manager.getMessages();
    }

}
