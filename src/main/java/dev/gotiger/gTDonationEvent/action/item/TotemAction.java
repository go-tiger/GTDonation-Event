package dev.gotiger.gTDonationEvent.action.item;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TotemAction implements DonationAction {

    @Override
    public String getName() {
        return "TOTEM";
    }

    @Override
    public void execute(Player target, int amount) {
        target.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING, amount));
    }
}
