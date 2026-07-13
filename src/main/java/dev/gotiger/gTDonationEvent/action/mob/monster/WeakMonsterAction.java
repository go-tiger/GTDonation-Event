package dev.gotiger.gTDonationEvent.action.mob.monster;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class WeakMonsterAction implements DonationAction {

    public static final String CHICKEN_JOCKEY_KEY = "CHICKEN_JOCKEY";

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
        String key = pickRandomCandidate();
        if (key == null) {
            return null;
        }
        return spawnMonster(target, key);
    }

    public Entity spawnMonster(Player target, EntityType entityType) {
        return spawnMonster(target, entityType.name());
    }

    public Entity spawnMonster(Player target, String key) {
        Location spawnLocation = randomLocationAround(target);

        if (CHICKEN_JOCKEY_KEY.equals(key)) {
            return spawnChickenJockey(target, spawnLocation);
        }

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(key);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return target.getWorld().spawnEntity(spawnLocation, entityType);
    }

    private Entity spawnChickenJockey(Player target, Location spawnLocation) {
        Chicken chicken = (Chicken) target.getWorld().spawnEntity(spawnLocation, EntityType.CHICKEN);
        Zombie zombie = (Zombie) target.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
        zombie.setBaby();
        chicken.addPassenger(zombie);
        return zombie;
    }

    private Location randomLocationAround(Player target) {
        int radius = plugin.getConfig().getInt("weak-monster.radius", 3);
        Location center = target.getLocation();
        double offsetX = (random.nextDouble() * 2 - 1) * radius;
        double offsetZ = (random.nextDouble() * 2 - 1) * radius;
        return center.clone().add(offsetX, 0, offsetZ);
    }

    /**
     * @deprecated CHICKEN_JOCKEY 같은 합성 후보를 표현할 수 없어 {@link #pickRandomCandidate()}로 대체됨
     */
    @Deprecated
    public EntityType pickRandomEntityType() {
        String key = pickRandomCandidate();
        if (key == null || CHICKEN_JOCKEY_KEY.equals(key)) {
            return null;
        }
        try {
            return EntityType.valueOf(key);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String pickRandomCandidate() {
        List<String> candidates = plugin.getConfig().getStringList("weak-monster.candidates");
        List<String> available = candidates.stream()
                .filter(this::isValidCandidate)
                .toList();

        if (available.isEmpty()) {
            return null;
        }

        return available.get(random.nextInt(available.size()));
    }

    private boolean isValidCandidate(String key) {
        if (CHICKEN_JOCKEY_KEY.equals(key)) {
            return true;
        }
        try {
            EntityType.valueOf(key);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
