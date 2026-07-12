package dev.gotiger.gTDonationEvent.action.chat.shooting;

import dev.gotiger.gTDonationCore.event.ChatEvent;
import dev.gotiger.gTDonationEvent.config.DonationTarget;
import dev.gotiger.gTDonationEvent.config.MessageService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatShootingManager implements Listener {

    private final JavaPlugin plugin;
    private final MessageService messages;
    private final ProjectileShooter projectileShooter = new ProjectileShooter();
    private final Map<UUID, ChatShootingSession> sessions = new ConcurrentHashMap<>();

    public ChatShootingManager(JavaPlugin plugin, MessageService messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    public void start(Player donor, int seconds, String word, DonationTarget target) {
        long durationMillis = seconds * 1000L;

        for (Player watched : target.resolve(donor)) {
            UUID id = watched.getUniqueId();

            ChatShootingSession previous = sessions.remove(id);
            if (previous != null) {
                previous.getBossBar().removeAll();
            }

            ChatShootingSession session = new ChatShootingSession(word, durationMillis);
            sessions.put(id, session);
            session.getBossBar().addPlayer(watched);

            new BukkitRunnable() {
                @Override
                public void run() {
                    ChatShootingSession current = sessions.get(id);
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
        ChatShootingSession session = sessions.get(watched.getUniqueId());
        if (session == null) {
            return;
        }

        if (session.isExpired()) {
            session.getBossBar().removeAll();
            sessions.remove(watched.getUniqueId(), session);
            return;
        }

        if (!session.matches(event.getMessage())) {
            return;
        }

        double fireballChance = plugin.getConfig().getDouble("chat-shooting.fireball-chance", 0.2);
        boolean isFireball = projectileShooter.shoot(watched, event.getChatterName(), fireballChance);
        int totalShotCount = session.addShotCount(1);
        String projectileName = messages.get(isFireball ? "chat-shooting.projectile-fireball" : "chat-shooting.projectile-arrow");
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("message", event.getMessage());
        placeholders.put("projectile", projectileName);
        placeholders.put("count", String.valueOf(totalShotCount));
        watched.sendMessage(messages.get("chat-shooting.broadcast", placeholders));
    }
}
