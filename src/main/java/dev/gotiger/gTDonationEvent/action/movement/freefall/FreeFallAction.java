package dev.gotiger.gTDonationEvent.action.movement.freefall;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FreeFallAction implements DonationAction {

    private final JavaPlugin plugin;

    public FreeFallAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "FREE_FALL";
    }

    @Override
    public void execute(Player target, int amount) {
        int height = plugin.getConfig().getInt("free-fall.height", 100);

        Location destination = target.getLocation().clone();
        double maxY = target.getWorld().getMaxHeight() - 5;
        destination.setY(Math.min(destination.getY() + height, maxY));

        target.teleport(destination);
        target.setFallDistance(0f);
        target.getWorld().playSound(destination, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.2f);
    }
}
