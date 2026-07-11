package dev.gotiger.gTDonationEvent.action.movement.knockback;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Random;

public class KnockbackAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public KnockbackAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "KNOCKBACK";
    }

    @Override
    public void execute(Player target, int amount) {
        double strength = plugin.getConfig().getDouble("knockback.strength", 2.0);
        double upward = plugin.getConfig().getDouble("knockback.upward", 1.2);

        double angle = random.nextDouble() * Math.PI * 2;
        Vector direction = new Vector(Math.cos(angle), 0, Math.sin(angle));
        Vector velocity = direction.multiply(strength).setY(upward);
        target.setVelocity(velocity);

        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 1f, 0.8f);
    }
}
