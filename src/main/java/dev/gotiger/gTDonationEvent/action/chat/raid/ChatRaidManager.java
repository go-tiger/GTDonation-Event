package dev.gotiger.gTDonationEvent.action.chat.raid;

import dev.gotiger.gTDonationCore.event.ChatEvent;
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

public class ChatRaidManager implements Listener {

    private final Plugin plugin;
    private final Random random = new Random();
    private final Map<UUID, ChatRaidSession> sessions = new ConcurrentHashMap<>();

    public ChatRaidManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start(Player watched, int seconds) {
        long durationMillis = seconds * 1000L;
        int maxParticipants = plugin.getConfig().getInt("chat-raid.max-participants", 10);
        UUID id = watched.getUniqueId();

        ChatRaidSession previous = sessions.remove(id);
        if (previous != null) {
            previous.getBossBar().removeAll();
        }

        ChatRaidSession session = new ChatRaidSession(durationMillis, maxParticipants);
        sessions.put(id, session);
        session.getBossBar().addPlayer(watched);

        new BukkitRunnable() {
            @Override
            public void run() {
                ChatRaidSession current = sessions.get(id);
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

    @EventHandler
    public void onChat(ChatEvent event) {
        Player watched = event.getPlayer();
        ChatRaidSession session = sessions.get(watched.getUniqueId());
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

        if (!session.tryJoin(event.getChatterName())) {
            return;
        }

        spawnPillager(watched, event.getChatterName());
    }

    private void spawnPillager(Player watched, String chatterName) {
        int radius = plugin.getConfig().getInt("chat-raid.spawn-radius", 10);
        Location center = watched.getLocation();
        double offsetX = (random.nextDouble() * 2 - 1) * radius;
        double offsetZ = (random.nextDouble() * 2 - 1) * radius;
        Location spawnLocation = center.clone().add(offsetX, 0, offsetZ);

        Monster pillager = (Monster) watched.getWorld().spawnEntity(spawnLocation, EntityType.PILLAGER);
        pillager.setCustomName(ChatColor.RED + "[레이드] " + ChatColor.WHITE + chatterName);
        pillager.setCustomNameVisible(true);
        pillager.setTarget(watched);

        watched.getServer().broadcastMessage(
                ChatColor.RED + "[채팅 레이드] " + ChatColor.WHITE + chatterName + ChatColor.GRAY + "님이 약탈자로 참여했습니다!"
        );
    }
}
