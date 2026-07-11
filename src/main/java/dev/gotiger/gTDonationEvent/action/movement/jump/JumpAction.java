package dev.gotiger.gTDonationEvent.action.movement.jump;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class JumpAction implements DonationAction {

    private final JavaPlugin plugin;

    public JumpAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "JUMP";
    }

    @Override
    public void execute(Player target, int amount) {
        double marginHealth = plugin.getConfig().getDouble("jump.margin-health", 1.0);
        double safeFallDistance = Math.max(0, target.getHealth() - marginHealth) + 3;

        double velocityY = 0.4 * Math.sqrt(safeFallDistance);
        target.setVelocity(new Vector(0, velocityY, 0));
        target.setFallDistance(0f);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.7f);
    }
}
