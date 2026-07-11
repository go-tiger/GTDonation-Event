package dev.gotiger.gTDonationEvent.action.inventory.special;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public final class ItemNameKo {

    private static final Map<Material, String> NAMES = new HashMap<>();

    static {
        NAMES.put(Material.NETHERITE_INGOT, "네더라이트 주괴");
        NAMES.put(Material.DIAMOND, "다이아몬드");
        NAMES.put(Material.EMERALD, "에메랄드");
        NAMES.put(Material.GOLDEN_APPLE, "황금 사과");
        NAMES.put(Material.ENCHANTED_GOLDEN_APPLE, "마법이 걸린 황금 사과");
        NAMES.put(Material.NETHER_STAR, "네더의 별");
        NAMES.put(Material.TOTEM_OF_UNDYING, "불사의 토템");
        NAMES.put(Material.ELYTRA, "겉날개");
    }

    private ItemNameKo() {
    }

    public static String of(Material material) {
        return NAMES.getOrDefault(material, material.name());
    }
}
