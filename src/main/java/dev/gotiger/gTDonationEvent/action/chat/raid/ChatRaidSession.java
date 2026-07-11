package dev.gotiger.gTDonationEvent.action.chat.raid;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.HashSet;
import java.util.Set;

public class ChatRaidSession {

    private static final String JOIN_COMMAND = "!참여";

    private final long startedAtMillis;
    private final long durationMillis;
    private final int maxParticipants;
    private final BossBar bossBar;
    private final Set<String> joinedChatters = new HashSet<>();

    public ChatRaidSession(long durationMillis, int maxParticipants) {
        this.startedAtMillis = System.currentTimeMillis();
        this.durationMillis = durationMillis;
        this.maxParticipants = maxParticipants;
        this.bossBar = Bukkit.createBossBar(
                "채팅 레이드! 남은 시간: " + (durationMillis / 1000) + "초 (참여 0/" + maxParticipants + ")",
                BarColor.RED,
                BarStyle.SOLID
        );
    }

    public boolean matches(String message) {
        return message.contains(JOIN_COMMAND);
    }

    public boolean isFull() {
        return joinedChatters.size() >= maxParticipants;
    }

    public boolean tryJoin(String chatterName) {
        if (isFull()) {
            return false;
        }
        return joinedChatters.add(chatterName);
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startedAtMillis >= durationMillis || isFull();
    }

    public void updateBossBar() {
        long remaining = durationMillis - (System.currentTimeMillis() - startedAtMillis);
        double progress = Math.max(0.0, Math.min(1.0, remaining / (double) durationMillis));
        bossBar.setProgress(progress);
        bossBar.setTitle("채팅 레이드! 남은 시간: " + Math.max(0, remaining / 1000) + "초 (참여 " + joinedChatters.size() + "/" + maxParticipants + ")");
    }
}
