package dev.gotiger.gTDonationEvent.action.misc.xray;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XrayManager {

    private final JavaPlugin plugin;

    public XrayManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int reveal(Player player) {
        int radius = plugin.getConfig().getInt("xray.radius", 8);
        int durationSeconds = plugin.getConfig().getInt("xray.duration-seconds", 60);
        Map<Material, ChatColor> oreColors = loadOreColors();

        Location center = player.getLocation();
        List<BlockDisplay> displays = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = center.clone().add(x, y, z).getBlock();
                    ChatColor color = oreColors.get(block.getType());
                    if (color == null) {
                        continue;
                    }
                    displays.add(spawnMarker(block, color));
                }
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (BlockDisplay display : displays) {
                    if (!display.isDead()) {
                        display.remove();
                    }
                }
            }
        }.runTaskLater(plugin, durationSeconds * 20L);

        return displays.size();
    }

    private BlockDisplay spawnMarker(Block block, ChatColor color) {
        Location location = block.getLocation();
        BlockData blockData = block.getBlockData();

        BlockDisplay display = block.getWorld().spawn(location, BlockDisplay.class);
        display.setBlock(blockData);
        display.setTransformation(new Transformation(
                new Vector3f(0, 0, 0),
                new AxisAngle4f(0, 0, 0, 1),
                new Vector3f(1.002f, 1.002f, 1.002f),
                new AxisAngle4f(0, 0, 0, 1)
        ));
        display.setGlowing(true);
        applyGlowColor(display, color);

        return display;
    }

    private void applyGlowColor(BlockDisplay display, ChatColor color) {
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        String teamName = "gtde_xray_" + color.name();
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setColor(color);
        }
        team.addEntry(display.getUniqueId().toString());
    }

    private Map<Material, ChatColor> loadOreColors() {
        Map<Material, ChatColor> result = new HashMap<>();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("xray.ores");
        if (section == null) {
            return result;
        }

        for (String key : section.getKeys(false)) {
            Material material = Material.matchMaterial(key);
            if (material == null) {
                continue;
            }
            try {
                ChatColor color = ChatColor.valueOf(section.getString(key));
                result.put(material, color);
            } catch (IllegalArgumentException ignored) {
            }
        }

        return result;
    }
}
