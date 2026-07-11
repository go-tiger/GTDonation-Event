package dev.gotiger.gTDonationEvent.command;

import dev.gotiger.gTDonationEvent.action.status.buff.BuffNameKo;
import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.message.MessageService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;

public class DebugCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final MessageService messageService;
    private final DonationConfig donationConfig;

    public DebugCommand(JavaPlugin plugin, MessageService messageService, DonationConfig donationConfig) {
        this.plugin = plugin;
        this.messageService = messageService;
        this.donationConfig = donationConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("effects")) {
            listEffects(sender);
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            reload(sender);
            return true;
        }

        sender.sendMessage("사용법: /" + label + " <effects|reload>");
        return true;
    }

    private void reload(CommandSender sender) {
        plugin.reloadConfig();
        messageService.load();
        donationConfig.load(plugin.getConfig());
        sender.sendMessage("config.yml, messages.yml 리로드 완료.");
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
