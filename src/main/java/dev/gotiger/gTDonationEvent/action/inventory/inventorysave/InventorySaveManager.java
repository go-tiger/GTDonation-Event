package dev.gotiger.gTDonationEvent.action.inventory.inventorysave;

import dev.gotiger.gTDonationEvent.action.hazard.instantdeath.InstantDeathManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class InventorySaveManager implements Listener {

    private static final String DISPLAY_NAME = ChatColor.GOLD + "인벤토리 세이브권";

    private final JavaPlugin plugin;
    private final NamespacedKey key;
    private final InstantDeathManager instantDeathManager;

    public InventorySaveManager(JavaPlugin plugin, InstantDeathManager instantDeathManager) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "inventory_save_ticket");
        this.instantDeathManager = instantDeathManager;
    }

    public ItemStack createTicket() {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(DISPLAY_NAME);
        meta.setLore(List.of(
                ChatColor.GRAY + "인벤토리에 가지고 있으면",
                ChatColor.GRAY + "사망 시 아이템을 보호해줍니다.",
                ChatColor.GRAY + "사용 시 이 아이템은 소모됩니다."
        ));
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isTicket(ItemStack item) {
        if (item == null || item.getType() != Material.WRITTEN_BOOK || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (instantDeathManager.isInstantDeath(player)) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; i++) {
            if (isTicket(contents[i])) {
                ItemStack ticket = contents[i];
                if (ticket.getAmount() > 1) {
                    ticket.setAmount(ticket.getAmount() - 1);
                } else {
                    inventory.setItem(i, null);
                }

                event.setKeepInventory(true);
                event.setKeepLevel(true);
                event.getDrops().clear();
                event.setDroppedExp(0);
                player.sendMessage(ChatColor.GOLD + "인벤토리 세이브권이 사용되었습니다.");
                return;
            }
        }
    }
}
