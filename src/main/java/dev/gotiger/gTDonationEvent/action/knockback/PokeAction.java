package dev.gotiger.gTDonationEvent.action.knockback;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Random;

public class PokeAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public PokeAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "POKE";
    }

    @Override
    public void execute(Player target, int amount) {
        double strength = plugin.getConfig().getDouble("poke.strength", 1.0);

        double angle = random.nextDouble() * Math.PI * 2;
        Vector direction = new Vector(Math.cos(angle), 0, Math.sin(angle));
        Vector velocity = direction.multiply(strength);
        velocity.setY(target.getVelocity().getY());
        target.setVelocity(velocity);

        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1.2f);
    }
}
