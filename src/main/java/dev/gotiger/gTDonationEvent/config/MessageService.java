package dev.gotiger.gTDonationEvent.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class MessageService extends YamlResourceConfig {

    private YamlConfiguration messages;

    public MessageService(JavaPlugin plugin) {
        super(plugin, "messages.yml");
        load();
    }

    public void load() {
        messages = loadYaml();
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
