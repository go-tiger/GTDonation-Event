package dev.gotiger.gTDonationEvent.action.scarecrow;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScarecrowManager implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Integer> hitCounts = new ConcurrentHashMap<>();

    public ScarecrowManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ArmorStand spawn(Player player) {
        Location front = player.getLocation().clone();
        front.add(front.getDirection().normalize().multiply(2));
        front.setY(player.getLocation().getY());

        ArmorStand scarecrow = player.getWorld().spawn(front, ArmorStand.class);
        scarecrow.setGravity(false);
        scarecrow.setInvulnerable(false);
        scarecrow.setCustomNameVisible(true);
        scarecrow.setCustomName("맞은 횟수: 0");
        scarecrow.setGlowing(true);
        applyWhiteGlow(scarecrow);
        faceTowards(scarecrow, player.getLocation());

        EntityEquipment equipment = scarecrow.getEquipment();
        if (equipment != null) {
            equipment.setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
            equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        }
        scarecrow.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
        scarecrow.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        scarecrow.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
        scarecrow.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);

        int durationSeconds = plugin.getConfig().getInt("scarecrow.duration-seconds", 60);
        UUID id = scarecrow.getUniqueId();
        hitCounts.put(id, 0);

        new BukkitRunnable() {
            @Override
            public void run() {
                hitCounts.remove(id);
                if (!scarecrow.isDead()) {
                    scarecrow.remove();
                }
            }
        }.runTaskLater(plugin, durationSeconds * 20L);

        return scarecrow;
    }

    private void faceTowards(ArmorStand scarecrow, Location targetLocation) {
        Location scarecrowLocation = scarecrow.getLocation();
        double dx = targetLocation.getX() - scarecrowLocation.getX();
        double dz = targetLocation.getZ() - scarecrowLocation.getZ();
        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        scarecrow.setRotation(yaw, scarecrow.getLocation().getPitch());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.ARMOR_STAND) {
            return;
        }
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        UUID id = event.getEntity().getUniqueId();
        Integer count = hitCounts.get(id);
        if (count == null) {
            return;
        }

        event.setCancelled(true);

        count++;
        hitCounts.put(id, count);
        event.getEntity().setCustomName("맞은 횟수: " + count);

        Location hitLocation = event.getEntity().getLocation().add(0, 1, 0);
        event.getEntity().getWorld().playSound(hitLocation, Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.5f, 1f);
        event.getEntity().getWorld().spawnParticle(Particle.CRIT, hitLocation, 10, 0.3, 0.3, 0.3, 0.05);

        Material foodMaterial = resolveFoodMaterial();
        int foodCount = plugin.getConfig().getInt("scarecrow.food-count", 1);
        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(foodMaterial, foodCount));
    }

    private Material resolveFoodMaterial() {
        String name = plugin.getConfig().getString("scarecrow.food-material", "CARROT");
        Material material = Material.matchMaterial(name);
        return material != null ? material : Material.CARROT;
    }

    private void applyWhiteGlow(ArmorStand scarecrow) {
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("gtde_scarecrow");
        if (team == null) {
            team = scoreboard.registerNewTeam("gtde_scarecrow");
            team.setColor(ChatColor.WHITE);
        }
        team.addEntry(scarecrow.getUniqueId().toString());
    }
}
