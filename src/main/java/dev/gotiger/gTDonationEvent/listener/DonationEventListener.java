package dev.gotiger.gTDonationEvent.listener;

import dev.gotiger.gTDonationCore.event.DonationEvent;
import dev.gotiger.gTDonationEvent.action.DonationAction;
import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.config.DonationMapping;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DonationEventListener implements Listener {

    private final DonationConfig donationConfig;
    private final DonationActionRegistry actionRegistry;
    private final Random random = new Random();

    public DonationEventListener(DonationConfig donationConfig, DonationActionRegistry actionRegistry) {
        this.donationConfig = donationConfig;
        this.actionRegistry = actionRegistry;
    }

    @EventHandler
    public void onDonation(DonationEvent event) {
        Optional<DonationMapping> mapping = donationConfig.getMapping(event.getAmount());
        if (mapping.isEmpty()) {
            return;
        }

        Optional<DonationAction> action = actionRegistry.get(mapping.get().action());
        if (action.isEmpty()) {
            return;
        }

        List<Player> targets = resolveTargets(mapping.get().target(), event.getPlayer());
        for (Player target : targets) {
            action.get().execute(target, event.getAmount());
        }
    }

    private List<Player> resolveTargets(dev.gotiger.gTDonationEvent.config.DonationTarget target, Player donor) {
        return switch (target) {
            case PLAYER -> List.of(donor);
            case ALL -> List.copyOf(Bukkit.getOnlinePlayers());
            case RANDOM -> {
                List<Player> online = List.copyOf(Bukkit.getOnlinePlayers());
                if (online.isEmpty()) {
                    yield List.of();
                }
                yield List.of(online.get(random.nextInt(online.size())));
            }
        };
    }
}
