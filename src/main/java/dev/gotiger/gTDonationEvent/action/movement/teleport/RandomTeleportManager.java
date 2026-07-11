package dev.gotiger.gTDonationEvent.action.movement.teleport;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.Set;

public class RandomTeleportManager {

    private static final Set<Material> UNSAFE_GROUND = Set.of(
            Material.WATER, Material.LAVA,
            Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.BIRCH_LEAVES,
            Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES,
            Material.MANGROVE_LEAVES, Material.CHERRY_LEAVES, Material.AZALEA_LEAVES,
            Material.FLOWERING_AZALEA_LEAVES
    );

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public RandomTeleportManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void teleport(Player player) {
        int radius = plugin.getConfig().getInt("random-teleport.radius", 200);

        new BukkitRunnable() {
            int secondsLeft = 3;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                if (secondsLeft > 0) {
                    player.sendTitle(ChatColor.AQUA + "" + ChatColor.BOLD + secondsLeft, ChatColor.GRAY + "잠시 후 랜덤 위치로 이동합니다", 0, 25, 5);
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                    secondsLeft--;
                    return;
                }

                Location destination = findSafeLocation(player.getWorld(), player.getLocation(), radius);
                player.teleport(destination);
                player.getWorld().playSound(destination, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                player.sendTitle(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "이동!", "", 0, 20, 10);
                cancel();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private Location findSafeLocation(World world, Location origin, int radius) {
        for (int attempt = 0; attempt < 30; attempt++) {
            double offsetX = (random.nextDouble() * 2 - 1) * radius;
            double offsetZ = (random.nextDouble() * 2 - 1) * radius;
            Location candidate = origin.clone().add(offsetX, 0, offsetZ);

            Block highestBlock = world.getHighestBlockAt(candidate);
            if (UNSAFE_GROUND.contains(highestBlock.getType())) {
                continue;
            }

            Location safeLocation = highestBlock.getLocation().add(0.5, 1, 0.5);
            safeLocation.setYaw(origin.getYaw());
            safeLocation.setPitch(origin.getPitch());
            return safeLocation;
        }

        return origin;
    }
}
