package dev.gotiger.gTDonationEvent.action.status.heal;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class SuperHealAction implements DonationAction {

    @Override
    public String getName() {
        return "SUPER_HEAL";
    }

    @Override
    public void execute(Player target, int amount) {
        double maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        target.setHealth(maxHealth);
        target.setFoodLevel(20);
        target.setSaturation(20f);
        target.setExhaustion(0f);
        target.setFireTicks(0);
        target.getActivePotionEffects().forEach(effect -> target.removePotionEffect(effect.getType()));
    }
}
