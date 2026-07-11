package dev.gotiger.gTDonationEvent.action.hazard.frostbite;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FrostbiteManager {

    private final JavaPlugin plugin;

    public FrostbiteManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void apply(Player player) {
        int durationSeconds = plugin.getConfig().getInt("frostbite.duration-seconds", 8);
        double damagePerTick = plugin.getConfig().getDouble("frostbite.damage-per-tick", 1.0);

        player.setFreezeTicks(player.getMaxFreezeTicks());
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_BUCKET_EMPTY_POWDER_SNOW, 1f, 1f);
        player.damage(damagePerTick);

        new BukkitRunnable() {
            int ticksLeft = durationSeconds * 20;

            @Override
            public void run() {
                if (!player.isOnline() || player.isDead() || ticksLeft <= 0) {
                    cancel();
                    return;
                }

                player.setFreezeTicks(player.getMaxFreezeTicks());
                if (ticksLeft % 20 == 0) {
                    player.damage(damagePerTick);
                }
                ticksLeft--;
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }
}
