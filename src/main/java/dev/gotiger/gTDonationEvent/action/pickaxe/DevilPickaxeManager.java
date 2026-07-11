package dev.gotiger.gTDonationEvent.action.pickaxe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DevilPickaxeManager implements Listener {

    private static final String DISPLAY_NAME = ChatColor.DARK_RED + "악마의 곡괭이";

    private final JavaPlugin plugin;
    private final NamespacedKey markerKey;

    public DevilPickaxeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.markerKey = new NamespacedKey(plugin, "devil_pickaxe");
    }

    public ItemStack createPickaxe() {
        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(DISPLAY_NAME);
        meta.setLore(List.of(ChatColor.GRAY + "캐는 블록을 중심으로 3x3x3 범위를 함께 채굴합니다."));
        meta.getPersistentDataContainer().set(markerKey, PersistentDataType.BYTE, (byte) 1);

        if (meta instanceof Damageable damageable) {
            damageable.setDamage(0);
        }

        item.setItemMeta(meta);
        return item;
    }

    private int damagePerUse() {
        int useCount = Math.max(1, plugin.getConfig().getInt("devil-pickaxe.use-count", 10));
        int maxDurability = Material.NETHERITE_PICKAXE.getMaxDurability();
        return Math.max(1, maxDurability / useCount);
    }

    private boolean isDevilPickaxe(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_PICKAXE || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(markerKey, PersistentDataType.BYTE);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!isDevilPickaxe(item)) {
            return;
        }

        Block center = event.getBlock();

        for (Block block : getSurroundingBlocks(center)) {
            if (block.equals(center) || block.getType().isAir()) {
                continue;
            }
            block.breakNaturally(item);
        }

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable damageable) {
            int newDamage = damageable.getDamage() + damagePerUse();
            int maxDurability = Material.NETHERITE_PICKAXE.getMaxDurability();
            if (newDamage >= maxDurability) {
                player.getInventory().setItemInMainHand(null);
                player.sendMessage(ChatColor.DARK_RED + "악마의 곡괭이가 힘을 다해 부서졌습니다.");
                return;
            }
            damageable.setDamage(newDamage);
            item.setItemMeta(meta);
        }
    }

    private List<Block> getSurroundingBlocks(Block center) {
        List<Block> result = new ArrayList<>(27);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    result.add(center.getRelative(x, y, z));
                }
            }
        }
        return result;
    }
}
