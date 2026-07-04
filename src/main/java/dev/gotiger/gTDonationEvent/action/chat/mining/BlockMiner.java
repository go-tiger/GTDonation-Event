package dev.gotiger.gTDonationEvent.action.chat.mining;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class BlockMiner {

    private static final int MAX_RAY_DISTANCE = 8;

    public int mineAround(Player player, int radius) {
        Block center = findTargetBlock(player);
        if (center == null) {
            return 0;
        }

        int minedCount = 0;
        int half = radius / 2;
        for (int x = -half; x < radius - half; x++) {
            for (int y = -half; y < radius - half; y++) {
                for (int z = -half; z < radius - half; z++) {
                    Block block = center.getRelative(x, y, z);
                    if (!block.getType().isAir()) {
                        block.breakNaturally();
                        minedCount++;
                    }
                }
            }
        }
        return minedCount;
    }

    private Block findTargetBlock(Player player) {
        Location eye = player.getEyeLocation();
        RayTraceResult result = player.getWorld().rayTraceBlocks(eye, eye.getDirection(), MAX_RAY_DISTANCE);
        return result != null ? result.getHitBlock() : null;
    }
}
