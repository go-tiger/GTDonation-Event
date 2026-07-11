package dev.gotiger.gTDonationEvent.action.mob.monster;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeakMonsterAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public WeakMonsterAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "WEAK_MONSTER";
    }

    @Override
    public void execute(Player target, int amount) {
        spawnMonster(target);
    }

    public Entity spawnMonster(Player target) {
        EntityType entityType = pickRandomEntityType();
        if (entityType == null) {
            return null;
        }

        int radius = plugin.getConfig().getInt("weak-monster.radius", 3);
        Location center = target.getLocation();
        double offsetX = (random.nextDouble() * 2 - 1) * radius;
        double offsetZ = (random.nextDouble() * 2 - 1) * radius;
        Location spawnLocation = center.clone().add(offsetX, 0, offsetZ);

        return target.getWorld().spawnEntity(spawnLocation, entityType);
    }

    private EntityType pickRandomEntityType() {
        List<EntityType> available = new ArrayList<>();
        for (String name : plugin.getConfig().getStringList("weak-monster.candidates")) {
            try {
                available.add(EntityType.valueOf(name));
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (available.isEmpty()) {
            return null;
        }

        return available.get(random.nextInt(available.size()));
    }
}
