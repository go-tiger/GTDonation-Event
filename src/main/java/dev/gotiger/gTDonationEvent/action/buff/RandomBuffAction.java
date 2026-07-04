package dev.gotiger.gTDonationEvent.action.buff;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomBuffAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public RandomBuffAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "BUFF";
    }

    @Override
    public void execute(Player target, int amount) {
        applyBuff(target);
    }

    public PotionEffectType applyBuff(Player target) {
        PotionEffectType effectType = pickRandomEffect();
        if (effectType == null) {
            return null;
        }

        int durationSeconds = plugin.getConfig().getInt("random-buff.duration-seconds", 30);
        int amplifier = plugin.getConfig().getInt("random-buff.amplifier", 1);

        target.addPotionEffect(new PotionEffect(effectType, durationSeconds * 20, amplifier));
        return effectType;
    }

    private PotionEffectType pickRandomEffect() {
        Set<PotionEffectType> excluded = new HashSet<>();
        for (String name : plugin.getConfig().getStringList("random-buff.exclude-effects")) {
            PotionEffectType type = PotionEffectType.getByName(name);
            if (type != null) {
                excluded.add(type);
            }
        }

        List<PotionEffectType> available = new ArrayList<>();
        for (PotionEffectType type : PotionEffectType.values()) {
            if (type != null && !excluded.contains(type)) {
                available.add(type);
            }
        }

        if (available.isEmpty()) {
            return null;
        }

        return available.get(random.nextInt(available.size()));
    }
}
