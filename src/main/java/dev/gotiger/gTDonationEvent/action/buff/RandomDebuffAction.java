package dev.gotiger.gTDonationEvent.action.buff;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDebuffAction implements DonationAction {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public RandomDebuffAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "DEBUFF";
    }

    @Override
    public void execute(Player target, int amount) {
        applyDebuff(target);
    }

    public PotionEffectType applyDebuff(Player target) {
        PotionEffectType effectType = pickRandomEffect();
        if (effectType == null) {
            return null;
        }

        int durationSeconds = plugin.getConfig().getInt("random-debuff.duration-seconds", 30);
        int amplifier = plugin.getConfig().getInt("random-debuff.amplifier", 1);

        target.addPotionEffect(new PotionEffect(effectType, durationSeconds * 20, amplifier));
        return effectType;
    }

    private PotionEffectType pickRandomEffect() {
        List<PotionEffectType> available = new ArrayList<>();
        for (String name : plugin.getConfig().getStringList("random-buff.exclude-effects")) {
            PotionEffectType type = PotionEffectType.getByName(name);
            if (type != null) {
                available.add(type);
            }
        }

        if (available.isEmpty()) {
            return null;
        }

        return available.get(random.nextInt(available.size()));
    }
}
