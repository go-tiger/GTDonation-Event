package dev.gotiger.gTDonationEvent.action.inventory.special;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class SpecialItemManager {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public SpecialItemManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void spin(Player player, Consumer<Material> onFinished) {
        List<Material> candidates = loadCandidates();
        if (candidates.isEmpty()) {
            return;
        }

        Material result = candidates.get(random.nextInt(candidates.size()));

        Location displayLocation = player.getEyeLocation().clone()
                .add(player.getLocation().getDirection().normalize().multiply(2));

        ItemDisplay display = player.getWorld().spawn(displayLocation, ItemDisplay.class);
        display.setItemStack(new ItemStack(candidates.get(random.nextInt(candidates.size()))));

        int spinSeconds = plugin.getConfig().getInt("special-item.spin-seconds", 2);
        long totalTicks = spinSeconds * 20L;

        new BukkitRunnable() {
            long elapsedTicks = 0;
            long nextSwapAt = 0;
            long interval = 2;

            @Override
            public void run() {
                if (display.isDead()) {
                    cancel();
                    return;
                }

                if (elapsedTicks >= totalTicks) {
                    display.setItemStack(new ItemStack(result));
                    player.getWorld().playSound(display.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!display.isDead()) {
                                display.remove();
                            }
                            giveItem(player, result);
                            onFinished.accept(result);
                        }
                    }.runTaskLater(plugin, 20L);

                    cancel();
                    return;
                }

                if (elapsedTicks >= nextSwapAt) {
                    display.setItemStack(new ItemStack(candidates.get(random.nextInt(candidates.size()))));
                    player.getWorld().playSound(display.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);

                    double progress = elapsedTicks / (double) totalTicks;
                    interval = 2 + Math.round(progress * 8);
                    nextSwapAt = elapsedTicks + interval;
                }

                elapsedTicks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void giveItem(Player player, Material material) {
        ItemStack itemStack = new ItemStack(material);
        String dropMode = plugin.getConfig().getString("special-item.drop-mode", "INVENTORY_FIRST");

        if ("DROP_ONLY".equalsIgnoreCase(dropMode)) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            return;
        }

        List<ItemStack> leftover = new ArrayList<>(player.getInventory().addItem(itemStack).values());
        for (ItemStack remaining : leftover) {
            player.getWorld().dropItemNaturally(player.getLocation(), remaining);
        }
    }

    private List<Material> loadCandidates() {
        List<Material> result = new ArrayList<>();
        for (String name : plugin.getConfig().getStringList("special-item.candidates")) {
            Material material = Material.matchMaterial(name);
            if (material != null) {
                result.add(material);
            }
        }
        return result;
    }
}
