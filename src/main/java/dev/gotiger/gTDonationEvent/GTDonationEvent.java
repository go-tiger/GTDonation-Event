package dev.gotiger.gTDonationEvent;

import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.action.mob.animal.RandomAnimalAction;
import dev.gotiger.gTDonationEvent.action.status.buff.RandomBuffAction;
import dev.gotiger.gTDonationEvent.action.status.buff.RandomDebuffAction;
import dev.gotiger.gTDonationEvent.action.hazard.burn.BurnAction;
import dev.gotiger.gTDonationEvent.action.status.diamondcurse.DiamondCurseManager;
import dev.gotiger.gTDonationEvent.action.hazard.diamondzone.DiamondZoneManager;
import dev.gotiger.gTDonationEvent.action.inventory.enchant.EnchantFairyManager;
import dev.gotiger.gTDonationEvent.action.inventory.enchant.EnchantScrollManager;
import dev.gotiger.gTDonationEvent.action.chat.mining.ChatMiningManager;
import dev.gotiger.gTDonationEvent.action.chat.punch.ChatPunchManager;
import dev.gotiger.gTDonationEvent.action.chat.raid.ChatRaidManager;
import dev.gotiger.gTDonationEvent.action.chat.rush.ChatRushManager;
import dev.gotiger.gTDonationEvent.action.chat.shooting.ChatShootingManager;
import dev.gotiger.gTDonationEvent.action.inventory.food.BreadAction;
import dev.gotiger.gTDonationEvent.action.mob.fanmeeting.FanMeetingManager;
import dev.gotiger.gTDonationEvent.action.hazard.firework.FireworkAction;
import dev.gotiger.gTDonationEvent.action.inventory.food.ExpBottleAction;
import dev.gotiger.gTDonationEvent.action.inventory.food.SteakAction;
import dev.gotiger.gTDonationEvent.action.movement.freefall.FreeFallAction;
import dev.gotiger.gTDonationEvent.action.hazard.frostbite.FrostbiteManager;
import dev.gotiger.gTDonationEvent.action.status.heal.AbsorptionAction;
import dev.gotiger.gTDonationEvent.action.status.heal.SuperHealAction;
import dev.gotiger.gTDonationEvent.action.inventory.inventorysave.InventorySaveManager;
import dev.gotiger.gTDonationEvent.action.inventory.item.RandomItemAction;
import dev.gotiger.gTDonationEvent.action.inventory.rotfood.RotFoodAction;
import dev.gotiger.gTDonationEvent.action.inventory.item.TotemAction;
import dev.gotiger.gTDonationEvent.action.inventory.itemremove.ItemRemoveAction;
import dev.gotiger.gTDonationEvent.action.movement.jump.JumpAction;
import dev.gotiger.gTDonationEvent.action.movement.knockback.KnockbackAction;
import dev.gotiger.gTDonationEvent.action.inventory.lock.SlotLockManager;
import dev.gotiger.gTDonationEvent.action.movement.knockback.PokeAction;
import dev.gotiger.gTDonationEvent.action.hazard.lightning.LightningAction;
import dev.gotiger.gTDonationEvent.action.status.miningcurse.MiningCurseManager;
import dev.gotiger.gTDonationEvent.action.mob.monster.MediumMonsterAction;
import dev.gotiger.gTDonationEvent.action.mob.monster.StrongMonsterAction;
import dev.gotiger.gTDonationEvent.action.mob.monster.WeakMonsterAction;
import dev.gotiger.gTDonationEvent.action.mob.monsterscan.MonsterScanManager;
import dev.gotiger.gTDonationEvent.action.inventory.pickaxe.DevilPickaxeManager;
import dev.gotiger.gTDonationEvent.action.movement.scale.RandomScaleManager;
import dev.gotiger.gTDonationEvent.action.mob.scarecrow.ScarecrowManager;
import dev.gotiger.gTDonationEvent.action.mob.soulout.SoulOutManager;
import dev.gotiger.gTDonationEvent.action.inventory.special.SpecialItemManager;
import dev.gotiger.gTDonationEvent.action.movement.teleport.RandomTeleportManager;
import dev.gotiger.gTDonationEvent.action.hazard.tnt.TntAction;
import dev.gotiger.gTDonationEvent.action.movement.view.RandomViewAction;
import dev.gotiger.gTDonationEvent.action.movement.waterprison.WaterPrisonManager;
import dev.gotiger.gTDonationEvent.action.misc.xray.XrayManager;
import dev.gotiger.gTDonationEvent.command.DebugCommand;
import dev.gotiger.gTDonationEvent.config.ConfigMigrator;
import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.listener.DonationEventListener;
import dev.gotiger.gTDonationEvent.message.MessageService;
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
        actionRegistry.register(new RotFoodAction());
        actionRegistry.register(new JumpAction(this));
        actionRegistry.register(new ItemRemoveAction(this));
        actionRegistry.register(new RandomViewAction(this));
        actionRegistry.register(new AbsorptionAction(this));
        actionRegistry.register(new KnockbackAction(this));
        actionRegistry.register(new WeakMonsterAction(this));
        actionRegistry.register(new MediumMonsterAction(this));
        actionRegistry.register(new StrongMonsterAction(this));
        actionRegistry.register(new BurnAction(this));
        actionRegistry.register(new FreeFallAction(this));
        actionRegistry.register(new LightningAction(this));
        actionRegistry.register(new TntAction(this));
        actionRegistry.register(new FireworkAction(this));
        actionRegistry.register(new PokeAction(this));

        MessageService messageService = new MessageService(this);

        ChatMiningManager chatMiningManager = new ChatMiningManager(this);
        ChatRaidManager chatRaidManager = new ChatRaidManager(this);
        ChatRushManager chatRushManager = new ChatRushManager(this);
        ChatPunchManager chatPunchManager = new ChatPunchManager(this, messageService);
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
        FanMeetingManager fanMeetingManager = new FanMeetingManager(this);
        DiamondCurseManager diamondCurseManager = new DiamondCurseManager(this);
        scriptAPI = new DonationScriptAPI(actionRegistry, chatMiningManager, chatShootingManager, scarecrowManager, xrayManager, specialItemManager, enchantScrollManager, enchantFairyManager, soulOutManager, devilPickaxeManager, inventorySaveManager, diamondZoneManager, monsterScanManager, frostbiteManager, randomScaleManager, waterPrisonManager, slotLockManager, miningCurseManager, randomTeleportManager, fanMeetingManager, diamondCurseManager, chatRaidManager, chatRushManager, chatPunchManager);

        getServer().getPluginManager().registerEvents(
                new DonationEventListener(this, donationConfig, actionRegistry),
                this
        );
        getServer().getPluginManager().registerEvents(chatMiningManager, this);
        getServer().getPluginManager().registerEvents(chatRaidManager, this);
        getServer().getPluginManager().registerEvents(chatRushManager, this);
        getServer().getPluginManager().registerEvents(chatPunchManager, this);
        getServer().getPluginManager().registerEvents(chatShootingManager, this);
        getServer().getPluginManager().registerEvents(scarecrowManager, this);
        getServer().getPluginManager().registerEvents(enchantScrollManager, this);
        getServer().getPluginManager().registerEvents(soulOutManager, this);
        getServer().getPluginManager().registerEvents(devilPickaxeManager, this);
        getServer().getPluginManager().registerEvents(inventorySaveManager, this);
        getServer().getPluginManager().registerEvents(waterPrisonManager, this);
        getServer().getPluginManager().registerEvents(slotLockManager, this);
        getServer().getPluginManager().registerEvents(miningCurseManager, this);
        getServer().getPluginManager().registerEvents(diamondCurseManager, this);

        getCommand("gtdonationevent").setExecutor(new DebugCommand(this, messageService, donationConfig));
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
