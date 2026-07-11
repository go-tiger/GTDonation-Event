package dev.gotiger.gTDonationEvent.action.chat.punch;

import dev.gotiger.gTDonationCore.event.ChatEvent;
import dev.gotiger.gTDonationEvent.config.DonationTarget;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatPunchManager implements Listener {

    private final Plugin plugin;
    private final Random random = new Random();
    private final Map<UUID, ChatPunchSession> sessions = new ConcurrentHashMap<>();

    public ChatPunchManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start(Player donor, int seconds, DonationTarget target) {
        long durationMillis = seconds * 1000L;

        for (Player watched : target.resolve(donor)) {
            UUID id = watched.getUniqueId();

            ChatPunchSession previous = sessions.remove(id);
            if (previous != null) {
                previous.getBossBar().removeAll();
            }

            ChatPunchSession session = new ChatPunchSession(durationMillis);
            sessions.put(id, session);
            session.getBossBar().addPlayer(watched);

            new BukkitRunnable() {
                @Override
                public void run() {
                    ChatPunchSession current = sessions.get(id);
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
        ChatPunchSession session = sessions.get(watched.getUniqueId());
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

        punch(watched);
        int totalPunchCount = session.addPunchCount(1);
        watched.getServer().broadcastMessage(
                ChatColor.YELLOW + "[채팅 펀치] " + ChatColor.WHITE + event.getChatterName() + ChatColor.GRAY + "님의 펀치! (총 "
                        + ChatColor.GOLD + totalPunchCount + "회" + ChatColor.GRAY + ")"
        );
    }

    private void punch(Player watched) {
        double strength = plugin.getConfig().getDouble("chat-punch.strength", 1.5);
        double verticalStrength = plugin.getConfig().getDouble("chat-punch.vertical-strength", 0.6);

        double angle = random.nextDouble() * Math.PI * 2;
        Vector direction = new Vector(Math.cos(angle), 0, Math.sin(angle));
        Vector velocity = direction.multiply(strength);
        velocity.setY(verticalStrength);
        watched.setVelocity(velocity);

        watched.getWorld().playSound(watched.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1f, 1f);
    }
}
