package dev.gotiger.gTDonationEvent.action.hazard.instantdeath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InstantDeathManager implements Listener {

    private final Set<UUID> pending = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void kill(Player target) {
        UUID id = target.getUniqueId();
        pending.add(id);
        try {
            target.setHealth(0);
        } finally {
            pending.remove(id);
        }
    }

    public boolean isInstantDeath(Player player) {
        return pending.contains(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (pending.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
