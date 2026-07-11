package dev.gotiger.gTDonationEvent.action.monsterscan;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class MonsterScanManager {

    private static final String TEAM_NAME = "gtde_monster_scan";

    private final JavaPlugin plugin;

    public MonsterScanManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int scan(Player player) {
        int radius = plugin.getConfig().getInt("monster-scan.radius", 20);
        int durationSeconds = plugin.getConfig().getInt("monster-scan.duration-seconds", 15);

        List<LivingEntity> targets = new ArrayList<>();
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Monster monster && !monster.isDead()) {
                targets.add(monster);
            }
        }

        if (targets.isEmpty()) {
            return 0;
        }

        ensureTeam();
        for (LivingEntity target : targets) {
            target.setGlowing(true);
            getTeam().addEntry(target.getUniqueId().toString());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (LivingEntity target : targets) {
                    if (!target.isDead()) {
                        target.setGlowing(false);
                    }
                    getTeam().removeEntry(target.getUniqueId().toString());
                }
            }
        }.runTaskLater(plugin, durationSeconds * 20L);

        return targets.size();
    }

    private void ensureTeam() {
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(TEAM_NAME);
        if (team == null) {
            team = scoreboard.registerNewTeam(TEAM_NAME);
            team.setColor(ChatColor.WHITE);
        }
    }

    private Team getTeam() {
        return plugin.getServer().getScoreboardManager().getMainScoreboard().getTeam(TEAM_NAME);
    }
}
