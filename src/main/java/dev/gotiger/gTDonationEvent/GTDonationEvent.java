package dev.gotiger.gTDonationEvent;

import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.action.animal.RandomAnimalAction;
import dev.gotiger.gTDonationEvent.action.buff.RandomBuffAction;
import dev.gotiger.gTDonationEvent.action.enchant.EnchantFairyManager;
import dev.gotiger.gTDonationEvent.action.enchant.EnchantScrollManager;
import dev.gotiger.gTDonationEvent.action.chat.mining.ChatMiningManager;
import dev.gotiger.gTDonationEvent.action.chat.shooting.ChatShootingManager;
import dev.gotiger.gTDonationEvent.action.food.BreadAction;
import dev.gotiger.gTDonationEvent.action.food.ExpBottleAction;
import dev.gotiger.gTDonationEvent.action.food.SteakAction;
import dev.gotiger.gTDonationEvent.action.item.TotemAction;
import dev.gotiger.gTDonationEvent.action.scarecrow.ScarecrowManager;
import dev.gotiger.gTDonationEvent.action.special.SpecialItemManager;
import dev.gotiger.gTDonationEvent.action.xray.XrayManager;
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
        actionRegistry.register(new TotemAction());
        actionRegistry.register(new ExpBottleAction(this));
        actionRegistry.register(new RandomBuffAction(this));
        actionRegistry.register(new RandomAnimalAction(this));

        ChatMiningManager chatMiningManager = new ChatMiningManager(this);
        ChatShootingManager chatShootingManager = new ChatShootingManager(this);
        ScarecrowManager scarecrowManager = new ScarecrowManager(this);
        XrayManager xrayManager = new XrayManager(this);
        SpecialItemManager specialItemManager = new SpecialItemManager(this);
        EnchantScrollManager enchantScrollManager = new EnchantScrollManager(this);
        EnchantFairyManager enchantFairyManager = new EnchantFairyManager(this);
        scriptAPI = new DonationScriptAPI(actionRegistry, chatMiningManager, chatShootingManager, scarecrowManager, xrayManager, specialItemManager, enchantScrollManager, enchantFairyManager);

        getServer().getPluginManager().registerEvents(
                new DonationEventListener(this, donationConfig, actionRegistry),
                this
        );
        getServer().getPluginManager().registerEvents(chatMiningManager, this);
        getServer().getPluginManager().registerEvents(chatShootingManager, this);
        getServer().getPluginManager().registerEvents(scarecrowManager, this);
        getServer().getPluginManager().registerEvents(enchantScrollManager, this);

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
