package dev.gotiger.gTDonationEvent.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * DonationTarget.RANDOM 대상이 확정될 때, 서버 전체 플레이어에게
 * 이름이 순환하는 룰렛 타이틀 연출을 보여준 뒤 실제 액션을 지연 실행한다.
 */
public class RandomTargetRouletteManager {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public RandomTargetRouletteManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * target이 RANDOM이면 룰렛 연출 후 onResolved를 지연 실행하고,
     * 그 외에는 target.resolve(donor)를 즉시 onResolved로 넘긴다.
     */
    public void resolve(Player donor, DonationTarget target, Consumer<List<Player>> onResolved) {
        if (target != DonationTarget.RANDOM) {
            onResolved.accept(target.resolve(donor));
            return;
        }

        List<Player> candidates = target.resolve(donor);
        if (candidates.isEmpty()) {
            onResolved.accept(candidates);
            return;
        }

        Player winner = candidates.get(0);
        playRoulette(winner, () -> onResolved.accept(candidates));
    }

    private void playRoulette(Player winner, Runnable onFinished) {
        List<Player> online = List.copyOf(Bukkit.getOnlinePlayers());
        if (online.isEmpty()) {
            onFinished.run();
            return;
        }

        int spinCount = plugin.getConfig().getInt("random-roulette.spin-count", 20);
        long intervalTicks = plugin.getConfig().getLong("random-roulette.interval-ticks", 3L);

        new BukkitRunnable() {
            int remaining = spinCount;

            @Override
            public void run() {
                if (remaining <= 0) {
                    announceWinner(online, winner);
                    onFinished.run();
                    cancel();
                    return;
                }

                Player shown = online.get(random.nextInt(online.size()));
                broadcastTitle(online, ChatColor.YELLOW + "" + ChatColor.BOLD + shown.getName(), ChatColor.GRAY + "룰렛 진행 중...");
                broadcastSound(online, Sound.UI_BUTTON_CLICK);
                remaining--;
            }
        }.runTaskTimer(plugin, 0L, intervalTicks);
    }

    private void announceWinner(List<Player> online, Player winner) {
        broadcastTitle(online, ChatColor.GOLD + "" + ChatColor.BOLD + winner.getName(), ChatColor.GREEN + "당첨!");
        broadcastSound(online, Sound.ENTITY_PLAYER_LEVELUP);
    }

    private void broadcastTitle(List<Player> online, String title, String subtitle) {
        for (Player player : online) {
            player.sendTitle(title, subtitle, 0, 8, 0);
        }
    }

    private void broadcastSound(List<Player> online, Sound sound) {
        for (Player player : online) {
            player.playSound(player.getLocation(), sound, 1f, 1f);
        }
    }
}
