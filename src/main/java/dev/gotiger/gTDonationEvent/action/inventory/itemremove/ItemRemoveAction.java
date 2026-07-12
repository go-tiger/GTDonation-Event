package dev.gotiger.gTDonationEvent.action.inventory.itemremove;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemRemoveAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public ItemRemoveAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "ITEM_REMOVE";
    }

    @Override
    public void execute(Player target, int amount) {
        PlayerInventory inventory = target.getInventory();
        List<Integer> filledSlots = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            if (inventory.getItem(i) != null) {
                filledSlots.add(i);
            }
        }

        if (filledSlots.isEmpty()) {
            return;
        }

        int slot = filledSlots.get(random.nextInt(filledSlots.size()));
        ItemStack item = inventory.getItem(slot);
        ItemStack whole = item.clone();

        inventory.setItem(slot, null);

        Item droppedItem = target.getWorld().dropItem(target.getLocation(), whole);
        droppedItem.setFireTicks(Integer.MAX_VALUE);
        droppedItem.setPickupDelay(Integer.MAX_VALUE);
        droppedItem.setVelocity(droppedItem.getVelocity().multiply(0.2));
        target.getWorld().playSound(target.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1f, 1f);

        int burnDurationTicks = plugin.getConfig().getInt("item-remove.burn-duration-ticks", 30);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!droppedItem.isDead()) {
                    droppedItem.remove();
                }
            }
        }.runTaskLater(plugin, burnDurationTicks);
    }
}
