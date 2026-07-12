package dev.gotiger.gTDonationEvent.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashMap;
import java.util.Map;

public class GTDonationCommand implements CommandExecutor {

    private final Map<String, SubCommand> subCommands = new LinkedHashMap<>();

    public GTDonationCommand(SubCommand... commands) {
        for (SubCommand command : commands) {
            subCommands.put(command.name(), command);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) {
                subCommand.execute(sender, args);
                return true;
            }
        }

        sender.sendMessage("사용법: /" + label + " <" + String.join("|", subCommands.keySet()) + ">");
        return true;
    }
}
