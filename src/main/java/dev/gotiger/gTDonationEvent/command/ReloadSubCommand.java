package dev.gotiger.gTDonationEvent.command;

import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.config.MessageService;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadSubCommand implements SubCommand {

    private final JavaPlugin plugin;
    private final MessageService messageService;
    private final DonationConfig donationConfig;

    public ReloadSubCommand(JavaPlugin plugin, MessageService messageService, DonationConfig donationConfig) {
        this.plugin = plugin;
        this.messageService = messageService;
        this.donationConfig = donationConfig;
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        messageService.load();
        donationConfig.load(plugin.getConfig());
        sender.sendMessage("config.yml, messages.yml 리로드 완료.");
    }
}
