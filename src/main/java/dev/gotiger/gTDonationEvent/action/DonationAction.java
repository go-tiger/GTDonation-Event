package dev.gotiger.gTDonationEvent.action;

import dev.gotiger.gTDonationEvent.config.DonationTarget;
import org.bukkit.entity.Player;

public interface DonationAction {

    String getName();

    void execute(Player target, int amount);

    default void applyTo(Player donor, int amount) {
        applyTo(donor, amount, DonationTarget.PLAYER);
    }

    default void applyTo(Player donor, int amount, DonationTarget target) {
        for (Player recipient : target.resolve(donor)) {
            execute(recipient, amount);
        }
    }
}
