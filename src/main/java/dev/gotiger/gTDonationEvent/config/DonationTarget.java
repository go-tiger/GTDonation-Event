package dev.gotiger.gTDonationEvent.config;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum DonationTarget {
    PLAYER,
    RANDOM,
    ALL;

    private static final Random RANDOM_SOURCE = new Random();
    private static TargetExclusionConfig exclusionConfig;

    public static void configure(TargetExclusionConfig config) {
        exclusionConfig = config;
    }

    public List<Player> resolve(Player donor) {
        return switch (this) {
            case PLAYER -> List.of(donor);
            case ALL -> onlineExcludingFiltered();
            case RANDOM -> {
                List<Player> candidates = onlineExcludingFiltered();
                if (candidates.isEmpty()) {
                    yield List.of();
                }
                yield List.of(candidates.get(RANDOM_SOURCE.nextInt(candidates.size())));
            }
        };
    }

    private static List<Player> onlineExcludingFiltered() {
        if (exclusionConfig == null) {
            return List.copyOf(Bukkit.getOnlinePlayers());
        }
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> !exclusionConfig.isExcluded(player))
                .collect(Collectors.toUnmodifiableList());
    }
}
