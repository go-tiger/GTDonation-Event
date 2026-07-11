package dev.gotiger.gTDonationEvent.action.soulout;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SoulOutManager implements Listener {

    private static final String DISPLAY_NAME = ChatColor.LIGHT_PURPLE + "유체이탈의 부적";

    private final JavaPlugin plugin;
    private final NamespacedKey key;
    private final Map<UUID, Session> activeSessions = new HashMap<>();

    public SoulOutManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "soul_out_charm");
    }

    public ItemStack createCharm() {
        ItemStack item = new ItemStack(Material.PHANTOM_MEMBRANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(DISPLAY_NAME);
        meta.setLore(List.of(
                ChatColor.GRAY + "우클릭하면 잠시 영혼 상태가 되어",
                ChatColor.GRAY + "주변을 뚫어볼 수 있습니다.",
                ChatColor.GRAY + "시간이 끝나면 원래 위치로 돌아옵니다."
        ));
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public boolean isActive(Player player) {
        return activeSessions.containsKey(player.getUniqueId());
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (!isCharm(item)) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (isActive(player)) {
            player.sendMessage(ChatColor.RED + "이미 유체이탈 상태입니다.");
            return;
        }

        start(player);
        item.setAmount(item.getAmount() - 1);
    }

    private void start(Player player) {
        int durationSeconds = plugin.getConfig().getInt("soul-out.duration-seconds", 30);

        Location origin = player.getLocation().clone();
        GameMode originalGameMode = player.getGameMode();

        player.setGameMode(GameMode.SPECTATOR);
        player.getWorld().playSound(origin, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.7f);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "유체이탈 상태가 되었습니다. (" + durationSeconds + "초)");

        Session session = new Session(origin, originalGameMode);
        activeSessions.put(player.getUniqueId(), session);

        session.task = new BukkitRunnable() {
            @Override
            public void run() {
                end(player);
            }
        };
        session.task.runTaskLater(plugin, durationSeconds * 20L);
    }

    private void end(Player player) {
        Session session = activeSessions.remove(player.getUniqueId());
        if (session == null) {
            return;
        }

        if (session.task != null) {
            session.task.cancel();
        }

        player.teleport(session.origin);
        player.setGameMode(session.originalGameMode);
        player.getWorld().playSound(session.origin, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.2f);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "원래 위치로 돌아왔습니다.");
    }

    private boolean isCharm(ItemStack item) {
        if (item == null || item.getType() != Material.PHANTOM_MEMBRANE || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    private static class Session {
        final Location origin;
        final GameMode originalGameMode;
        BukkitRunnable task;

        Session(Location origin, GameMode originalGameMode) {
            this.origin = origin;
            this.originalGameMode = originalGameMode;
        }
    }
}
