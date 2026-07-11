package dev.gotiger.gTDonationEvent;

import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.action.animal.RandomAnimalAction;
import dev.gotiger.gTDonationEvent.action.buff.RandomBuffAction;
import dev.gotiger.gTDonationEvent.action.chat.mining.ChatMiningManager;
import dev.gotiger.gTDonationEvent.action.chat.shooting.ChatShootingManager;
import dev.gotiger.gTDonationEvent.action.food.BreadAction;
import dev.gotiger.gTDonationEvent.action.food.ExpBottleAction;
import dev.gotiger.gTDonationEvent.action.food.SteakAction;
import dev.gotiger.gTDonationEvent.command.DebugCommand;
import dev.gotiger.gTDonationEvent.config.ConfigMigrator;
import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.listener.DonationEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class GTDonationEvent extends JavaPlugin {

    private DonationConfig donationConfig;
    private DonationActionRegistry actionRegistry;
    private DonationScriptAPI scriptAPI;
    private boolean scriptMode;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        new ConfigMigrator().migrate(this, getConfig());

        scriptMode = getConfig().getBoolean("script-mode", true);

        donationConfig = new DonationConfig();
        donationConfig.load(getConfig());

        actionRegistry = new DonationActionRegistry();
        actionRegistry.register(new BreadAction());
        actionRegistry.register(new SteakAction());
        actionRegistry.register(new ExpBottleAction(this));
        actionRegistry.register(new RandomBuffAction(this));
        actionRegistry.register(new RandomAnimalAction(this));

        ChatMiningManager chatMiningManager = new ChatMiningManager(this);
        ChatShootingManager chatShootingManager = new ChatShootingManager(this);
        scriptAPI = new DonationScriptAPI(actionRegistry, chatMiningManager, chatShootingManager);

        getServer().getPluginManager().registerEvents(
                new DonationEventListener(this, donationConfig, actionRegistry),
                this
        );
        getServer().getPluginManager().registerEvents(chatMiningManager, this);
        getServer().getPluginManager().registerEvents(chatShootingManager, this);

        getCommand("gtdonationevent").setExecutor(new DebugCommand(this));
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

    public DonationScriptAPI getScriptAPI() {
        return scriptAPI;
    }
}
