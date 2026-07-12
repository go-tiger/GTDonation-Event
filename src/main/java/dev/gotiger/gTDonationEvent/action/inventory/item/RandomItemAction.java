package dev.gotiger.gTDonationEvent.action.inventory.item;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomItemAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public RandomItemAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "RANDOM_ITEM";
    }

    @Override
    public void execute(Player target, int amount) {
        giveRandomItem(target);
    }

    public Material giveRandomItem(Player target) {
        Material material = pickRandomMaterial();
        if (material == null) {
            return null;
        }
        giveItem(target, material);
        return material;
    }

    public void giveItem(Player target, Material material) {
        var leftover = target.getInventory().addItem(new ItemStack(material));
        for (var remaining : leftover.values()) {
            target.getWorld().dropItemNaturally(target.getLocation(), remaining);
        }
    }

    public Material pickRandomMaterial() {
        Set<Material> excluded = new HashSet<>();
        for (String name : plugin.getConfig().getStringList("random-item.exclude-items")) {
            Material material = Material.matchMaterial(name);
            if (material != null) {
                excluded.add(material);
            }
        }

        List<Material> available = new ArrayList<>();
        for (Material material : Material.values()) {
            if (!material.isItem() || material.isLegacy() || excluded.contains(material)) {
                continue;
            }
            available.add(material);
        }

        if (available.isEmpty()) {
            return null;
        }

        return available.get(random.nextInt(available.size()));
    }
}
