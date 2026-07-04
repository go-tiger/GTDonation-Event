package dev.gotiger.gTDonationEvent.command;

import dev.gotiger.gTDonationEvent.action.buff.BuffNameKo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;

public class DebugCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public DebugCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("effects")) {
            listEffects(sender);
            return true;
        }

        sender.sendMessage("사용법: /" + label + " effects");
        return true;
    }

    private void listEffects(CommandSender sender) {
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
