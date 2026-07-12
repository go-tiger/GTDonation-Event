package dev.gotiger.gTDonationEvent.action.hazard.instantdeath;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.entity.Player;

public class InstantDeathAction implements DonationAction {

    private final InstantDeathManager manager;

    public InstantDeathAction(InstantDeathManager manager) {
        this.manager = manager;
    }

    @Override
    public String getName() {
        return "INSTANT_DEATH";
    }

    @Override
    public void execute(Player target, int amount) {
        manager.kill(target);
    }
}
