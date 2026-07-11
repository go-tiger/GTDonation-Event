package dev.gotiger.gTDonationEvent.action.inventory.rotfood;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.EnumSet;
import java.util.Set;

public class RotFoodAction implements DonationAction {

    private static final Set<Material> FOOD_MATERIALS = EnumSet.noneOf(Material.class);

    static {
        for (Material material : Material.values()) {
            if (material.isEdible()) {
                FOOD_MATERIALS.add(material);
            }
        }
    }

    @Override
    public String getName() {
        return "ROT_FOOD";
    }

    @Override
    public void execute(Player target, int amount) {
        PlayerInventory inventory = target.getInventory();
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null || !FOOD_MATERIALS.contains(item.getType())) {
                continue;
            }
            if (item.getType() == Material.ROTTEN_FLESH) {
                continue;
            }

            inventory.setItem(i, new ItemStack(Material.ROTTEN_FLESH, item.getAmount()));
        }
    }
}
