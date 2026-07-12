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

    // 스핀 횟수(11회), 매 스핀 사이 2틱 대기
    private static final int SPIN_COUNT = 11;
    private static final long TICKS_BETWEEN_SPINS = 2L;
    private static final long TICKS_AFTER_WINNER = 30L;

    /**
     * playRouletteEffect 연출의 총 소요 시간(틱). Skript의 wait 구문은 이 값을(skript-reflect가
     * 반환하는 Java 숫자 타입) 파싱하지 못하므로 스크립트에서는 이 값을 계산해 리터럴로 적어야 한다.
     */
    public static final long TOTAL_EFFECT_TICKS = SPIN_COUNT * TICKS_BETWEEN_SPINS + TICKS_AFTER_WINNER;

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

    /**
     * 스크립트에서 대상을 먼저 뽑아 쓰고 싶을 때 사용. 룰렛 연출 없이 target.resolve(donor)의
     * 첫 번째(RANDOM인 경우 무작위로 뽑힌 유일한) 대상을 즉시 반환한다. 대상이 없으면 null.
     */
    public Player pickTarget(Player donor, DonationTarget target) {
        List<Player> candidates = target.resolve(donor);
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    /**
     * 스크립트에서 pickTarget으로 이미 뽑은 당첨자를 대상으로 룰렛 연출(타이틀 애니메이션)만 재생한다.
     * 연출은 비동기로 진행되며 완료를 기다리지 않는다(연출이 끝나기 전에 이미 결과가 정해져 있으므로
     * 실제 액션은 연출과 무관하게 바로 실행해도 무방하다).
     */
    public void playRouletteEffect(Player winner) {
        if (winner == null) {
            return;
        }
        playRoulette(winner, () -> {
        });
    }

    private void playRoulette(Player winner, Runnable onFinished) {
        List<Player> online = List.copyOf(Bukkit.getOnlinePlayers());
        if (online.isEmpty()) {
            onFinished.run();
            return;
        }

        new BukkitRunnable() {
            int step = 0;
            long ticksUntilNextSpin = 0;
            long ticksUntilFinish = -1;

            @Override
            public void run() {
                if (ticksUntilFinish >= 0) {
                    if (ticksUntilFinish == 0) {
                        onFinished.run();
                        cancel();
                        return;
                    }
                    ticksUntilFinish--;
                    return;
                }

                if (ticksUntilNextSpin > 0) {
                    ticksUntilNextSpin--;
                    return;
                }

                if (step >= SPIN_COUNT) {
                    announceWinner(online, winner);
                    ticksUntilFinish = TICKS_AFTER_WINNER;
                    return;
                }

                Player shown = online.get(random.nextInt(online.size()));
                broadcastTitle(online, ChatColor.YELLOW + "[대상 추첨]", ChatColor.WHITE + shown.getName());
                broadcastSound(online, Sound.UI_BUTTON_CLICK, 1f, 1.5f);

                ticksUntilNextSpin = TICKS_BETWEEN_SPINS;
                step++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void announceWinner(List<Player> online, Player winner) {
        broadcastTitle(online, ChatColor.GREEN + "[대상 추첨]", ChatColor.WHITE + "당첨: " + winner.getName());
        broadcastSound(online, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
    }

    private void broadcastTitle(List<Player> online, String title, String subtitle) {
        for (Player player : online) {
            player.sendTitle(title, subtitle, 0, 20, 0);
        }
    }

    private void broadcastSound(List<Player> online, Sound sound, float volume, float pitch) {
        for (Player player : online) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}
