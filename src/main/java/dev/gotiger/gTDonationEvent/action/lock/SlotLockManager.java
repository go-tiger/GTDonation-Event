package dev.gotiger.gTDonationEvent.action.lock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlotLockManager implements Listener {

    private static final String DISPLAY_NAME = ChatColor.DARK_GRAY + "잠긴 슬롯";

    private final JavaPlugin plugin;
    private final NamespacedKey key;
    private final Random random = new Random();

    public SlotLockManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "locked_slot_marker");
    }

    public boolean lock(Player player) {
        PlayerInventory inventory = player.getInventory();

        List<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            if (!isMarker(inventory.getItem(i))) {
                candidates.add(i);
            }
        }
        if (candidates.isEmpty()) {
            return false;
        }

        int slot = candidates.get(random.nextInt(candidates.size()));
        inventory.setItem(slot, createMarker());

        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        if (isMarker(event.getCurrentItem()) || isMarker(event.getCursor())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        if (isMarker(event.getOldCursor())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(this::isMarker);
    }

    private ItemStack createMarker() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(DISPLAY_NAME);
        meta.setLore(List.of(ChatColor.GRAY + "영구적으로 사용할 수 없는 슬롯입니다."));
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isMarker(ItemStack item) {
        if (item == null || item.getType() != Material.BARRIER || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}
