package dev.gotiger.gTDonationEvent.action.firework;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class FireworkAction implements DonationAction {

    private static final List<Color> COLORS = List.of(
            Color.RED, Color.ORANGE, Color.YELLOW, Color.LIME, Color.AQUA, Color.BLUE, Color.PURPLE, Color.FUCHSIA
    );
    private static final List<FireworkEffect.Type> TYPES = List.of(
            FireworkEffect.Type.BALL, FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.BURST,
            FireworkEffect.Type.STAR, FireworkEffect.Type.CREEPER
    );

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public FireworkAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "FIREWORK";
    }

    @Override
    public void execute(Player target, int amount) {
        int count = plugin.getConfig().getInt("firework.count", 1);
        int power = plugin.getConfig().getInt("firework.power", 1);

        for (int i = 0; i < count; i++) {
            var firework = target.getWorld().spawn(target.getLocation(), org.bukkit.entity.Firework.class);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.addEffect(randomEffect());
            meta.setPower(power);
            firework.setFireworkMeta(meta);
            firework.detonate();
        }
    }

    private FireworkEffect randomEffect() {
        FireworkEffect.Builder builder = FireworkEffect.builder()
                .with(TYPES.get(random.nextInt(TYPES.size())))
                .withColor(COLORS.get(random.nextInt(COLORS.size())))
                .withFade(COLORS.get(random.nextInt(COLORS.size())));

        if (random.nextBoolean()) {
            builder.withTrail();
        }
        if (random.nextBoolean()) {
            builder.withFlicker();
        }

        return builder.build();
    }
}
