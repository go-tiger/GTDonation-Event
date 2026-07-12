package dev.gotiger.gTDonationEvent;

import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.action.mob.animal.RandomAnimalAction;
import dev.gotiger.gTDonationEvent.action.status.buff.BuffMessageSender;
import dev.gotiger.gTDonationEvent.action.status.buff.BuffNameKo;
import dev.gotiger.gTDonationEvent.action.status.buff.RandomBuffAction;
import dev.gotiger.gTDonationEvent.action.status.buff.RandomDebuffAction;
import dev.gotiger.gTDonationEvent.action.chat.mining.ChatMiningManager;
import dev.gotiger.gTDonationEvent.action.chat.punch.ChatPunchManager;
import dev.gotiger.gTDonationEvent.action.chat.raid.ChatRaidManager;
import dev.gotiger.gTDonationEvent.action.chat.rush.ChatRushManager;
import dev.gotiger.gTDonationEvent.action.status.diamondcurse.DiamondCurseManager;
import dev.gotiger.gTDonationEvent.action.hazard.diamondzone.DiamondZoneManager;
import dev.gotiger.gTDonationEvent.action.hazard.frostbite.FrostbiteManager;
import dev.gotiger.gTDonationEvent.action.inventory.enchant.EnchantFairyManager;
import dev.gotiger.gTDonationEvent.action.inventory.enchant.EnchantScrollManager;
import dev.gotiger.gTDonationEvent.action.chat.shooting.ChatShootingManager;
import dev.gotiger.gTDonationEvent.action.mob.fanmeeting.FanMeetingManager;
import dev.gotiger.gTDonationEvent.action.inventory.food.ExpBottleAction;
import dev.gotiger.gTDonationEvent.action.inventory.inventorysave.InventorySaveManager;
import dev.gotiger.gTDonationEvent.action.inventory.item.RandomItemAction;
import dev.gotiger.gTDonationEvent.action.inventory.lock.SlotLockManager;
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
import dev.gotiger.gTDonationEvent.action.inventory.special.SpecialItemMessageSender;
import dev.gotiger.gTDonationEvent.action.movement.waterprison.WaterPrisonManager;
import dev.gotiger.gTDonationEvent.action.movement.teleport.RandomTeleportManager;
import dev.gotiger.gTDonationEvent.action.misc.xray.XrayManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class DonationScriptAPI {

    private final DonationActionRegistry actionRegistry;
    private final ChatMiningManager chatMiningManager;
    private final ChatShootingManager chatShootingManager;
    private final ScarecrowManager scarecrowManager;
    private final XrayManager xrayManager;
    private final SpecialItemManager specialItemManager;
    private final EnchantScrollManager enchantScrollManager;
    private final EnchantFairyManager enchantFairyManager;
    private final SoulOutManager soulOutManager;
    private final DevilPickaxeManager devilPickaxeManager;
    private final InventorySaveManager inventorySaveManager;
    private final DiamondZoneManager diamondZoneManager;
    private final MonsterScanManager monsterScanManager;
    private final FrostbiteManager frostbiteManager;
    private final RandomScaleManager randomScaleManager;
    private final WaterPrisonManager waterPrisonManager;
    private final SlotLockManager slotLockManager;
    private final MiningCurseManager miningCurseManager;
    private final RandomTeleportManager randomTeleportManager;
    private final FanMeetingManager fanMeetingManager;
    private final DiamondCurseManager diamondCurseManager;
    private final ChatRaidManager chatRaidManager;
    private final ChatRushManager chatRushManager;
    private final ChatPunchManager chatPunchManager;

    public DonationScriptAPI(DonationActionRegistry actionRegistry, ChatMiningManager chatMiningManager, ChatShootingManager chatShootingManager, ScarecrowManager scarecrowManager, XrayManager xrayManager, SpecialItemManager specialItemManager, EnchantScrollManager enchantScrollManager, EnchantFairyManager enchantFairyManager, SoulOutManager soulOutManager, DevilPickaxeManager devilPickaxeManager, InventorySaveManager inventorySaveManager, DiamondZoneManager diamondZoneManager, MonsterScanManager monsterScanManager, FrostbiteManager frostbiteManager, RandomScaleManager randomScaleManager, WaterPrisonManager waterPrisonManager, SlotLockManager slotLockManager, MiningCurseManager miningCurseManager, RandomTeleportManager randomTeleportManager, FanMeetingManager fanMeetingManager, DiamondCurseManager diamondCurseManager, ChatRaidManager chatRaidManager, ChatRushManager chatRushManager, ChatPunchManager chatPunchManager) {
        this.actionRegistry = actionRegistry;
        this.chatMiningManager = chatMiningManager;
        this.chatShootingManager = chatShootingManager;
        this.scarecrowManager = scarecrowManager;
        this.xrayManager = xrayManager;
        this.specialItemManager = specialItemManager;
        this.enchantScrollManager = enchantScrollManager;
        this.enchantFairyManager = enchantFairyManager;
        this.soulOutManager = soulOutManager;
        this.devilPickaxeManager = devilPickaxeManager;
        this.inventorySaveManager = inventorySaveManager;
        this.diamondZoneManager = diamondZoneManager;
        this.monsterScanManager = monsterScanManager;
        this.frostbiteManager = frostbiteManager;
        this.randomScaleManager = randomScaleManager;
        this.waterPrisonManager = waterPrisonManager;
        this.slotLockManager = slotLockManager;
        this.miningCurseManager = miningCurseManager;
        this.randomTeleportManager = randomTeleportManager;
        this.fanMeetingManager = fanMeetingManager;
        this.diamondCurseManager = diamondCurseManager;
        this.chatRaidManager = chatRaidManager;
        this.chatRushManager = chatRushManager;
        this.chatPunchManager = chatPunchManager;
    }

    public void getChatPunch(Player recipient, int seconds) {
        chatPunchManager.start(recipient, seconds);
    }

    public void getChatRaid(Player recipient, int seconds) {
        chatRaidManager.start(recipient, seconds);
    }

    public void getChatRush(Player recipient, int seconds) {
        chatRushManager.start(recipient, seconds);
    }

    public void getDiamondCurse(Player recipient, String donorName) {
        if (!diamondCurseManager.curse(recipient)) {
            return;
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님에게 다이아의 저주 발동"
        );
    }

    public void getFanMeeting(Player recipient, String donorName) {
        fanMeetingManager.summon(recipient, donorName);
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님의 팬미팅!"
        );
    }

    public void getRandomTeleport(Player recipient, String donorName) {
        randomTeleportManager.teleport(recipient);
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 랜덤 순간이동 발동"
        );
    }

    public void getMiningCurse(Player recipient, String donorName) {
        if (!miningCurseManager.curse(recipient)) {
            return;
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님에게 광질 저주 발동"
        );
    }

    public void getSlotLock(Player recipient, String donorName) {
        if (!slotLockManager.lock(recipient)) {
            return;
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님의 인벤토리 슬롯 하나가 잠김"
        );
    }

    public void getWaterPrison(Player recipient, String donorName) {
        if (!waterPrisonManager.trap(recipient)) {
            return;
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 물 감옥에 갇힘"
        );
    }

    public void getRandomScale(Player recipient, String donorName) {
        if (!randomScaleManager.apply(recipient)) {
            return;
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님의 크기가 랜덤하게 변경됨"
        );
    }

    public void getFrostbite(Player recipient, String donorName) {
        frostbiteManager.apply(recipient);
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 동상 발동"
        );
    }

    public void getMonsterScan(Player recipient, String donorName) {
        int detectedCount = monsterScanManager.scan(recipient);
        if (detectedCount == 0) {
            return;
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 몬스터 탐지 발동"
        );
    }

    public void getDiamondZone(Player recipient, String donorName) {
        int convertedCount = diamondZoneManager.convert(recipient);
        if (convertedCount == 0) {
            return;
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님 주변이 다이아존으로 변했습니다!"
        );
    }

    public void getInventorySave(Player recipient, String donorName) {
        var leftover = recipient.getInventory().addItem(inventorySaveManager.createTicket());
        for (var remaining : leftover.values()) {
            recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 인벤토리 세이브권 지급"
        );
    }

    public void getDevilPickaxe(Player recipient, String donorName) {
        var leftover = recipient.getInventory().addItem(devilPickaxeManager.createPickaxe());
        for (var remaining : leftover.values()) {
            recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 악마의 곡괭이 지급"
        );
    }

    public void getSoulOutCharm(Player recipient, String donorName) {
        var leftover = recipient.getInventory().addItem(soulOutManager.createCharm());
        for (var remaining : leftover.values()) {
            recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 유체이탈의 부적 지급"
        );
    }

    public void getEnchantFairy(Player recipient, String donorName) {
        boolean started = enchantFairyManager.enhance(recipient, (enchantment, level) ->
                recipient.getServer().broadcastMessage(
                        ChatColor.LIGHT_PURPLE + "강화 요정이 " + ChatColor.WHITE + recipient.getName()
                                + ChatColor.LIGHT_PURPLE + "님의 장비를 " + ChatColor.GOLD
                                + enchantment.getKey().getKey() + " " + level
                                + ChatColor.LIGHT_PURPLE + "(으)로 강화했습니다!"
                )
        );

        if (started) {
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님의 강화 요정이 나타났습니다!"
            );
        }
    }

    public void getEnchantScroll(Player recipient, String donorName) {
        var leftover = recipient.getInventory().addItem(enchantScrollManager.createScroll());
        for (var remaining : leftover.values()) {
            recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
        }
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 즉석 인챈트권 지급"
        );
    }

    public void getSpecialItem(Player recipient, String donorName) {
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 특별한 아이템 룰렛!"
        );
        specialItemManager.spin(recipient, material ->
                SpecialItemMessageSender.broadcastResultMessage(recipient, donorName, material)
        );
    }

    public void getXray(Player recipient, String donorName) {
        xrayManager.reveal(recipient);
    }

    public void getScarecrow(Player recipient, String donorName) {
        scarecrowManager.spawn(recipient);
        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 음식 허수아비를 소환"
        );
    }

    public void getChatMining(Player recipient, int seconds, String word1, String word2, int radius) {
        chatMiningManager.start(recipient, seconds, word1, word2, radius);
    }

    public void getChatShooting(Player recipient, int seconds, String word) {
        chatShootingManager.start(recipient, seconds, word);
    }

    public void getBread(Player recipient, int amount) {
        run("BREAD", recipient, amount);
    }

    public void getSteak(Player recipient, int amount) {
        run("STEAK", recipient, amount);
    }

    public void getTotem(Player recipient, int amount, String donorName) {
        actionRegistry.get("TOTEM").ifPresent(action -> {
            action.execute(recipient, amount);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 불사의 토템 지급"
            );
        });
    }

    public void getRandomBuff(Player recipient, String donorName) {
        actionRegistry.get("BUFF").ifPresent(action -> {
            RandomBuffAction randomBuffAction = (RandomBuffAction) action;
            PotionEffectType effectType = randomBuffAction.applyBuff(recipient);
            if (effectType == null) {
                return;
            }
            BuffMessageSender.broadcastBuffMessage(recipient, donorName, effectType);
        });
    }

    public void getRandomDebuff(Player recipient, String donorName) {
        actionRegistry.get("DEBUFF").ifPresent(action -> {
            RandomDebuffAction randomDebuffAction = (RandomDebuffAction) action;
            PotionEffectType effectType = randomDebuffAction.applyDebuff(recipient);
            if (effectType == null) {
                return;
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 "
                            + ChatColor.DARK_RED + BuffNameKo.of(effectType) + ChatColor.GRAY + " 디버프를 받음"
            );
        });
    }

    public void getExpBottle(Player recipient, String donorName) {
        actionRegistry.get("EXP_BOTTLE").ifPresent(action -> {
            ExpBottleAction expBottleAction = (ExpBottleAction) action;
            int thrownCount = expBottleAction.throwBottles(recipient);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 경험치 병 "
                            + ChatColor.YELLOW + thrownCount + "개" + ChatColor.GRAY + " 지급"
            );
        });
    }

    public void getRandomAnimal(Player recipient, String donorName) {
        actionRegistry.get("ANIMAL").ifPresent(action -> {
            RandomAnimalAction randomAnimalAction = (RandomAnimalAction) action;
            if (randomAnimalAction.spawnAnimal(recipient) == null) {
                return;
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 동물을 소환"
            );
        });
    }

    public void getSuperHeal(Player recipient, String donorName) {
        actionRegistry.get("SUPER_HEAL").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 슈퍼 회복 지급"
            );
        });
    }

    public void getAbsorption(Player recipient, String donorName) {
        actionRegistry.get("ABSORPTION").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 추가 체력 지급"
            );
        });
    }

    public void getKnockback(Player recipient, String donorName) {
        actionRegistry.get("KNOCKBACK").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 깡! 발동"
            );
        });
    }

    public void getWeakMonster(Player recipient, String donorName) {
        actionRegistry.get("WEAK_MONSTER").ifPresent(action -> {
            WeakMonsterAction weakMonsterAction = (WeakMonsterAction) action;
            if (weakMonsterAction.spawnMonster(recipient) == null) {
                return;
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 하급 몬스터를 소환"
            );
        });
    }

    public void getMediumMonster(Player recipient, String donorName) {
        actionRegistry.get("MEDIUM_MONSTER").ifPresent(action -> {
            MediumMonsterAction mediumMonsterAction = (MediumMonsterAction) action;
            if (mediumMonsterAction.spawnMonster(recipient) == null) {
                return;
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 중급 몬스터를 소환"
            );
        });
    }

    public void getBurn(Player recipient, String donorName) {
        actionRegistry.get("BURN").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 화상 발동"
            );
        });
    }

    public void getLightning(Player recipient, String donorName) {
        actionRegistry.get("LIGHTNING").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님에게 벼락 발동"
            );
        });
    }

    public void getTnt(Player recipient, String donorName) {
        actionRegistry.get("TNT").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 TNT를 소환"
            );
        });
    }

    public void getFirework(Player recipient, String donorName) {
        actionRegistry.get("FIREWORK").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 폭죽 발사"
            );
        });
    }

    public void getPoke(Player recipient, String donorName) {
        actionRegistry.get("POKE").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 딱콩 발동"
            );
        });
    }

    public void getRandomView(Player recipient, String donorName) {
        actionRegistry.get("RANDOM_VIEW").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님의 시야가 강제로 변경됨"
            );
        });
    }

    public void getStrongMonster(Player recipient, String donorName) {
        actionRegistry.get("STRONG_MONSTER").ifPresent(action -> {
            StrongMonsterAction strongMonsterAction = (StrongMonsterAction) action;
            if (strongMonsterAction.spawnMonster(recipient) == null) {
                return;
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 상급 몬스터를 소환"
            );
        });
    }

    public void getFreeFall(Player recipient, String donorName) {
        actionRegistry.get("FREE_FALL").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 자유낙하 발동"
            );
        });
    }

    public void getRotFood(Player recipient, String donorName) {
        actionRegistry.get("ROT_FOOD").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님의 음식이 모두 썩음"
            );
        });
    }

    public void getJump(Player recipient, String donorName) {
        actionRegistry.get("JUMP").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 점프 발동"
            );
        });
    }

    public void getItemRemove(Player recipient, String donorName) {
        actionRegistry.get("ITEM_REMOVE").ifPresent(action -> {
            action.execute(recipient, 0);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님의 아이템 하나가 불타서 사라짐"
            );
        });
    }

    public void getRandomItem(Player recipient, String donorName) {
        actionRegistry.get("RANDOM_ITEM").ifPresent(action -> {
            RandomItemAction randomItemAction = (RandomItemAction) action;
            var material = randomItemAction.giveRandomItem(recipient);
            if (material == null) {
                return;
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 랜덤 아이템 "
                            + ChatColor.YELLOW + material.name() + ChatColor.GRAY + " 지급"
            );
        });
    }

    private void run(String actionName, Player recipient, int amount) {
        actionRegistry.get(actionName).ifPresent(action -> action.execute(recipient, amount));
    }
}
