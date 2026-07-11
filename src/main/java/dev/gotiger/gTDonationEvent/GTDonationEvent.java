package dev.gotiger.gTDonationEvent;

import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.action.animal.RandomAnimalAction;
import dev.gotiger.gTDonationEvent.action.buff.RandomBuffAction;
import dev.gotiger.gTDonationEvent.action.buff.RandomDebuffAction;
import dev.gotiger.gTDonationEvent.action.burn.BurnAction;
import dev.gotiger.gTDonationEvent.action.diamondzone.DiamondZoneManager;
import dev.gotiger.gTDonationEvent.action.enchant.EnchantFairyManager;
import dev.gotiger.gTDonationEvent.action.enchant.EnchantScrollManager;
import dev.gotiger.gTDonationEvent.action.chat.mining.ChatMiningManager;
import dev.gotiger.gTDonationEvent.action.chat.shooting.ChatShootingManager;
import dev.gotiger.gTDonationEvent.action.food.BreadAction;
import dev.gotiger.gTDonationEvent.action.firework.FireworkAction;
import dev.gotiger.gTDonationEvent.action.food.ExpBottleAction;
import dev.gotiger.gTDonationEvent.action.food.SteakAction;
import dev.gotiger.gTDonationEvent.action.frostbite.FrostbiteManager;
import dev.gotiger.gTDonationEvent.action.heal.AbsorptionAction;
import dev.gotiger.gTDonationEvent.action.heal.SuperHealAction;
import dev.gotiger.gTDonationEvent.action.inventorysave.InventorySaveManager;
import dev.gotiger.gTDonationEvent.action.item.RandomItemAction;
import dev.gotiger.gTDonationEvent.action.item.TotemAction;
import dev.gotiger.gTDonationEvent.action.knockback.KnockbackAction;
import dev.gotiger.gTDonationEvent.action.lock.SlotLockManager;
import dev.gotiger.gTDonationEvent.action.knockback.PokeAction;
import dev.gotiger.gTDonationEvent.action.lightning.LightningAction;
import dev.gotiger.gTDonationEvent.action.miningcurse.MiningCurseManager;
import dev.gotiger.gTDonationEvent.action.monster.MediumMonsterAction;
import dev.gotiger.gTDonationEvent.action.monster.StrongMonsterAction;
import dev.gotiger.gTDonationEvent.action.monster.WeakMonsterAction;
import dev.gotiger.gTDonationEvent.action.monsterscan.MonsterScanManager;
import dev.gotiger.gTDonationEvent.action.pickaxe.DevilPickaxeManager;
import dev.gotiger.gTDonationEvent.action.scale.RandomScaleManager;
import dev.gotiger.gTDonationEvent.action.scarecrow.ScarecrowManager;
import dev.gotiger.gTDonationEvent.action.soulout.SoulOutManager;
import dev.gotiger.gTDonationEvent.action.special.SpecialItemManager;
import dev.gotiger.gTDonationEvent.action.teleport.RandomTeleportManager;
import dev.gotiger.gTDonationEvent.action.tnt.TntAction;
import dev.gotiger.gTDonationEvent.action.view.RandomViewAction;
import dev.gotiger.gTDonationEvent.action.waterprison.WaterPrisonManager;
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
        actionRegistry.register(new RandomDebuffAction(this));
        actionRegistry.register(new RandomAnimalAction(this));
        actionRegistry.register(new SuperHealAction());
        actionRegistry.register(new RandomItemAction(this));
        actionRegistry.register(new RandomViewAction(this));
        actionRegistry.register(new AbsorptionAction(this));
        actionRegistry.register(new KnockbackAction(this));
        actionRegistry.register(new WeakMonsterAction(this));
        actionRegistry.register(new MediumMonsterAction(this));
        actionRegistry.register(new StrongMonsterAction(this));
        actionRegistry.register(new BurnAction(this));
        actionRegistry.register(new LightningAction(this));
        actionRegistry.register(new TntAction(this));
        actionRegistry.register(new FireworkAction(this));
        actionRegistry.register(new PokeAction(this));

        ChatMiningManager chatMiningManager = new ChatMiningManager(this);
        ChatShootingManager chatShootingManager = new ChatShootingManager(this);
        ScarecrowManager scarecrowManager = new ScarecrowManager(this);
        XrayManager xrayManager = new XrayManager(this);
        SpecialItemManager specialItemManager = new SpecialItemManager(this);
        EnchantScrollManager enchantScrollManager = new EnchantScrollManager(this);
        EnchantFairyManager enchantFairyManager = new EnchantFairyManager(this);
        SoulOutManager soulOutManager = new SoulOutManager(this);
        DevilPickaxeManager devilPickaxeManager = new DevilPickaxeManager(this);
        InventorySaveManager inventorySaveManager = new InventorySaveManager(this);
        DiamondZoneManager diamondZoneManager = new DiamondZoneManager(this);
        MonsterScanManager monsterScanManager = new MonsterScanManager(this);
        FrostbiteManager frostbiteManager = new FrostbiteManager(this);
        RandomScaleManager randomScaleManager = new RandomScaleManager(this);
        WaterPrisonManager waterPrisonManager = new WaterPrisonManager(this);
        SlotLockManager slotLockManager = new SlotLockManager(this);
        MiningCurseManager miningCurseManager = new MiningCurseManager(this);
        RandomTeleportManager randomTeleportManager = new RandomTeleportManager(this);
        scriptAPI = new DonationScriptAPI(actionRegistry, chatMiningManager, chatShootingManager, scarecrowManager, xrayManager, specialItemManager, enchantScrollManager, enchantFairyManager, soulOutManager, devilPickaxeManager, inventorySaveManager, diamondZoneManager, monsterScanManager, frostbiteManager, randomScaleManager, waterPrisonManager, slotLockManager, miningCurseManager, randomTeleportManager);

        getServer().getPluginManager().registerEvents(
                new DonationEventListener(this, donationConfig, actionRegistry),
                this
        );
        getServer().getPluginManager().registerEvents(chatMiningManager, this);
        getServer().getPluginManager().registerEvents(chatShootingManager, this);
        getServer().getPluginManager().registerEvents(scarecrowManager, this);
        getServer().getPluginManager().registerEvents(enchantScrollManager, this);
        getServer().getPluginManager().registerEvents(soulOutManager, this);
        getServer().getPluginManager().registerEvents(devilPickaxeManager, this);
        getServer().getPluginManager().registerEvents(inventorySaveManager, this);
        getServer().getPluginManager().registerEvents(waterPrisonManager, this);
        getServer().getPluginManager().registerEvents(slotLockManager, this);
        getServer().getPluginManager().registerEvents(miningCurseManager, this);

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
