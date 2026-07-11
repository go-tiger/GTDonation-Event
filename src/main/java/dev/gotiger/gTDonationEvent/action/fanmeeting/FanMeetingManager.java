package dev.gotiger.gTDonationEvent.action.fanmeeting;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class FanMeetingManager {

    private static final List<String> RANDOM_SKIN_NAMES = List.of(
            "MHF_Steve", "MHF_Alex", "Notch", "Herobrine", "Technoblade", "Dream"
    );

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public FanMeetingManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int summon(Player player, String donorName) {
        int minCount = plugin.getConfig().getInt("fan-meeting.min-count", 5);
        int maxCount = plugin.getConfig().getInt("fan-meeting.max-count", 10);
        int radius = plugin.getConfig().getInt("fan-meeting.radius", 5);

        int count = minCount + random.nextInt(Math.max(1, maxCount - minCount + 1));
        Location center = player.getLocation();
        String label = ChatColor.LIGHT_PURPLE + "[팬] " + ChatColor.WHITE + donorName;

        for (int i = 0; i < count; i++) {
            double offsetX = (random.nextDouble() * 2 - 1) * radius;
            double offsetZ = (random.nextDouble() * 2 - 1) * radius;
            Location spawnLocation = center.clone().add(offsetX, 0, offsetZ);

            Zombie zombie = (Zombie) player.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
            zombie.setBaby(false);
            zombie.setCustomName(label);
            zombie.setCustomNameVisible(true);

            EntityEquipment equipment = zombie.getEquipment();
            if (equipment != null) {
                equipment.setHelmet(createPlayerHead());
                equipment.setHelmetDropChance(0f);
            }
        }

        return count;
    }

    private ItemStack createPlayerHead() {
        String skinName = RANDOM_SKIN_NAMES.get(random.nextInt(RANDOM_SKIN_NAMES.size()));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(skinName);
        skull.setItemMeta(meta);
        return skull;
    }
}
