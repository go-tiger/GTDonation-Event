package dev.gotiger.gTDonationEvent.action;

import org.bukkit.entity.Player;

public interface DonationAction {

    String getName();

    void execute(Player target, int amount);
}
