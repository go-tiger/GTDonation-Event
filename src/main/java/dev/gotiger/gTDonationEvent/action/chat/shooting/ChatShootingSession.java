package dev.gotiger.gTDonationEvent.action.chat.shooting;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class ChatShootingSession {

    private final String word;
    private final long startedAtMillis;
    private final long durationMillis;
    private final BossBar bossBar;

    private int totalShotCount;

    public ChatShootingSession(String word, long durationMillis) {
        this.word = word;
        this.startedAtMillis = System.currentTimeMillis();
        this.durationMillis = durationMillis;
        this.bossBar = Bukkit.createBossBar(
                "남은 시간: " + (durationMillis / 1000) + "초 (발사 0회, " + describeKeyword() + ")",
                BarColor.RED,
                BarStyle.SOLID
        );
    }

    public boolean matches(String message) {
        return word == null || message.contains(word);
    }

    private String describeKeyword() {
        return word == null ? "모든 채팅" : "\"" + word + "\"이 포함된 채팅";
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
        bossBar.setTitle("남은 시간: " + Math.max(0, remaining / 1000) + "초 (발사 " + totalShotCount + "회, " + describeKeyword() + ")");
    }

    public int addShotCount(int count) {
        totalShotCount += count;
        return totalShotCount;
    }
}
