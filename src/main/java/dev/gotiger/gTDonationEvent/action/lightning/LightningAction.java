package dev.gotiger.gTDonationEvent.action.lightning;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LightningAction implements DonationAction {

    private final JavaPlugin plugin;

    public LightningAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "LIGHTNING";
    }

    @Override
    public void execute(Player target, int amount) {
        boolean dealDamage = plugin.getConfig().getBoolean("lightning.deal-damage", true);

        if (dealDamage) {
            target.getWorld().strikeLightning(target.getLocation());
        } else {
            target.getWorld().strikeLightningEffect(target.getLocation());
        }
    }
}
