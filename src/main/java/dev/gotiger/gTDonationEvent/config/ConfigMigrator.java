package dev.gotiger.gTDonationEvent.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ConfigMigrator {

    // 새 버전에서 config 구조가 바뀌면 여기에 등록: migrations.put("1.1", config -> config.set("new-option", "default-value"));
    private final Map<String, Consumer<FileConfiguration>> migrations = new LinkedHashMap<>();

    public void migrate(Plugin plugin, FileConfiguration config) {
        String currentVersion = config.getString("config-version", "1.0");
        String latestVersion = plugin.getDescription().getVersion();

        if (currentVersion.equals(latestVersion)) {
            return;
        }

        boolean changed = false;
        for (Map.Entry<String, Consumer<FileConfiguration>> migration : migrations.entrySet()) {
            if (isOlderThan(currentVersion, migration.getKey())) {
                migration.getValue().accept(config);
                changed = true;
            }
        }

        config.set("config-version", latestVersion);
        if (changed) {
            plugin.saveConfig();
        }
    }

    private boolean isOlderThan(String version, String than) {
        String[] a = version.split("\\.");
        String[] b = than.split("\\.");

        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            int partA = i < a.length ? Integer.parseInt(a[i]) : 0;
            int partB = i < b.length ? Integer.parseInt(b[i]) : 0;
            if (partA != partB) {
                return partA < partB;
            }
        }
        return false;
    }
}
