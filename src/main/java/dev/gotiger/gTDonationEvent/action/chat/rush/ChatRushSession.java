package dev.gotiger.gTDonationEvent.action.chat.rush;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class ChatRushSession {

    private final long startedAtMillis;
    private final long durationMillis;
    private final BossBar bossBar;

    private int totalSummonedCount;

    public ChatRushSession(long durationMillis) {
        this.startedAtMillis = System.currentTimeMillis();
        this.durationMillis = durationMillis;
        this.bossBar = Bukkit.createBossBar(
                "채팅 러시! 남은 시간: " + (durationMillis / 1000) + "초 (소환 0마리)",
                BarColor.GREEN,
                BarStyle.SOLID
        );
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startedAtMillis >= durationMillis;
    }

    public void updateBossBar() {
        long remaining = durationMillis - (System.currentTimeMillis() - startedAtMillis);
        double progress = Math.max(0.0, Math.min(1.0, remaining / (double) durationMillis));
        bossBar.setProgress(progress);
        bossBar.setTitle("채팅 러시! 남은 시간: " + Math.max(0, remaining / 1000) + "초 (소환 " + totalSummonedCount + "마리)");
    }

    public int addSummonedCount(int count) {
        totalSummonedCount += count;
        return totalSummonedCount;
    }

    public int getTotalSummonedCount() {
        return totalSummonedCount;
    }
}
