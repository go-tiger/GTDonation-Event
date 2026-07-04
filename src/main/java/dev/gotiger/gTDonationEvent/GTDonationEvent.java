package dev.gotiger.gTDonationEvent;

import dev.gotiger.gTDonationEvent.action.BreadAction;
import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.action.SteakAction;
import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.config.DonationTarget;
import dev.gotiger.gTDonationEvent.listener.DonationEventListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class GTDonationEvent extends JavaPlugin {

    private DonationConfig donationConfig;
    private DonationActionRegistry actionRegistry;
    private boolean scriptMode;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("event.yml", false);

        scriptMode = getConfig().getBoolean("script-mode", true);

        File eventFile = new File(getDataFolder(), "event.yml");
        FileConfiguration eventConfig = YamlConfiguration.loadConfiguration(eventFile);

        donationConfig = new DonationConfig();
        donationConfig.load(eventConfig);

        actionRegistry = new DonationActionRegistry();
        actionRegistry.register(new BreadAction());
        actionRegistry.register(new SteakAction());

        getServer().getPluginManager().registerEvents(
                new DonationEventListener(this, donationConfig, actionRegistry),
                this
        );
    }

    public boolean isScriptMode() {
        return scriptMode;
    }

    @Override
    public void onDisable() {
    }

    public DonationActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    public void getBread(Player player, int amount) {
        getBread(player, amount, DonationTarget.PLAYER);
    }

    public void getBread(Player player, int amount, DonationTarget target) {
        BreadAction bread = (BreadAction) actionRegistry.get("BREAD")
                .orElseThrow(() -> new IllegalArgumentException("BREAD action is not registered"));

        for (Player recipient : target.resolve(player)) {
            bread.execute(recipient, amount);
        }
    }

    public void getSteak(Player player, int amount) {
        getSteak(player, amount, DonationTarget.PLAYER);
    }

    public void getSteak(Player player, int amount, DonationTarget target) {
        SteakAction steak = (SteakAction) actionRegistry.get("STEAK")
                .orElseThrow(() -> new IllegalArgumentException("STEAK action is not registered"));

        for (Player recipient : target.resolve(player)) {
            steak.execute(recipient, amount);
        }
    }
}
