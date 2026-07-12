package dev.gotiger.gTDonationEvent.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class TargetExclusionConfig extends YamlResourceConfig {

    private boolean excludeSpectator;
    private boolean excludeOp;
    private Set<UUID> excludedPlayerIds = new HashSet<>();

    public TargetExclusionConfig(JavaPlugin plugin) {
        super(plugin, "targets.yml");
        load();
    }

    public void load() {
        YamlConfiguration config = loadYaml();

        excludeSpectator = config.getBoolean("exclusion.exclude-spectator", true);
        excludeOp = config.getBoolean("exclusion.exclude-op", false);

        Set<UUID> ids = new HashSet<>();
        for (String rawId : config.getStringList("exclusion.exclude-players")) {
            try {
                ids.add(UUID.fromString(rawId));
            } catch (IllegalArgumentException ignored) {
            }
        }
        excludedPlayerIds = ids;
    }

    public boolean isExcluded(Player player) {
        if (excludeSpectator && player.getGameMode() == org.bukkit.GameMode.SPECTATOR) {
            return true;
        }
        if (excludeOp && player.isOp()) {
            return true;
        }
        return excludedPlayerIds.contains(player.getUniqueId());
    }

    public boolean isPlayerExcluded(UUID playerId) {
        return excludedPlayerIds.contains(playerId);
    }

    public boolean excludePlayer(UUID playerId) {
        boolean added = excludedPlayerIds.add(playerId);
        if (added) {
            persistExcludedPlayers();
        }
        return added;
    }

    public boolean includePlayer(UUID playerId) {
        boolean removed = excludedPlayerIds.remove(playerId);
        if (removed) {
            persistExcludedPlayers();
        }
        return removed;
    }

    public UUID resolvePlayerId(String playerName) {
        Player online = Bukkit.getPlayerExact(playerName);
        if (online != null) {
            return online.getUniqueId();
        }
        return Bukkit.getOfflinePlayer(playerName).getUniqueId();
    }

    private void persistExcludedPlayers() {
        YamlConfiguration config = loadYaml();
        List<String> sorted = new ArrayList<>();
        for (UUID id : new TreeSet<>(excludedPlayerIds)) {
            sorted.add(id.toString());
        }
        config.set("exclusion.exclude-players", sorted);
        save(config);
    }
}
