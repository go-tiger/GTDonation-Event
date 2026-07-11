package dev.gotiger.gTDonationEvent.action.diamondcurse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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

public class DiamondCurseManager implements Listener {

    private static final Set<Material> DIAMOND_BLOCKS = Set.of(
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE
    );

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private final Set<UUID> cursedPlayers = new HashSet<>();

    public DiamondCurseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isCursed(Player player) {
        return cursedPlayers.contains(player.getUniqueId());
    }

    public boolean curse(Player player) {
        if (isCursed(player)) {
            return false;
        }

        int durationSeconds = plugin.getConfig().getInt("diamond-curse.duration-seconds", 60);
        cursedPlayers.add(player.getUniqueId());
        player.sendMessage(ChatColor.DARK_RED + "다이아의 저주에 걸렸습니다! 다이아몬드를 캐면 저주가 발동합니다.");

        new BukkitRunnable() {
            @Override
            public void run() {
                cursedPlayers.remove(player.getUniqueId());
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.GRAY + "다이아의 저주가 풀렸습니다.");
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
        if (!DIAMOND_BLOCKS.contains(event.getBlock().getType())) {
            return;
        }

        PotionEffectType effectType = pickDebuffType();
        int durationTicks = plugin.getConfig().getInt("diamond-curse.debuff-duration-seconds", 10) * 20;
        player.addPotionEffect(new PotionEffect(effectType, durationTicks, 1));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1f, 0.8f);
        player.sendMessage(ChatColor.DARK_RED + "다이아의 저주가 발동했습니다!");
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
