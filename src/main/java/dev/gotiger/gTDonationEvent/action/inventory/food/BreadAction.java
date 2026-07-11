package dev.gotiger.gTDonationEvent.action.inventory.food;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BreadAction implements DonationAction {

    @Override
    public String getName() {
        return "BREAD";
    }

    @Override
    public void execute(Player target, int amount) {
        target.getInventory().addItem(new ItemStack(Material.BREAD, amount));
    }
}
