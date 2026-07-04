package dev.gotiger.gTDonationEvent.action.chat.mining;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class ChatMiningSession {

    private final String word1;
    private final String word2;
    private final int radius;
    private final long startedAtMillis;
    private final long durationMillis;
    private final BossBar bossBar;

    private boolean awaitingWord2;
    private String word1Chatter;
    private int totalMinedCount;

    public ChatMiningSession(String word1, String word2, int radius, long durationMillis) {
        this.word1 = word1;
        this.word2 = word2;
        this.radius = radius;
        this.startedAtMillis = System.currentTimeMillis();
        this.durationMillis = durationMillis;
        this.bossBar = Bukkit.createBossBar(
                "남은 시간: " + (durationMillis / 1000) + "초 (" + describeKeyword() + ")",
                BarColor.YELLOW,
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
        bossBar.setTitle("남은 시간: " + Math.max(0, remaining / 1000) + "초 (" + describeKeyword() + ")");
    }

    private String describeKeyword() {
        if (word1 == null) {
            return "모든 채팅";
        }
        String keyword = word2 == null ? word1 : word1 + " -> " + word2;
        return "\"" + keyword + "\"이 포함된 채팅";
    }

    public int getRadius() {
        return radius;
    }

    public int addMinedCount(int count) {
        totalMinedCount += count;
        return totalMinedCount;
    }

    public String onChatMessage(String chatterName, String message) {
        if (word1 == null) {
            return chatterName;
        }

        if (word2 == null) {
            return message.contains(word1) ? chatterName : null;
        }

        if (awaitingWord2) {
            String triggeredBy = message.contains(word2) ? (word1Chatter + " + " + chatterName) : null;
            boolean nowAwaiting = message.contains(word1);
            awaitingWord2 = nowAwaiting;
            word1Chatter = nowAwaiting ? chatterName : null;
            return triggeredBy;
        }

        if (message.contains(word1)) {
            awaitingWord2 = true;
            word1Chatter = chatterName;
        }
        return null;
    }
}
