package dev.gotiger.gTDonationEvent.command;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    String name();

    void execute(CommandSender sender, String[] args);
}
