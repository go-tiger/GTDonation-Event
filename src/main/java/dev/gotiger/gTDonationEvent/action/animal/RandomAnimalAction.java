package dev.gotiger.gTDonationEvent.action.animal;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomAnimalAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public RandomAnimalAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "ANIMAL";
    }

    @Override
    public void execute(Player target, int amount) {
        spawnAnimal(target);
    }

    public Entity spawnAnimal(Player target) {
        EntityType entityType = pickRandomEntityType();
        if (entityType == null) {
            return null;
        }

        int radius = plugin.getConfig().getInt("random-animal.radius", 1);
        Location center = target.getLocation();
        double offsetX = (random.nextDouble() * 2 - 1) * radius;
        double offsetZ = (random.nextDouble() * 2 - 1) * radius;
        Location spawnLocation = center.clone().add(offsetX, 0, offsetZ);

        return target.getWorld().spawnEntity(spawnLocation, entityType);
    }

    private EntityType pickRandomEntityType() {
        Set<EntityType> excluded = new HashSet<>();
        for (String name : plugin.getConfig().getStringList("random-animal.exclude-entities")) {
            try {
                excluded.add(EntityType.valueOf(name));
            } catch (IllegalArgumentException ignored) {
            }
        }

        List<EntityType> available = new ArrayList<>();
        for (EntityType type : EntityType.values()) {
            if (type == null || excluded.contains(type)) {
                continue;
            }
            if (type.getEntityClass() == null || !Animals.class.isAssignableFrom(type.getEntityClass())) {
                continue;
            }
            available.add(type);
        }

        if (available.isEmpty()) {
            return null;
        }

        return available.get(random.nextInt(available.size()));
    }
}
