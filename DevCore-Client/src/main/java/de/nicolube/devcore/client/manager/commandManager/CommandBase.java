package de.nicolube.devcore.client.manager.commandManager;

import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;

public abstract interface CommandBase
{
  public abstract boolean run(CommandSender paramCommandSender, String paramString, String[] paramArrayOfString);
  
  public abstract void onEnable();
  
  public abstract void addSubCommand(SubCommand paramSubCommand);
  
  public abstract SubCommand getNextCompleter(String paramString);
  
  public abstract List<String> getComplets();
  
  public abstract List<String> getComplets(String paramString);
  
  public abstract String getDescription();
  
  public abstract String getName();
  
  public abstract List<String> getAliases();
  
  public abstract Map<String, SubCommand> getSubCommands();
  
  public abstract Map<String, SubCommand> getSubCommandsAliases();
  
  public abstract String getPermission();
  
  public abstract String getAdvanceName();
  
  public abstract String getUsageMessage();
}
