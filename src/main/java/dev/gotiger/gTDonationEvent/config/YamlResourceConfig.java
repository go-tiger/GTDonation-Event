package dev.gotiger.gTDonationEvent.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * jar에 번들된 기본 리소스를 데이터 폴더로 복사해 로드하고,
 * 데이터 폴더 파일에 없는 키는 jar의 기본값으로 채워주는 YAML 설정의 공통 베이스.
 */
public abstract class YamlResourceConfig {

    private final JavaPlugin plugin;
    private final String resourceName;

    protected YamlResourceConfig(JavaPlugin plugin, String resourceName) {
        this.plugin = plugin;
        this.resourceName = resourceName;
    }

    protected final YamlConfiguration loadYaml() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file());

        try (InputStream defaultStream = plugin.getResource(resourceName)) {
            if (defaultStream != null) {
                YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaultStream, StandardCharsets.UTF_8)
                );
                config.setDefaults(defaults);
            }
        } catch (Exception ignored) {
        }

        return config;
    }

    protected final void save(YamlConfiguration config) {
        try {
            config.save(file());
        } catch (IOException e) {
            plugin.getLogger().warning(resourceName + " 저장 실패: " + e.getMessage());
        }
    }

    protected final File file() {
        File file = new File(plugin.getDataFolder(), resourceName);
        if (!file.exists()) {
            plugin.saveResource(resourceName, false);
        }
        return file;
    }

    protected final JavaPlugin plugin() {
        return plugin;
    }
}
