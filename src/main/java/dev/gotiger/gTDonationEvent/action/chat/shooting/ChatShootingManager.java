package dev.gotiger.gTDonationEvent.action.chat.shooting;

import dev.gotiger.gTDonationCore.event.ChatEvent;
import dev.gotiger.gTDonationEvent.config.DonationTarget;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatShootingManager implements Listener {

    private final JavaPlugin plugin;
    private final ProjectileShooter projectileShooter = new ProjectileShooter();
    private final Map<UUID, ChatShootingSession> sessions = new ConcurrentHashMap<>();

    public ChatShootingManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start(Player donor, int seconds, DonationTarget target) {
        long durationMillis = seconds * 1000L;

        for (Player watched : target.resolve(donor)) {
            UUID id = watched.getUniqueId();

            ChatShootingSession previous = sessions.remove(id);
            if (previous != null) {
                previous.getBossBar().removeAll();
            }

            ChatShootingSession session = new ChatShootingSession(durationMillis);
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

        double fireballChance = plugin.getConfig().getDouble("chat-shooting.fireball-chance", 0.2);
        boolean isFireball = projectileShooter.shoot(watched, event.getChatterName(), fireballChance);
        int totalShotCount = session.addShotCount(1);
        String projectileName = isFireball ? ChatColor.GOLD + "화염구" + ChatColor.GRAY : "화살";
        watched.getServer().broadcastMessage(
                ChatColor.RED + "[채팅 사격] " + ChatColor.WHITE + event.getMessage() + ChatColor.GRAY + " " + projectileName + " 발사! ("
                        + ChatColor.YELLOW + totalShotCount + "번째" + ChatColor.GRAY + ")"
        );
    }
}
