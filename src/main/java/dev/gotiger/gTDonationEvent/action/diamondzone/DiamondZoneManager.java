package dev.gotiger.gTDonationEvent.action.diamondzone;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DiamondZoneManager {

    private final JavaPlugin plugin;

    public DiamondZoneManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int convert(Player player) {
        int radius = plugin.getConfig().getInt("diamond-zone.radius", 5);
        int durationSeconds = plugin.getConfig().getInt("diamond-zone.duration-seconds", 30);
        double convertChance = plugin.getConfig().getDouble("diamond-zone.convert-chance", 0.75);

        Location center = player.getLocation();
        List<RestoreEntry> converted = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = center.clone().add(x, y, z).getBlock();
                    if (!isConvertible(block.getType())) {
                        continue;
                    }
                    if (ThreadLocalRandom.current().nextDouble() >= convertChance) {
                        continue;
                    }

                    converted.add(new RestoreEntry(block, block.getBlockData()));
                    Material diamondOre = block.getY() < 0 ? Material.DEEPSLATE_DIAMOND_ORE : Material.DIAMOND_ORE;
                    block.setType(diamondOre);
                }
            }
        }

        if (converted.isEmpty()) {
            return 0;
        }

        player.getWorld().playSound(center, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 1.5f);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (RestoreEntry entry : converted) {
                    if (entry.block.getType() == Material.DIAMOND_ORE || entry.block.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
                        entry.block.setBlockData(entry.originalData);
                    }
                }

                if (player.isOnline()) {
                    player.sendMessage(ChatColor.AQUA + "다이아존 효과가 끝나 블록이 원래대로 돌아왔습니다.");
                }
            }
        }.runTaskLater(plugin, durationSeconds * 20L);

        return converted.size();
    }

    private boolean isConvertible(Material type) {
        if (type == Material.DIAMOND_ORE || type == Material.DEEPSLATE_DIAMOND_ORE) {
            return false;
        }
        return type.isBlock() && type.isSolid() && !type.isAir();
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
