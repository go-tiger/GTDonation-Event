package dev.gotiger.gTDonationEvent.action.chat.rush;

import dev.gotiger.gTDonationCore.event.ChatEvent;
import dev.gotiger.gTDonationEvent.config.DonationTarget;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRushManager implements Listener {

    private final Plugin plugin;
    private final Random random = new Random();
    private final Map<UUID, ChatRushSession> sessions = new ConcurrentHashMap<>();

    public ChatRushManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start(Player donor, int seconds, DonationTarget target) {
        long durationMillis = seconds * 1000L;

        for (Player watched : target.resolve(donor)) {
            UUID id = watched.getUniqueId();

            ChatRushSession previous = sessions.remove(id);
            if (previous != null) {
                previous.getBossBar().removeAll();
            }

            ChatRushSession session = new ChatRushSession(durationMillis);
            sessions.put(id, session);
            session.getBossBar().addPlayer(watched);
            sendTitle(watched, session);

            new BukkitRunnable() {
                @Override
                public void run() {
                    ChatRushSession current = sessions.get(id);
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
        ChatRushSession session = sessions.get(watched.getUniqueId());
        if (session == null) {
            return;
        }

        if (session.isExpired()) {
            session.getBossBar().removeAll();
            sessions.remove(watched.getUniqueId(), session);
            return;
        }

        spawnSilverfish(watched, event.getMessage());
        session.addSummonedCount(1);
        sendTitle(watched, session);
    }

    private void sendTitle(Player watched, ChatRushSession session) {
        watched.sendTitle(
                ChatColor.GREEN + "" + ChatColor.BOLD + "좀벌레 습격 진행 중",
                ChatColor.WHITE + "채팅으로 좀벌레를 소환하세요! (" + session.getTotalSummonedCount() + "마리)",
                0, 30, 10
        );
    }

    private void spawnSilverfish(Player watched, String message) {
        int radius = plugin.getConfig().getInt("chat-rush.spawn-radius", 8);
        Location center = watched.getLocation();
        double offsetX = (random.nextDouble() * 2 - 1) * radius;
        double offsetZ = (random.nextDouble() * 2 - 1) * radius;
        Location spawnLocation = center.clone().add(offsetX, 0, offsetZ);

        Monster silverfish = (Monster) watched.getWorld().spawnEntity(spawnLocation, EntityType.SILVERFISH);
        silverfish.setCustomName(ChatColor.WHITE + message);
        silverfish.setCustomNameVisible(true);
        silverfish.setTarget(watched);
    }
}
