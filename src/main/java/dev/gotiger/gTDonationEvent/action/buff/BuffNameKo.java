package dev.gotiger.gTDonationEvent.action.buff;

import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public final class BuffNameKo {

    private static final Map<String, String> NAMES = new HashMap<>();

    static {
        NAMES.put("SPEED", "속도 증가");
        NAMES.put("SLOWNESS", "속도 감소");
        NAMES.put("HASTE", "성급함");
        NAMES.put("MINING_FATIGUE", "채굴 피로");
        NAMES.put("STRENGTH", "힘");
        NAMES.put("INSTANT_HEALTH", "즉시 치유");
        NAMES.put("INSTANT_DAMAGE", "즉시 피해");
        NAMES.put("JUMP_BOOST", "점프 강화");
        NAMES.put("NAUSEA", "멀미");
        NAMES.put("REGENERATION", "재생");
        NAMES.put("RESISTANCE", "저항");
        NAMES.put("FIRE_RESISTANCE", "화염 저항");
        NAMES.put("WATER_BREATHING", "수중 호흡");
        NAMES.put("INVISIBILITY", "투명");
        NAMES.put("BLINDNESS", "실명");
        NAMES.put("NIGHT_VISION", "야간 투시");
        NAMES.put("HUNGER", "허기");
        NAMES.put("WEAKNESS", "나약함");
        NAMES.put("POISON", "독");
        NAMES.put("WITHER", "시듦");
        NAMES.put("HEALTH_BOOST", "생명력 강화");
        NAMES.put("ABSORPTION", "흡수");
        NAMES.put("SATURATION", "포화");
        NAMES.put("GLOWING", "발광");
        NAMES.put("LEVITATION", "공중 부양");
        NAMES.put("LUCK", "행운");
        NAMES.put("UNLUCK", "불운");
        NAMES.put("SLOW_FALLING", "느린 낙하");
        NAMES.put("CONDUIT_POWER", "전달체의 힘");
        NAMES.put("DOLPHINS_GRACE", "돌고래의 가호");
        NAMES.put("BAD_OMEN", "흉조");
        NAMES.put("HERO_OF_THE_VILLAGE", "마을의 영웅");
        NAMES.put("DARKNESS", "어둠");
        NAMES.put("TRIAL_OMEN", "시련 징조");
        NAMES.put("RAID_OMEN", "습격 징조");
        NAMES.put("WIND_CHARGED", "돌풍");
        NAMES.put("WEAVING", "방적");
        NAMES.put("OOZING", "점액화");
        NAMES.put("INFESTED", "벌레 먹음");
        NAMES.put("BREATH_OF_THE_NAUTILUS", "앵무조개의 숨결");

        // 레거시 별칭
        NAMES.put("JUMP", "점프 강화");
        NAMES.put("SLOW", "속도 감소");
        NAMES.put("FAST_DIGGING", "성급함");
        NAMES.put("SLOW_DIGGING", "채굴 피로");
        NAMES.put("INCREASE_DAMAGE", "힘");
        NAMES.put("HEAL", "즉시 치유");
        NAMES.put("HARM", "즉시 피해");
        NAMES.put("CONFUSION", "멀미");
        NAMES.put("DAMAGE_RESISTANCE", "저항");
    }

    private BuffNameKo() {
    }

    public static String of(PotionEffectType type) {
        String key = normalize(type.getKey().getKey());
        return NAMES.getOrDefault(key, normalize(type.getName()));
    }

    private static String normalize(String raw) {
        return raw.toUpperCase();
    }
}
