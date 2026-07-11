package dev.gotiger.gTDonationEvent.action.chat.punch;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class ChatPunchSession {

    private static final String WORD = "펀치";

    private final long startedAtMillis;
    private final long durationMillis;
    private final BossBar bossBar;

    private int totalPunchCount;

    public ChatPunchSession(long durationMillis) {
        this.startedAtMillis = System.currentTimeMillis();
        this.durationMillis = durationMillis;
        this.bossBar = Bukkit.createBossBar(
                "남은 시간: " + (durationMillis / 1000) + "초 (펀치 0회)",
                BarColor.YELLOW,
                BarStyle.SOLID
        );
    }

    public boolean matches(String message) {
        return message.contains(WORD);
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
        bossBar.setTitle("남은 시간: " + Math.max(0, remaining / 1000) + "초 (펀치 " + totalPunchCount + "회)");
    }

    public int addPunchCount(int count) {
        totalPunchCount += count;
        return totalPunchCount;
    }
}
