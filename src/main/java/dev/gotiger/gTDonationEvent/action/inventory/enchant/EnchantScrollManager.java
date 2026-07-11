package dev.gotiger.gTDonationEvent.action.inventory.enchant;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantScrollManager implements Listener {

    private static final String DISPLAY_NAME = ChatColor.LIGHT_PURPLE + "즉석 인챈트권";

    private final JavaPlugin plugin;
    private final NamespacedKey key;
    private final Random random = new Random();

    public EnchantScrollManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "enchant_scroll");
    }

    public ItemStack createScroll() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(DISPLAY_NAME);
        meta.setLore(List.of(ChatColor.GRAY + "인챈트권을 든 채로 아이템을 클릭하면", ChatColor.GRAY + "무작위 인챈트가 즉시 적용됩니다."));
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack cursor = event.getCursor();
        ItemStack target = event.getCurrentItem();

        if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT
                && event.getClick() != ClickType.CREATIVE) {
            return;
        }

        if (!isScroll(cursor) || target == null || target.getType() == Material.AIR || isScroll(target)) {
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        Enchantment enchantment = pickRandomEnchantment(target);
        if (enchantment == null) {
            player.sendMessage(ChatColor.RED + "이 아이템에는 적용 가능한 인챈트가 없습니다.");
            return;
        }

        int level = 1 + random.nextInt(enchantment.getMaxLevel());
        target.addUnsafeEnchantment(enchantment, level);

        cursor.setAmount(cursor.getAmount() - 1);
        event.setCursor(cursor.getAmount() <= 0 ? null : cursor);

        player.sendMessage(ChatColor.LIGHT_PURPLE + "즉석 인챈트권 사용! " + ChatColor.GOLD
                + enchantment.getKey().getKey() + " " + level + ChatColor.LIGHT_PURPLE + "이(가) 적용되었습니다.");
    }

    private boolean isScroll(ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    private Enchantment pickRandomEnchantment(ItemStack target) {
        List<Enchantment> candidates = new ArrayList<>();
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.canEnchantItem(target)) {
                candidates.add(enchantment);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(random.nextInt(candidates.size()));
    }
}
