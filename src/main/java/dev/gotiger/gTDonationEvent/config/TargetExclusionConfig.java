package dev.gotiger.gTDonationEvent.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class TargetExclusionConfig {

    private final JavaPlugin plugin;
    private boolean excludeSpectator;
    private boolean excludeOp;
    private Set<UUID> excludedPlayerIds = new HashSet<>();

    public TargetExclusionConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file());

        try (InputStream defaultStream = plugin.getResource("targets.yml")) {
            if (defaultStream != null) {
                YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaultStream, StandardCharsets.UTF_8)
                );
                config.setDefaults(defaults);
            }
        } catch (Exception ignored) {
        }

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
        File file = file();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> sorted = new ArrayList<>();
        for (UUID id : new TreeSet<>(excludedPlayerIds)) {
            sorted.add(id.toString());
        }
        config.set("exclusion.exclude-players", sorted);

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("targets.yml 저장 실패: " + e.getMessage());
        }
    }

    private File file() {
        File file = new File(plugin.getDataFolder(), "targets.yml");
        if (!file.exists()) {
            plugin.saveResource("targets.yml", false);
        }
        return file;
    }
}
