package dev.gotiger.gTDonationEvent.command;

import dev.gotiger.gTDonationEvent.action.status.buff.BuffNameKo;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;

public class EffectsSubCommand implements SubCommand {

    private final JavaPlugin plugin;

    public EffectsSubCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String name() {
        return "effects";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Method translationKeyMethod = resolveTranslationKeyMethod();

        PotionEffectType[] types = PotionEffectType.values();
        sender.sendMessage("등록된 PotionEffectType 총 " + types.length + "개:");
        plugin.getLogger().info("등록된 PotionEffectType 총 " + types.length + "개:");

        for (PotionEffectType type : types) {
            if (type == null) {
                continue;
            }

            String translationKey = "N/A";
            if (translationKeyMethod != null) {
                try {
                    translationKey = (String) translationKeyMethod.invoke(type);
                } catch (ReflectiveOperationException ignored) {
                }
            }

            String line = type.getKey() + " (name=" + type.getName() + ", translationKey=" + translationKey
                    + ", BuffNameKo=" + BuffNameKo.of(type) + ")";
            sender.sendMessage(line);
            plugin.getLogger().info(line);
        }
    }

    private Method resolveTranslationKeyMethod() {
        try {
            return PotionEffectType.class.getMethod("translationKey");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
