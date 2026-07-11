package dev.gotiger.gTDonationEvent.action.miningcurse;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class MiningCurseManager implements Listener {

    private static final Set<Material> ORE_BLOCKS = Set.of(
            Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
            Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE,
            Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.NETHER_GOLD_ORE,
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
            Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE,
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
            Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE,
            Material.NETHER_QUARTZ_ORE, Material.ANCIENT_DEBRIS
    );

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private final Set<UUID> cursedPlayers = new HashSet<>();

    public MiningCurseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isCursed(Player player) {
        return cursedPlayers.contains(player.getUniqueId());
    }

    public boolean curse(Player player) {
        if (isCursed(player)) {
            return false;
        }

        int durationSeconds = plugin.getConfig().getInt("mining-curse.duration-seconds", 60);
        cursedPlayers.add(player.getUniqueId());
        player.sendMessage(ChatColor.DARK_RED + "광질 저주에 걸렸습니다! 광석을 캐면 저주가 발동합니다.");

        new BukkitRunnable() {
            @Override
            public void run() {
                cursedPlayers.remove(player.getUniqueId());
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.GRAY + "광질 저주가 풀렸습니다.");
                }
            }
        }.runTaskLater(plugin, durationSeconds * 20L);

        return true;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!cursedPlayers.contains(player.getUniqueId())) {
            return;
        }
        if (!ORE_BLOCKS.contains(event.getBlock().getType())) {
            return;
        }

        Location location = event.getBlock().getLocation().add(0.5, 0.5, 0.5);
        boolean summonMob = random.nextBoolean();

        if (summonMob) {
            EntityType mobType = pickMobType();
            if (mobType != null) {
                player.getWorld().spawnEntity(location, mobType);
                player.sendMessage(ChatColor.DARK_RED + "광질 저주가 발동해 몬스터가 나타났습니다!");
            }
        } else {
            PotionEffectType effectType = pickDebuffType();
            int durationTicks = plugin.getConfig().getInt("mining-curse.debuff-duration-seconds", 10) * 20;
            player.addPotionEffect(new PotionEffect(effectType, durationTicks, 1));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1f, 0.8f);
            player.sendMessage(ChatColor.DARK_RED + "광질 저주가 발동했습니다!");
        }
    }

    private EntityType pickMobType() {
        List<String> names = plugin.getConfig().getStringList("mining-curse.mob-candidates");
        if (names.isEmpty()) {
            return EntityType.SILVERFISH;
        }
        String name = names.get(random.nextInt(names.size()));
        try {
            return EntityType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return EntityType.SILVERFISH;
        }
    }

    private PotionEffectType pickDebuffType() {
        List<String> names = plugin.getConfig().getStringList("mining-curse.debuff-candidates");
        if (!names.isEmpty()) {
            String name = names.get(random.nextInt(names.size()));
            PotionEffectType type = PotionEffectType.getByName(name);
            if (type != null) {
                return type;
            }
        }
        return PotionEffectType.SLOW_DIGGING;
    }
}
