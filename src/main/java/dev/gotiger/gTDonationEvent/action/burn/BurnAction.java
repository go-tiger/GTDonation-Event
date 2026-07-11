package dev.gotiger.gTDonationEvent.action.burn;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BurnAction implements DonationAction {

    private final JavaPlugin plugin;

    public BurnAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "BURN";
    }

    @Override
    public void execute(Player target, int amount) {
        int durationSeconds = plugin.getConfig().getInt("burn.duration-seconds", 5);

        target.setFireTicks(durationSeconds * 20);
        target.getWorld().playSound(target.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1f, 1f);
    }
}
