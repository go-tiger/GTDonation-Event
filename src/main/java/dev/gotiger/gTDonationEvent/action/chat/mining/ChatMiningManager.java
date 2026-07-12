package dev.gotiger.gTDonationEvent.action.chat.mining;

import dev.gotiger.gTDonationCore.event.ChatEvent;
import dev.gotiger.gTDonationEvent.config.DonationTarget;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatMiningManager implements Listener {

    private final Plugin plugin;
    private final BlockMiner blockMiner = new BlockMiner();
    private final Map<UUID, ChatMiningSession> sessions = new ConcurrentHashMap<>();

    public ChatMiningManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start(Player donor, int seconds, String word1, String word2, int radius, DonationTarget target) {
        long durationMillis = seconds * 1000L;

        for (Player watched : target.resolve(donor)) {
            UUID id = watched.getUniqueId();

            ChatMiningSession previous = sessions.remove(id);
            if (previous != null) {
                previous.getBossBar().removeAll();
            }

            ChatMiningSession session = new ChatMiningSession(word1, word2, radius, durationMillis);
            sessions.put(id, session);
            session.getBossBar().addPlayer(watched);

            new BukkitRunnable() {
                @Override
                public void run() {
                    ChatMiningSession current = sessions.get(id);
                    if (current == null || current != session) {
                        cancel();
                        return;
                    }

                    if (session.isExpired()) {
                        session.getBossBar().removeAll();
                        sessions.remove(id, session);
                        cancel();
                        return;
                    }

                    session.updateBossBar();
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        Player watched = event.getPlayer();
        ChatMiningSession session = sessions.get(watched.getUniqueId());
        if (session == null) {
            return;
        }

        if (session.isExpired()) {
            session.getBossBar().removeAll();
            sessions.remove(watched.getUniqueId(), session);
            return;
        }

        String triggeredBy = session.onChatMessage(event.getChatterName(), event.getMessage());
        if (triggeredBy != null) {
            int minedCount = blockMiner.mineAround(watched, session.getRadius());
            int totalMinedCount = session.addMinedCount(minedCount);
            watched.sendMessage(
                    ChatColor.GREEN + triggeredBy + ChatColor.WHITE + "님이 블록 채굴! (총 "
                            + ChatColor.YELLOW + totalMinedCount + "개" + ChatColor.WHITE + ")"
            );
        }
    }
}
