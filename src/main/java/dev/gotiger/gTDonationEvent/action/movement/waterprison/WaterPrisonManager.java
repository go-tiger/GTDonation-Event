package dev.gotiger.gTDonationEvent.action.movement.waterprison;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class WaterPrisonManager implements Listener {

    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private final Map<UUID, Session> sessions = new HashMap<>();

    public WaterPrisonManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isTrapped(Player player) {
        return sessions.containsKey(player.getUniqueId());
    }

    public boolean trap(Player player) {
        if (isTrapped(player)) {
            return false;
        }

        int sequenceLength = plugin.getConfig().getInt("water-prison.sequence-length", 4);
        int timeoutSeconds = plugin.getConfig().getInt("water-prison.timeout-seconds", 60);

        int radius = plugin.getConfig().getInt("water-prison.radius", 1);
        int height = plugin.getConfig().getInt("water-prison.height", 2);

        Location origin = player.getLocation().clone();
        List<RestoreEntry> restoreEntries = buildCage(origin, radius, height);
        List<Direction> sequence = randomSequence(sequenceLength);

        Session session = new Session(origin, restoreEntries, sequence);
        sessions.put(player.getUniqueId(), session);

        player.getWorld().playSound(origin, Sound.BLOCK_WATER_AMBIENT, 1f, 0.8f);
        player.sendMessage(ChatColor.AQUA + "물 감옥에 갇혔습니다! 표시되는 방향으로 시야를 돌려 탈출하세요.");
        updateTitle(player, session);

        session.timeoutTask = new BukkitRunnable() {
            @Override
            public void run() {
                release(player, false);
            }
        };
        session.timeoutTask.runTaskLater(plugin, timeoutSeconds * 20L);

        return true;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Session session = sessions.get(player.getUniqueId());
        if (session == null || session.awaitingNextTitle) {
            return;
        }

        if (event.getTo() == null) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - session.lastDetectionMillis < 250) {
            return;
        }

        float dYaw = deltaAngle(event.getTo().getYaw(), event.getFrom().getYaw());
        float dPitch = event.getTo().getPitch() - event.getFrom().getPitch();

        double threshold = 8.0;
        if (Math.abs(dYaw) < threshold && Math.abs(dPitch) < threshold) {
            return;
        }

        Direction moved;
        if (Math.abs(dYaw) >= Math.abs(dPitch)) {
            moved = dYaw > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            moved = dPitch > 0 ? Direction.DOWN : Direction.UP;
        }

        session.lastDetectionMillis = now;

        Direction expected = session.sequence.get(session.progress);
        session.awaitingNextTitle = true;

        if (moved == expected) {
            player.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + key(moved), ChatColor.GREEN + "성공!", 0, 8, 4);
            session.progress++;
            if (session.progress >= session.sequence.size()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (sessions.get(player.getUniqueId()) == session) {
                            player.sendMessage(ChatColor.GREEN + "탈출 성공!");
                            release(player, true);
                        }
                    }
                }.runTaskLater(plugin, 8L);
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (sessions.get(player.getUniqueId()) == session) {
                            session.awaitingNextTitle = false;
                            updateTitle(player, session);
                        }
                    }
                }.runTaskLater(plugin, 8L);
            }
        } else {
            player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + key(moved), ChatColor.RED + "실패! 처음부터 다시", 0, 8, 4);
            session.progress = 0;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (sessions.get(player.getUniqueId()) == session) {
                        session.awaitingNextTitle = false;
                        updateTitle(player, session);
                    }
                }
            }.runTaskLater(plugin, 8L);
        }
    }

    private void updateTitle(Player player, Session session) {
        Direction next = session.sequence.get(session.progress);
        player.sendTitle(ChatColor.AQUA + "" + ChatColor.BOLD + key(next), describeSequence(session.sequence.subList(session.progress, session.sequence.size())), 0, 30, 10);
    }

    private float deltaAngle(float to, float from) {
        float delta = (to - from) % 360f;
        if (delta > 180f) {
            delta -= 360f;
        } else if (delta < -180f) {
            delta += 360f;
        }
        return delta;
    }

    private void release(Player player, boolean success) {
        Session session = sessions.remove(player.getUniqueId());
        if (session == null) {
            return;
        }

        if (session.timeoutTask != null) {
            session.timeoutTask.cancel();
        }

        for (RestoreEntry entry : session.restoreEntries) {
            entry.block.setBlockData(entry.originalData);
        }

        if (!success) {
            player.sendMessage(ChatColor.GRAY + "시간이 초과되어 물 감옥이 해제되었습니다.");
        }

        player.getWorld().playSound(player.getLocation(), Sound.ITEM_BUCKET_EMPTY, 1f, 1f);
    }

    private List<RestoreEntry> buildCage(Location center, int radius, int height) {
        List<RestoreEntry> restoreEntries = new ArrayList<>();
        Block feet = center.getBlock();

        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y <= height; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = feet.getRelative(x, y, z);
                    restoreEntries.add(new RestoreEntry(block, block.getBlockData()));

                    boolean isShell = x == -radius || x == radius || z == -radius || z == radius || y == height;
                    block.setType(isShell ? Material.GLASS : Material.WATER);
                }
            }
        }

        return restoreEntries;
    }

    private List<Direction> randomSequence(int length) {
        Direction[] all = Direction.values();
        List<Direction> sequence = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            sequence.add(all[random.nextInt(all.length)]);
        }
        return sequence;
    }

    private String describeSequence(List<Direction> sequence) {
        StringBuilder builder = new StringBuilder();
        for (Direction direction : sequence) {
            builder.append(key(direction)).append(" ");
        }
        return builder.toString().trim();
    }

    private String key(Direction direction) {
        return switch (direction) {
            case UP -> "▲";
            case DOWN -> "▼";
            case LEFT -> "◀";
            case RIGHT -> "▶";
        };
    }

    private static class Session {
        final Location origin;
        final List<RestoreEntry> restoreEntries;
        final List<Direction> sequence;
        int progress;
        long lastDetectionMillis;
        boolean awaitingNextTitle;
        BukkitRunnable timeoutTask;

        Session(Location origin, List<RestoreEntry> restoreEntries, List<Direction> sequence) {
            this.origin = origin;
            this.restoreEntries = restoreEntries;
            this.sequence = sequence;
        }
    }

    private static class RestoreEntry {
        final Block block;
        final BlockData originalData;

        RestoreEntry(Block block, BlockData originalData) {
            this.block = block;
            this.originalData = originalData;
        }
    }
}
