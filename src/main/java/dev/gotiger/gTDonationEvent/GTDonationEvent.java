package dev.gotiger.gTDonationEvent;

import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.listener.DonationEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class GTDonationEvent extends JavaPlugin {

    private DonationConfig donationConfig;
    private DonationActionRegistry actionRegistry;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        donationConfig = new DonationConfig();
        donationConfig.load(getConfig());

        actionRegistry = new DonationActionRegistry();

        getServer().getPluginManager().registerEvents(
                new DonationEventListener(donationConfig, actionRegistry),
                this
        );
    }

    @Override
    public void onDisable() {
    }

    public DonationActionRegistry getActionRegistry() {
        return actionRegistry;
    }
}
