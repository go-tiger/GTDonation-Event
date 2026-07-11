package dev.gotiger.gTDonationEvent.action.view;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RandomViewAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public RandomViewAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "RANDOM_VIEW";
    }

    @Override
    public void execute(Player target, int amount) {
        int count = plugin.getConfig().getInt("random-view.count", 15);
        long intervalTicks = plugin.getConfig().getLong("random-view.interval-ticks", 10L);

        new BukkitRunnable() {
            int remaining = count;

            @Override
            public void run() {
                if (remaining <= 0 || !target.isOnline()) {
                    cancel();
                    return;
                }

                float yaw = random.nextFloat() * 360f - 180f;
                float pitch = random.nextFloat() * 180f - 90f;
                target.setRotation(yaw, pitch);
                remaining--;
            }
        }.runTaskTimer(plugin, 0L, intervalTicks);
    }
}
