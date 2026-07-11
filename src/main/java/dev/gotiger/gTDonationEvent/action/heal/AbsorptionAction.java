package dev.gotiger.gTDonationEvent.action.heal;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AbsorptionAction implements DonationAction {

    private final JavaPlugin plugin;

    public AbsorptionAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "ABSORPTION";
    }

    @Override
    public void execute(Player target, int amount) {
        int durationSeconds = plugin.getConfig().getInt("absorption.duration-seconds", 60);
        int hearts = plugin.getConfig().getInt("absorption.hearts", 4);
        int amplifier = Math.max(0, (int) Math.ceil(hearts / 2.0) - 1);

        target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, durationSeconds * 20, amplifier, false, true, true));
    }
}
