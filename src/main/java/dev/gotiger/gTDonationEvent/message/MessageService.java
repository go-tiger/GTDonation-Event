package dev.gotiger.gTDonationEvent.message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MessageService {

    private final JavaPlugin plugin;
    private YamlConfiguration messages;

    public MessageService(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(file);

        try (InputStream defaultStream = plugin.getResource("messages.yml")) {
            if (defaultStream != null) {
                YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaultStream, StandardCharsets.UTF_8)
                );
                messages.setDefaults(defaults);
            }
        } catch (Exception ignored) {
        }
    }

    public String get(String key) {
        String raw = messages.getString(key, key);
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public String get(String key, Map<String, String> placeholders) {
        String result = get(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return result;
    }
}
