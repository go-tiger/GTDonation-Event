package dev.gotiger.gTDonationEvent.action.movement.scale;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RandomScaleManager {

    private static final Attribute SCALE_ATTRIBUTE = resolveScaleAttribute();

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public RandomScaleManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean apply(Player player) {
        if (SCALE_ATTRIBUTE == null) {
            return false;
        }

        AttributeInstance instance = player.getAttribute(SCALE_ATTRIBUTE);
        if (instance == null) {
            return false;
        }

        double minScale = plugin.getConfig().getDouble("random-scale.min-scale", 0.5);
        double maxScale = plugin.getConfig().getDouble("random-scale.max-scale", 2.0);
        int durationSeconds = plugin.getConfig().getInt("random-scale.duration-seconds", 30);
        long durationMillis = durationSeconds * 1000L;

        double scale = minScale + random.nextDouble() * (maxScale - minScale);
        double originalScale = instance.getBaseValue();
        instance.setBaseValue(scale);

        BossBar bossBar = Bukkit.createBossBar(
                "크기 변경 남은 시간: " + durationSeconds + "초",
                BarColor.PURPLE,
                BarStyle.SOLID
        );
        bossBar.addPlayer(player);

        long startedAtMillis = System.currentTimeMillis();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    bossBar.removeAll();
                    cancel();
                    return;
                }

                long remaining = durationMillis - (System.currentTimeMillis() - startedAtMillis);
                if (remaining <= 0) {
                    AttributeInstance current = player.getAttribute(SCALE_ATTRIBUTE);
                    if (current != null) {
                        current.setBaseValue(originalScale);
                    }
                    bossBar.removeAll();
                    player.sendMessage("§b크기 변경 효과가 끝나 원래 크기로 돌아왔습니다.");
                    cancel();
                    return;
                }

                double progress = Math.max(0.0, Math.min(1.0, remaining / (double) durationMillis));
                bossBar.setProgress(progress);
                bossBar.setTitle("크기 변경 남은 시간: " + (remaining / 1000 + 1) + "초");
            }
        }.runTaskTimer(plugin, 0L, 20L);

        return true;
    }

    private static Attribute resolveScaleAttribute() {
        try {
            return Attribute.valueOf("GENERIC_SCALE");
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
