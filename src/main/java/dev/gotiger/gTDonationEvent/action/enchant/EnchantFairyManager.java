package dev.gotiger.gTDonationEvent.action.enchant;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EnchantFairyManager {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public EnchantFairyManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean enhance(Player player, BiConsumer<Enchantment, Integer> onFinished) {
        List<Candidate> candidates = collectCandidates(player);
        if (candidates.isEmpty()) {
            player.sendMessage(ChatColor.RED + "강화할 수 있는 아이템이 없습니다.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return false;
        }

        Candidate chosen = candidates.get(random.nextInt(candidates.size()));
        ItemStack target = chosen.item;

        Enchantment enchantment = pickRandomEnchantment(target);
        if (enchantment == null) {
            player.sendMessage(ChatColor.RED + "강화 가능한 인챈트가 없습니다.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return false;
        }
        int level = 1 + random.nextInt(enchantment.getMaxLevel());

        Location eye = player.getEyeLocation();
        Location fairyLocation = eye.clone().add(eye.getDirection().normalize().multiply(2));
        fairyLocation.setYaw(eye.getYaw() + 180f);
        fairyLocation.setPitch(0f);
        Vex fairy = player.getWorld().spawn(fairyLocation, Vex.class);
        fairy.setInvulnerable(true);
        fairy.setSilent(true);
        fairy.setAI(false);
        fairy.setCustomName("강화 요정");
        fairy.setCustomNameVisible(true);
        fairy.setGravity(false);
        fairy.setCharging(false);

        EntityEquipment equipment = fairy.getEquipment();

        player.getWorld().playSound(fairyLocation, Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1f, 1.5f);

        // 1단계(0.5초 후): 플레이어에게서 아이템을 제거하고 요정이 들게 함 (가져감)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (fairy.isDead()) {
                    cancel();
                    return;
                }

                chosen.apply.accept(null);
                if (equipment != null) {
                    equipment.setItemInMainHand(target.clone());
                }
                player.getWorld().playSound(fairy.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);

                // 2단계(2초 후): 인챈트 적용
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (fairy.isDead()) {
                            cancel();
                            return;
                        }

                        target.addUnsafeEnchantment(enchantment, level);
                        if (equipment != null) {
                            equipment.setItemInMainHand(target.clone());
                        }
                        player.getWorld().playSound(fairy.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);

                        // 3단계(1초 후): 플레이어에게 돌려주고 요정 사라짐
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                chosen.apply.accept(target);
                                if (!fairy.isDead()) {
                                    fairy.remove();
                                }
                                onFinished.accept(enchantment, level);
                            }
                        }.runTaskLater(plugin, 20L);

                        cancel();
                    }
                }.runTaskLater(plugin, 40L);

                cancel();
            }
        }.runTaskLater(plugin, 10L);

        return true;
    }

    private List<Candidate> collectCandidates(Player player) {
        List<Candidate> result = new ArrayList<>();

        addIfEnchantable(result, player.getInventory().getItemInMainHand(),
                item -> player.getInventory().setItemInMainHand(item));
        addIfEnchantable(result, player.getInventory().getHelmet(),
                item -> player.getInventory().setHelmet(item));
        addIfEnchantable(result, player.getInventory().getChestplate(),
                item -> player.getInventory().setChestplate(item));
        addIfEnchantable(result, player.getInventory().getLeggings(),
                item -> player.getInventory().setLeggings(item));
        addIfEnchantable(result, player.getInventory().getBoots(),
                item -> player.getInventory().setBoots(item));

        ItemStack[] contents = player.getInventory().getStorageContents();
        for (int i = 0; i < contents.length; i++) {
            int slot = i;
            addIfEnchantable(result, contents[i],
                    item -> player.getInventory().setItem(slot, item));
        }

        return result;
    }

    private void addIfEnchantable(List<Candidate> list, ItemStack item, Consumer<ItemStack> apply) {
        if (isEnchantable(item)) {
            list.add(new Candidate(item, apply));
        }
    }

    private boolean isEnchantable(ItemStack item) {
        return item != null && item.getType().isItem() && !item.getType().isAir();
    }

    private Enchantment pickRandomEnchantment(ItemStack target) {
        List<Enchantment> candidates = new ArrayList<>();
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.canEnchantItem(target)) {
                candidates.add(enchantment);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(random.nextInt(candidates.size()));
    }

    private static class Candidate {
        final ItemStack item;
        final Consumer<ItemStack> apply;

        Candidate(ItemStack item, Consumer<ItemStack> apply) {
            this.item = item;
            this.apply = apply;
        }
    }
}
