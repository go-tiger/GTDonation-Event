package dev.gotiger.gTDonationEvent;

import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.action.animal.RandomAnimalAction;
import dev.gotiger.gTDonationEvent.action.buff.BuffMessageSender;
import dev.gotiger.gTDonationEvent.action.buff.RandomBuffAction;
import dev.gotiger.gTDonationEvent.action.chat.mining.ChatMiningManager;
import dev.gotiger.gTDonationEvent.action.diamondzone.DiamondZoneManager;
import dev.gotiger.gTDonationEvent.action.frostbite.FrostbiteManager;
import dev.gotiger.gTDonationEvent.action.enchant.EnchantFairyManager;
import dev.gotiger.gTDonationEvent.action.enchant.EnchantScrollManager;
import dev.gotiger.gTDonationEvent.action.chat.shooting.ChatShootingManager;
import dev.gotiger.gTDonationEvent.action.food.ExpBottleAction;
import dev.gotiger.gTDonationEvent.action.inventorysave.InventorySaveManager;
import dev.gotiger.gTDonationEvent.action.item.RandomItemAction;
import dev.gotiger.gTDonationEvent.action.monster.MediumMonsterAction;
import dev.gotiger.gTDonationEvent.action.monster.WeakMonsterAction;
import dev.gotiger.gTDonationEvent.action.monsterscan.MonsterScanManager;
import dev.gotiger.gTDonationEvent.action.pickaxe.DevilPickaxeManager;
import dev.gotiger.gTDonationEvent.action.scarecrow.ScarecrowManager;
import dev.gotiger.gTDonationEvent.action.soulout.SoulOutManager;
import dev.gotiger.gTDonationEvent.action.special.SpecialItemManager;
import dev.gotiger.gTDonationEvent.action.special.SpecialItemMessageSender;
import dev.gotiger.gTDonationEvent.action.xray.XrayManager;
import dev.gotiger.gTDonationEvent.config.DonationTarget;
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

    public DonationScriptAPI(DonationActionRegistry actionRegistry, ChatMiningManager chatMiningManager, ChatShootingManager chatShootingManager, ScarecrowManager scarecrowManager, XrayManager xrayManager, SpecialItemManager specialItemManager, EnchantScrollManager enchantScrollManager, EnchantFairyManager enchantFairyManager, SoulOutManager soulOutManager, DevilPickaxeManager devilPickaxeManager, InventorySaveManager inventorySaveManager, DiamondZoneManager diamondZoneManager, MonsterScanManager monsterScanManager, FrostbiteManager frostbiteManager) {
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
    }

    public void getFrostbite(Player player, String donorName) {
        getFrostbite(player, donorName, DonationTarget.PLAYER);
    }

    public void getFrostbite(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            frostbiteManager.apply(recipient);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 동상 발동"
            );
        }
    }

    public void getMonsterScan(Player player, String donorName) {
        getMonsterScan(player, donorName, DonationTarget.PLAYER);
    }

    public void getMonsterScan(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            int detectedCount = monsterScanManager.scan(recipient);
            if (detectedCount == 0) {
                continue;
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 몬스터 탐지 발동"
            );
        }
    }

    public void getDiamondZone(Player player, String donorName) {
        getDiamondZone(player, donorName, DonationTarget.PLAYER);
    }

    public void getDiamondZone(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            int convertedCount = diamondZoneManager.convert(recipient);
            if (convertedCount == 0) {
                continue;
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님 주변이 다이아존으로 변했습니다!"
            );
        }
    }

    public void getInventorySave(Player player, String donorName) {
        getInventorySave(player, donorName, DonationTarget.PLAYER);
    }

    public void getInventorySave(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            var leftover = recipient.getInventory().addItem(inventorySaveManager.createTicket());
            for (var remaining : leftover.values()) {
                recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 인벤토리 세이브권 지급"
            );
        }
    }

    public void getDevilPickaxe(Player player, String donorName) {
        getDevilPickaxe(player, donorName, DonationTarget.PLAYER);
    }

    public void getDevilPickaxe(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            var leftover = recipient.getInventory().addItem(devilPickaxeManager.createPickaxe());
            for (var remaining : leftover.values()) {
                recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 악마의 곡괭이 지급"
            );
        }
    }

    public void getSoulOutCharm(Player player, String donorName) {
        getSoulOutCharm(player, donorName, DonationTarget.PLAYER);
    }

    public void getSoulOutCharm(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            var leftover = recipient.getInventory().addItem(soulOutManager.createCharm());
            for (var remaining : leftover.values()) {
                recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 유체이탈의 부적 지급"
            );
        }
    }

    public void getEnchantFairy(Player player, String donorName) {
        getEnchantFairy(player, donorName, DonationTarget.PLAYER);
    }

    public void getEnchantFairy(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
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
    }

    public void getEnchantScroll(Player player, String donorName) {
        getEnchantScroll(player, donorName, DonationTarget.PLAYER);
    }

    public void getEnchantScroll(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            var leftover = recipient.getInventory().addItem(enchantScrollManager.createScroll());
            for (var remaining : leftover.values()) {
                recipient.getWorld().dropItemNaturally(recipient.getLocation(), remaining);
            }
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 즉석 인챈트권 지급"
            );
        }
    }

    public void getSpecialItem(Player player, String donorName) {
        getSpecialItem(player, donorName, DonationTarget.PLAYER);
    }

    public void getSpecialItem(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 특별한 아이템 룰렛!"
            );
            specialItemManager.spin(recipient, material ->
                    SpecialItemMessageSender.broadcastResultMessage(recipient, donorName, material)
            );
        }
    }

    public void getXray(Player player, String donorName) {
        getXray(player, donorName, DonationTarget.PLAYER);
    }

    public void getXray(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            xrayManager.reveal(recipient);
        }
    }

    public void getScarecrow(Player player, String donorName) {
        getScarecrow(player, donorName, DonationTarget.PLAYER);
    }

    public void getScarecrow(Player player, String donorName, DonationTarget target) {
        for (Player recipient : target.resolve(player)) {
            scarecrowManager.spawn(recipient);
            recipient.getServer().broadcastMessage(
                    ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 음식 허수아비를 소환"
            );
        }
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
            for (Player recipient : target.resolve(player)) {
                action.execute(recipient, amount);
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 불사의 토템 지급"
                );
            }
        });
    }

    public void getRandomBuff(Player player, String donorName) {
        getRandomBuff(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomBuff(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("BUFF").ifPresent(action -> {
            RandomBuffAction randomBuffAction = (RandomBuffAction) action;
            for (Player recipient : target.resolve(player)) {
                PotionEffectType effectType = randomBuffAction.applyBuff(recipient);
                if (effectType == null) {
                    continue;
                }
                BuffMessageSender.broadcastBuffMessage(recipient, donorName, effectType);
            }
        });
    }

    public void getExpBottle(Player player, String donorName) {
        getExpBottle(player, donorName, DonationTarget.PLAYER);
    }

    public void getExpBottle(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("EXP_BOTTLE").ifPresent(action -> {
            ExpBottleAction expBottleAction = (ExpBottleAction) action;
            for (Player recipient : target.resolve(player)) {
                int thrownCount = expBottleAction.throwBottles(recipient);
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 경험치 병 "
                                + ChatColor.YELLOW + thrownCount + "개" + ChatColor.GRAY + " 지급"
                );
            }
        });
    }

    public void getRandomAnimal(Player player, String donorName) {
        getRandomAnimal(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomAnimal(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("ANIMAL").ifPresent(action -> {
            RandomAnimalAction randomAnimalAction = (RandomAnimalAction) action;
            for (Player recipient : target.resolve(player)) {
                if (randomAnimalAction.spawnAnimal(recipient) == null) {
                    continue;
                }
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 동물을 소환"
                );
            }
        });
    }

    public void getSuperHeal(Player player, String donorName) {
        getSuperHeal(player, donorName, DonationTarget.PLAYER);
    }

    public void getSuperHeal(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("SUPER_HEAL").ifPresent(action -> {
            for (Player recipient : target.resolve(player)) {
                action.execute(recipient, 0);
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 슈퍼 회복 지급"
                );
            }
        });
    }

    public void getAbsorption(Player player, String donorName) {
        getAbsorption(player, donorName, DonationTarget.PLAYER);
    }

    public void getAbsorption(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("ABSORPTION").ifPresent(action -> {
            for (Player recipient : target.resolve(player)) {
                action.execute(recipient, 0);
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 추가 체력 지급"
                );
            }
        });
    }

    public void getKnockback(Player player, String donorName) {
        getKnockback(player, donorName, DonationTarget.PLAYER);
    }

    public void getKnockback(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("KNOCKBACK").ifPresent(action -> {
            for (Player recipient : target.resolve(player)) {
                action.execute(recipient, 0);
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 깡! 발동"
                );
            }
        });
    }

    public void getWeakMonster(Player player, String donorName) {
        getWeakMonster(player, donorName, DonationTarget.PLAYER);
    }

    public void getWeakMonster(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("WEAK_MONSTER").ifPresent(action -> {
            WeakMonsterAction weakMonsterAction = (WeakMonsterAction) action;
            for (Player recipient : target.resolve(player)) {
                if (weakMonsterAction.spawnMonster(recipient) == null) {
                    continue;
                }
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 하급 몬스터를 소환"
                );
            }
        });
    }

    public void getMediumMonster(Player player, String donorName) {
        getMediumMonster(player, donorName, DonationTarget.PLAYER);
    }

    public void getMediumMonster(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("MEDIUM_MONSTER").ifPresent(action -> {
            MediumMonsterAction mediumMonsterAction = (MediumMonsterAction) action;
            for (Player recipient : target.resolve(player)) {
                if (mediumMonsterAction.spawnMonster(recipient) == null) {
                    continue;
                }
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 중급 몬스터를 소환"
                );
            }
        });
    }

    public void getBurn(Player player, String donorName) {
        getBurn(player, donorName, DonationTarget.PLAYER);
    }

    public void getBurn(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("BURN").ifPresent(action -> {
            for (Player recipient : target.resolve(player)) {
                action.execute(recipient, 0);
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 화상 발동"
                );
            }
        });
    }

    public void getRandomItem(Player player, String donorName) {
        getRandomItem(player, donorName, DonationTarget.PLAYER);
    }

    public void getRandomItem(Player player, String donorName, DonationTarget target) {
        actionRegistry.get("RANDOM_ITEM").ifPresent(action -> {
            RandomItemAction randomItemAction = (RandomItemAction) action;
            for (Player recipient : target.resolve(player)) {
                var material = randomItemAction.giveRandomItem(recipient);
                if (material == null) {
                    continue;
                }
                recipient.getServer().broadcastMessage(
                        ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 랜덤 아이템 "
                                + ChatColor.YELLOW + material.name() + ChatColor.GRAY + " 지급"
                );
            }
        });
    }

    private void run(String actionName, Player player, int amount, DonationTarget target) {
        actionRegistry.get(actionName).ifPresent(action -> action.applyTo(player, amount, target));
    }
}
