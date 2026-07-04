package dev.gotiger.gTDonationEvent.action.food;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class ExpBottleAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public ExpBottleAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "EXP_BOTTLE";
    }

    @Override
    public void execute(Player target, int amount) {
        throwBottles(target);
    }

    public int throwBottles(Player target) {
        int minCount = plugin.getConfig().getInt("exp-bottle.min-count", 3);
        int maxCount = plugin.getConfig().getInt("exp-bottle.max-count", 8);
        int count = minCount + random.nextInt(Math.max(1, maxCount - minCount + 1));

        Location front = target.getEyeLocation().add(target.getLocation().getDirection());
        for (int i = 0; i < count; i++) {
            ThrownExpBottle bottle = target.getWorld().spawn(front, ThrownExpBottle.class);
            bottle.setShooter(target);
            bottle.setVelocity(target.getLocation().getDirection().multiply(0.5));
        }
        return count;
    }
}
