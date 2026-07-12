package dev.gotiger.gTDonationEvent.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DonationConfig {

    private final Map<Integer, DonationMapping> mappings = new HashMap<>();

    public void load(FileConfiguration config) {
        mappings.clear();

        ConfigurationSection eventsSection = config.getConfigurationSection("events");
        if (eventsSection == null) {
            return;
        }

        for (String key : eventsSection.getKeys(false)) {
            int amount = Integer.parseInt(key);
            ConfigurationSection entry = eventsSection.getConfigurationSection(key);
            if (entry == null) {
                continue;
            }

            String action = entry.getString("action");
            mappings.put(amount, new DonationMapping(action));
        }
    }

    public Optional<DonationMapping> getMapping(int amount) {
        return Optional.ofNullable(mappings.get(amount));
    }
}
