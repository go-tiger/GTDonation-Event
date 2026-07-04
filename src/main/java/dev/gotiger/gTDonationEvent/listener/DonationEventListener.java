package dev.gotiger.gTDonationEvent.listener;

import dev.gotiger.gTDonationCore.event.DonationEvent;
import dev.gotiger.gTDonationEvent.GTDonationEvent;
import dev.gotiger.gTDonationEvent.action.DonationAction;
import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.config.DonationMapping;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

public class DonationEventListener implements Listener {

    private final GTDonationEvent plugin;
    private final DonationConfig donationConfig;
    private final DonationActionRegistry actionRegistry;

    public DonationEventListener(GTDonationEvent plugin, DonationConfig donationConfig, DonationActionRegistry actionRegistry) {
        this.plugin = plugin;
        this.donationConfig = donationConfig;
        this.actionRegistry = actionRegistry;
    }

    @EventHandler
    public void onDonation(DonationEvent event) {
        Optional<DonationMapping> mapping = donationConfig.getMapping(event.getAmount());
        if (mapping.isEmpty()) {
            return;
        }

        if (plugin.isScriptMode()) {
            plugin.getLogger().info("[SCRIPT] " + event.getAmount() + " -> " + mapping.get().action());
            return;
        }

        Optional<DonationAction> action = actionRegistry.get(mapping.get().action());
        if (action.isEmpty()) {
            return;
        }

        for (Player target : mapping.get().target().resolve(event.getPlayer())) {
            action.get().execute(target, event.getAmount());
        }
    }
}
