package de.nicolube.devcore.client.manager.commandManager;

import java.util.HashMap;
import java.util.Map;

public class CommandHolder {

    private Map<String, Command> commandList = new HashMap<>();
    public void onEnable() {
        
    }
    
    public final void addCommand(Command command) {
        commandList.put(command.getName(), command);
    }

    public Map<String, Command> getCommandList() {
        return commandList;
    }
}
