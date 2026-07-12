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
import dev.gotiger.gTDonationEvent.config.DonationTarget;
import dev.gotiger.gTDonationEvent.config.MessageService;
import dev.gotiger.gTDonationEvent.config.RandomTargetRouletteManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class DonationScriptAPI {

    private final JavaPlugin plugin;
    private final MessageService messages;
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
    private final RandomTargetRouletteManager rouletteManager;

    public DonationScriptAPI(JavaPlugin plugin, MessageService messages, DonationActionRegistry actionRegistry, ChatMiningManager chatMiningManager, ChatShootingManager chatShootingManager, ScarecrowManager scarecrowManager, XrayManager xrayManager, SpecialItemManager specialItemManager, EnchantScrollManager enchantScrollManager, EnchantFairyManager enchantFairyManager, SoulOutManager soulOutManager, DevilPickaxeManager devilPickaxeManager, InventorySaveManager inventorySaveManager, DiamondZoneManager diamondZoneManager, MonsterScanManager monsterScanManager, FrostbiteManager frostbiteManager, RandomScaleManager randomScaleManager, WaterPrisonManager waterPrisonManager, SlotLockManager slotLockManager, MiningCurseManager miningCurseManager, RandomTeleportManager randomTeleportManager, FanMeetingManager fanMeetingManager, DiamondCurseManager diamondCurseManager, ChatRaidManager chatRaidManager, ChatRushManager chatRushManager, ChatPunchManager chatPunchManager, RandomTargetRouletteManager rouletteManager) {
        this.plugin = plugin;
        this.messages = messages;
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
        this.rouletteManager = rouletteManager;
    }

    /**
     * 스크립트에서 대상을 먼저 뽑아 쓰고 싶을 때 사용. RANDOM이면 접속 중인 플레이어 중 한 명을
     * 즉시(연출 없이) 반환하고, PLAYER/ALL은 target.resolve(donor) 규칙대로 첫 번째 대상을 반환한다.
     * 대상이 없으면 null.
     */
    public Player pickTarget(Player donor, DonationTarget target) {
        return rouletteManager.pickTarget(donor, target);
    }

    /**
     * pickTarget으로 이미 뽑은 당첨자를 대상으로 "[대상 추첨]" 룰렛 타이틀 연출만 재생한다.
     * 연출은 비동기로 진행되며 즉시 리턴하므로, 연출이 끝난 뒤에 실제 액션을 실행하고 싶다면
     * 스크립트에서 연출 총 소요 시간만큼 wait 한 뒤 winner를 대상으로 액션을 실행하세요.
     */
    public void playRouletteEffect(Player winner) {
        rouletteManager.playRouletteEffect(winner);
    }

    /**
     * DonationTarget.ALL로 여러 명에게 랜덤 요소가 있는 액션을 적용할 때, config.yml의
     * random-action-same-result-for-all이 true이면 모두 같은 결과를 받아야 하므로 true를 반환한다.
     */
    private boolean shouldShareResultForAll(DonationTarget target) {
        return target == DonationTarget.ALL
                && plugin.getConfig().getBoolean("random-action-same-result-for-all", true);
    }

    private Map<String, String> donorPlaceholder(String donorName) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("donor", donorName);
        return placeholders;
    }

    public void getChatPunch(Player player, int seconds) {
        chatPunchManager.start(player, seconds, DonationTarget.PLAYER);
    }

    public void getChatPunch(Player player, int seconds, DonationTarget target) {
        chatPunchManager.start(player, seconds, target);
    }

    public void getChatRaid(Player player, int seconds) {
        chatRaidManager.start(player, seconds, DonationTarget.PLAYER);
    }

    public void getChatRaid(Player player, int seconds, DonationTarget target) {
        chatRaidManager.start(player, seconds, target);
    }

    public void getChatRush(Player player, int seconds) {
        chatRushManager.start(player, seconds, DonationTarget.PLAYER);
    }

    public void getChatRush(Player player, int seconds, DonationTarget target) {
        chatRushManager.start(player, seconds, target);
    }

    public void getDiamondCurse(Player player, String donorName) {
        getDiamondCurse(player, donorName, DonationTarget.PLAYER);
    }

    public void getDiamondCurse(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                if (!diamondCurseManager.curse(recipient)) {
                    continue;
                }
                recipient.sendMessage(messages.get("diamond-curse.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getFanMeeting(Player player, String donorName) {
        getFanMeeting(player, donorName, DonationTarget.PLAYER);
    }

    public void getFanMeeting(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                fanMeetingManager.summon(recipient, donorName);
                recipient.sendMessage(messages.get("fan-meeting.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getRandomTeleport(Player player, String donorName) {
        getRandomTeleport(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomTeleport(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                randomTeleportManager.teleport(recipient);
                recipient.sendMessage(messages.get("random-teleport.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getMiningCurse(Player player, String donorName) {
        getMiningCurse(player, donorName, DonationTarget.PLAYER);
    }

    public void getMiningCurse(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                if (!miningCurseManager.curse(recipient)) {
                    continue;
                }
                recipient.sendMessage(messages.get("mining-curse.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getSlotLock(Player player, String donorName) {
        getSlotLock(player, donorName, DonationTarget.PLAYER);
    }

    public void getSlotLock(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                if (!slotLockManager.lock(recipient)) {
                    continue;
                }
                recipient.sendMessage(messages.get("slot-lock.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getWaterPrison(Player player, String donorName) {
        getWaterPrison(player, donorName, DonationTarget.PLAYER);
    }

    public void getWaterPrison(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                if (!waterPrisonManager.trap(recipient)) {
                    continue;
                }
                recipient.sendMessage(messages.get("water-prison.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getRandomScale(Player player, String donorName) {
        getRandomScale(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomScale(Player player, String donorName, DonationTarget target) {
        boolean shareResult = shouldShareResultForAll(target);
        Double sharedScale = null;
        if (shareResult) {
            double minScale = plugin.getConfig().getDouble("random-scale.min-scale", 0.5);
            double maxScale = plugin.getConfig().getDouble("random-scale.max-scale", 2.0);
            sharedScale = minScale + Math.random() * (maxScale - minScale);
        }
        Double finalSharedScale = sharedScale;

        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                boolean applied = shareResult
                        ? randomScaleManager.apply(recipient, finalSharedScale)
                        : randomScaleManager.apply(recipient);
                if (!applied) {
                    continue;
                }
                recipient.sendMessage(messages.get("random-scale.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getFrostbite(Player player, String donorName) {
        getFrostbite(player, donorName, DonationTarget.PLAYER);
    }

    public void getFrostbite(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                frostbiteManager.apply(recipient);
                recipient.sendMessage(messages.get("frostbite.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getMonsterScan(Player player, String donorName) {
        getMonsterScan(player, donorName, DonationTarget.PLAYER);
    }

    public void getMonsterScan(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                int detectedCount = monsterScanManager.scan(recipient);
                if (detectedCount == 0) {
                    continue;
                }
                recipient.sendMessage(messages.get("monster-scan.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getDiamondZone(Player player, String donorName) {
        getDiamondZone(player, donorName, DonationTarget.PLAYER);
    }

    public void getDiamondZone(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                int convertedCount = diamondZoneManager.convert(recipient);
                if (convertedCount == 0) {
                    continue;
                }
                recipient.sendMessage(messages.get("diamond-zone.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getInventorySave(Player player, String donorName) {
        getInventorySave(player, donorName, DonationTarget.PLAYER);
    }

    public void getInventorySave(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                var leftover = recipient.getInventory().addItem(inventorySaveManager.createTicket());
                for (var remaining : leftover.values()) {
                    recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
                }
                recipient.sendMessage(messages.get("inventory-save.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getDevilPickaxe(Player player, String donorName) {
        getDevilPickaxe(player, donorName, DonationTarget.PLAYER);
    }

    public void getDevilPickaxe(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                var leftover = recipient.getInventory().addItem(devilPickaxeManager.createPickaxe());
                for (var remaining : leftover.values()) {
                    recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
                }
                recipient.sendMessage(messages.get("devil-pickaxe.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getSoulOutCharm(Player player, String donorName) {
        getSoulOutCharm(player, donorName, DonationTarget.PLAYER);
    }

    public void getSoulOutCharm(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                var leftover = recipient.getInventory().addItem(soulOutManager.createCharm());
                for (var remaining : leftover.values()) {
                    recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
                }
                recipient.sendMessage(messages.get("soul-out.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getEnchantFairy(Player player, String donorName) {
        getEnchantFairy(player, donorName, DonationTarget.PLAYER);
    }

    public void getEnchantFairy(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                boolean started = enchantFairyManager.enhance(recipient, (enchantment, level) -> {
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("recipient", recipient.getName());
                    placeholders.put("enchant", enchantment.getKey().getKey());
                    placeholders.put("level", String.valueOf(level));
                    recipient.sendMessage(messages.get("enchant-fairy.enhanced", placeholders));
                });

                if (started) {
                    recipient.sendMessage(messages.get("enchant-fairy.appeared", donorPlaceholder(donorName)));
                }
            }
        });
    }

    public void getEnchantScroll(Player player, String donorName) {
        getEnchantScroll(player, donorName, DonationTarget.PLAYER);
    }

    public void getEnchantScroll(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                var leftover = recipient.getInventory().addItem(enchantScrollManager.createScroll());
                for (var remaining : leftover.values()) {
                    recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
                }
                recipient.sendMessage(messages.get("enchant-scroll.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getSpecialItem(Player player, String donorName) {
        getSpecialItem(player, donorName, DonationTarget.PLAYER);
    }

    public void getSpecialItem(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                recipient.sendMessage(messages.get("special-item.spin-start", donorPlaceholder(donorName)));
                specialItemManager.spin(recipient, material ->
                        SpecialItemMessageSender.sendResultMessage(recipient, donorName, material, messages)
                );
            }
        });
    }

    public void getXray(Player player, String donorName) {
        getXray(player, donorName, DonationTarget.PLAYER);
    }

    public void getXray(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                xrayManager.reveal(recipient);
            }
        });
    }

    public void getScarecrow(Player player, String donorName) {
        getScarecrow(player, donorName, DonationTarget.PLAYER);
    }

    public void getScarecrow(Player player, String donorName, DonationTarget target) {
        rouletteManager.resolve(player, target, recipients -> {
            for (Player recipient : recipients) {
                scarecrowManager.spawn(recipient);
                recipient.sendMessage(messages.get("scarecrow.msg", donorPlaceholder(donorName)));
            }
        });
    }

    public void getChatMining(Player player, int seconds, String word1, String word2, int radius) {
        chatMiningManager.start(player, seconds, word1, word2, radius, DonationTarget.PLAYER);
    }

    public void getChatMining(Player player, int seconds, String word1, String word2, int radius, DonationTarget target) {
        chatMiningManager.start(player, seconds, word1, word2, radius, target);
    }

    public void getChatShooting(Player player, int seconds, String word) {
        chatShootingManager.start(player, seconds, word, DonationTarget.PLAYER);
    }

    public void getChatShooting(Player player, int seconds, String word, DonationTarget target) {
        chatShootingManager.start(player, seconds, word, target);
    }

    public void getBread(Player player, int amount) {
        run("BREAD", player, amount, DonationTarget.PLAYER);
    }

    public void getBread(Player player, int amount, DonationTarget target) {
        run("BREAD", player, amount, target);
    }

    public void getSteak(Player player, int amount) {
        run("STEAK", player, amount, DonationTarget.PLAYER);
    }

    public void getSteak(Player player, int amount, DonationTarget target) {
        run("STEAK", player, amount, target);
    }

    public void getTotem(Player player, int amount, String donorName) {
        getTotem(player, amount, donorName, DonationTarget.PLAYER);
    }

    public void getTotem(Player player, int amount, String donorName, DonationTarget target) {
        actionRegistry.get("TOTEM").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, amount);
                    recipient.sendMessage(messages.get("totem.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getRandomBuff(Player player, String donorName) {
        getRandomBuff(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomBuff(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("BUFF").ifPresent(action -> {
            RandomBuffAction randomBuffAction = (RandomBuffAction) action;
            boolean shareResult = shouldShareResultForAll(target);
            PotionEffectType sharedEffect = shareResult ? randomBuffAction.pickRandomEffect() : null;

            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    PotionEffectType effectType = sharedEffect;
                    if (shareResult) {
                        if (sharedEffect == null) {
                            continue;
                        }
                        randomBuffAction.applyBuff(recipient, sharedEffect);
                    } else {
                        effectType = randomBuffAction.applyBuff(recipient);
                    }
                    if (effectType == null) {
                        continue;
                    }
                    BuffMessageSender.sendBuffMessage(recipient, donorName, effectType, messages);
                }
            });
        });
    }

    public void getRandomDebuff(Player player, String donorName) {
        getRandomDebuff(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomDebuff(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("DEBUFF").ifPresent(action -> {
            RandomDebuffAction randomDebuffAction = (RandomDebuffAction) action;
            boolean shareResult = shouldShareResultForAll(target);
            PotionEffectType sharedEffect = shareResult ? randomDebuffAction.pickRandomEffect() : null;

            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    PotionEffectType effectType = sharedEffect;
                    if (shareResult) {
                        if (sharedEffect == null) {
                            continue;
                        }
                        randomDebuffAction.applyDebuff(recipient, sharedEffect);
                    } else {
                        effectType = randomDebuffAction.applyDebuff(recipient);
                    }
                    if (effectType == null) {
                        continue;
                    }
                    Map<String, String> placeholders = donorPlaceholder(donorName);
                    placeholders.put("effect", BuffNameKo.of(effectType));
                    recipient.sendMessage(messages.get("random-debuff.msg", placeholders));
                }
            });
        });
    }

    public void getExpBottle(Player player, String donorName) {
        getExpBottle(player, donorName, DonationTarget.PLAYER);
    }

    public void getExpBottle(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("EXP_BOTTLE").ifPresent(action -> {
            ExpBottleAction expBottleAction = (ExpBottleAction) action;
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    int thrownCount = expBottleAction.throwBottles(recipient);
                    Map<String, String> placeholders = donorPlaceholder(donorName);
                    placeholders.put("count", String.valueOf(thrownCount));
                    recipient.sendMessage(messages.get("exp-bottle.msg", placeholders));
                }
            });
        });
    }

    public void getRandomAnimal(Player player, String donorName) {
        getRandomAnimal(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomAnimal(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("ANIMAL").ifPresent(action -> {
            RandomAnimalAction randomAnimalAction = (RandomAnimalAction) action;
            boolean shareResult = shouldShareResultForAll(target);
            EntityType sharedType = shareResult ? randomAnimalAction.pickRandomEntityType() : null;

            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    boolean spawned = shareResult
                            ? sharedType != null && randomAnimalAction.spawnAnimal(recipient, sharedType) != null
                            : randomAnimalAction.spawnAnimal(recipient) != null;
                    if (!spawned) {
                        continue;
                    }
                    recipient.sendMessage(messages.get("random-animal.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getSuperHeal(Player player, String donorName) {
        getSuperHeal(player, donorName, DonationTarget.PLAYER);
    }

    public void getSuperHeal(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("SUPER_HEAL").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("super-heal.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getAbsorption(Player player, String donorName) {
        getAbsorption(player, donorName, DonationTarget.PLAYER);
    }

    public void getAbsorption(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("ABSORPTION").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("absorption.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getKnockback(Player player, String donorName) {
        getKnockback(player, donorName, DonationTarget.PLAYER);
    }

    public void getKnockback(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("KNOCKBACK").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("knockback.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getWeakMonster(Player player, String donorName) {
        getWeakMonster(player, donorName, DonationTarget.PLAYER);
    }

    public void getWeakMonster(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("WEAK_MONSTER").ifPresent(action -> {
            WeakMonsterAction weakMonsterAction = (WeakMonsterAction) action;
            boolean shareResult = shouldShareResultForAll(target);
            EntityType sharedType = shareResult ? weakMonsterAction.pickRandomEntityType() : null;

            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    boolean spawned = shareResult
                            ? sharedType != null && weakMonsterAction.spawnMonster(recipient, sharedType) != null
                            : weakMonsterAction.spawnMonster(recipient) != null;
                    if (!spawned) {
                        continue;
                    }
                    recipient.sendMessage(messages.get("weak-monster.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getMediumMonster(Player player, String donorName) {
        getMediumMonster(player, donorName, DonationTarget.PLAYER);
    }

    public void getMediumMonster(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("MEDIUM_MONSTER").ifPresent(action -> {
            MediumMonsterAction mediumMonsterAction = (MediumMonsterAction) action;
            boolean shareResult = shouldShareResultForAll(target);
            EntityType sharedType = shareResult ? mediumMonsterAction.pickRandomEntityType() : null;

            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    boolean spawned = shareResult
                            ? sharedType != null && mediumMonsterAction.spawnMonster(recipient, sharedType) != null
                            : mediumMonsterAction.spawnMonster(recipient) != null;
                    if (!spawned) {
                        continue;
                    }
                    recipient.sendMessage(messages.get("medium-monster.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getBurn(Player player, String donorName) {
        getBurn(player, donorName, DonationTarget.PLAYER);
    }

    public void getBurn(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("BURN").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("burn.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getLightning(Player player, String donorName) {
        getLightning(player, donorName, DonationTarget.PLAYER);
    }

    public void getLightning(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("LIGHTNING").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("lightning.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getTnt(Player player, String donorName) {
        getTnt(player, donorName, DonationTarget.PLAYER);
    }

    public void getTnt(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("TNT").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("tnt.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getFirework(Player player, String donorName) {
        getFirework(player, donorName, DonationTarget.PLAYER);
    }

    public void getFirework(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("FIREWORK").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("firework.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getPoke(Player player, String donorName) {
        getPoke(player, donorName, DonationTarget.PLAYER);
    }

    public void getPoke(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("POKE").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("poke.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getRandomView(Player player, String donorName) {
        getRandomView(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomView(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("RANDOM_VIEW").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("random-view.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getStrongMonster(Player player, String donorName) {
        getStrongMonster(player, donorName, DonationTarget.PLAYER);
    }

    public void getStrongMonster(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("STRONG_MONSTER").ifPresent(action -> {
            StrongMonsterAction strongMonsterAction = (StrongMonsterAction) action;
            boolean shareResult = shouldShareResultForAll(target);
            EntityType sharedType = shareResult ? strongMonsterAction.pickRandomEntityType() : null;

            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    boolean spawned = shareResult
                            ? sharedType != null && strongMonsterAction.spawnMonster(recipient, sharedType) != null
                            : strongMonsterAction.spawnMonster(recipient) != null;
                    if (!spawned) {
                        continue;
                    }
                    recipient.sendMessage(messages.get("strong-monster.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getFreeFall(Player player, String donorName) {
        getFreeFall(player, donorName, DonationTarget.PLAYER);
    }

    public void getFreeFall(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("FREE_FALL").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("free-fall.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getRotFood(Player player, String donorName) {
        getRotFood(player, donorName, DonationTarget.PLAYER);
    }

    public void getRotFood(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("ROT_FOOD").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("rot-food.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getJump(Player player, String donorName) {
        getJump(player, donorName, DonationTarget.PLAYER);
    }

    public void getJump(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("JUMP").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("jump.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getItemRemove(Player player, String donorName) {
        getItemRemove(player, donorName, DonationTarget.PLAYER);
    }

    public void getItemRemove(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("ITEM_REMOVE").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("item-remove.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    public void getRandomItem(Player player, String donorName) {
        getRandomItem(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomItem(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("RANDOM_ITEM").ifPresent(action -> {
            RandomItemAction randomItemAction = (RandomItemAction) action;
            boolean shareResult = shouldShareResultForAll(target);
            Material sharedMaterial = shareResult ? randomItemAction.pickRandomMaterial() : null;

            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    Material material = sharedMaterial;
                    if (shareResult) {
                        if (sharedMaterial == null) {
                            continue;
                        }
                        randomItemAction.giveItem(recipient, sharedMaterial);
                    } else {
                        material = randomItemAction.giveRandomItem(recipient);
                    }
                    if (material == null) {
                        continue;
                    }
                    Map<String, String> placeholders = donorPlaceholder(donorName);
                    placeholders.put("item", material.name());
                    recipient.sendMessage(messages.get("random-item.msg", placeholders));
                }
            });
        });
    }

    public void getInstantDeath(Player player, String donorName) {
        getInstantDeath(player, donorName, DonationTarget.PLAYER);
    }

    public void getInstantDeath(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("INSTANT_DEATH").ifPresent(action -> {
            rouletteManager.resolve(player, target, recipients -> {
                for (Player recipient : recipients) {
                    action.execute(recipient, 0);
                    recipient.sendMessage(messages.get("instant-death.msg", donorPlaceholder(donorName)));
                }
            });
        });
    }

    private void run(String actionName, Player player, int amount, DonationTarget target) {
        actionRegistry.get(actionName).ifPresent(action ->
                rouletteManager.resolve(player, target, recipients -> {
                    for (Player recipient : recipients) {
                        action.execute(recipient, amount);
                    }
                })
        );
    }
}
