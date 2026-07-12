package dev.gotiger.gTDonationEvent.command;

import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.config.MessageService;
import dev.gotiger.gTDonationEvent.config.TargetExclusionConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadSubCommand implements SubCommand {

    private final JavaPlugin plugin;
    private final MessageService messageService;
    private final DonationConfig donationConfig;
    private final TargetExclusionConfig targetExclusionConfig;

    public ReloadSubCommand(JavaPlugin plugin, MessageService messageService, DonationConfig donationConfig, TargetExclusionConfig targetExclusionConfig) {
        this.plugin = plugin;
        this.messageService = messageService;
        this.donationConfig = donationConfig;
        this.targetExclusionConfig = targetExclusionConfig;
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
        targetExclusionConfig.load();
        sender.sendMessage("config.yml, messages.yml, targets.yml 리로드 완료.");
    }
}
