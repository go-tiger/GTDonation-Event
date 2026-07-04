package dev.gotiger.gTDonationEvent.config;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public enum DonationTarget {
    PLAYER,
    RANDOM,
    ALL;

    private static final Random RANDOM_SOURCE = new Random();

    public List<Player> resolve(Player donor) {
        return switch (this) {
            case PLAYER -> List.of(donor);
            case ALL -> List.copyOf(Bukkit.getOnlinePlayers());
            case RANDOM -> {
                List<Player> online = List.copyOf(Bukkit.getOnlinePlayers());
                if (online.isEmpty()) {
                    yield List.of();
                }
                yield List.of(online.get(RANDOM_SOURCE.nextInt(online.size())));
            }
        };
    }
}
