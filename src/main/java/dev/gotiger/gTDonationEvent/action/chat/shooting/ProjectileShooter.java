package dev.gotiger.gTDonationEvent.action.chat.shooting;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.util.Random;

public class ProjectileShooter {

    private final Random random = new Random();

    public boolean shoot(Player player, String nameTag, double fireballChance) {
        boolean isFireball = random.nextDouble() < fireballChance;

        if (isFireball) {
            nameTag(player.launchProjectile(Fireball.class), ChatColor.GOLD + nameTag);
        } else {
            nameTag(player.launchProjectile(Arrow.class), nameTag);
        }

        return isFireball;
    }

    private void nameTag(Projectile projectile, String nameTag) {
        projectile.setCustomName(nameTag);
        projectile.setCustomNameVisible(true);
    }
}
