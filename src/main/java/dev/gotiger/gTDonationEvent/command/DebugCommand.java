package dev.gotiger.gTDonationEvent.command;

import dev.gotiger.gTDonationEvent.action.status.buff.BuffNameKo;
import dev.gotiger.gTDonationEvent.config.DonationConfig;
import dev.gotiger.gTDonationEvent.config.TargetExclusionConfig;
import dev.gotiger.gTDonationEvent.message.MessageService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;
import java.util.UUID;

public class DebugCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final MessageService messageService;
    private final DonationConfig donationConfig;
    private final TargetExclusionConfig targetExclusionConfig;

    public DebugCommand(JavaPlugin plugin, MessageService messageService, DonationConfig donationConfig, TargetExclusionConfig targetExclusionConfig) {
        this.plugin = plugin;
        this.messageService = messageService;
        this.donationConfig = donationConfig;
        this.targetExclusionConfig = targetExclusionConfig;
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

        if (args.length > 0 && args[0].equalsIgnoreCase("ignore")) {
            handleIgnore(sender, args);
            return true;
        }

        sender.sendMessage("사용법: /" + label + " <effects|reload|ignore>");
        return true;
    }

    private void handleIgnore(CommandSender sender, String[] args) {
        if (args.length < 2 || (!args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove"))) {
            sender.sendMessage("사용법: /gtdonationevent ignore <add|remove> [플레이어]");
            return;
        }

        boolean add = args[1].equalsIgnoreCase("add");
        String playerName;

        if (args.length >= 3) {
            playerName = args[2];
        } else if (sender instanceof Player player) {
            playerName = player.getName();
        } else {
            sender.sendMessage("콘솔에서는 대상 플레이어 이름을 지정해야 합니다.");
            return;
        }

        UUID playerId = targetExclusionConfig.resolvePlayerId(playerName);
        boolean changed = add
                ? targetExclusionConfig.excludePlayer(playerId)
                : targetExclusionConfig.includePlayer(playerId);

        if (!changed) {
            String state = add ? "이미 제외되어 있습니다." : "제외 목록에 없습니다.";
            sender.sendMessage(playerName + "님은 " + state);
            return;
        }

        String action = add ? "제외되었습니다." : "제외 목록에서 해제되었습니다.";
        sender.sendMessage(playerName + "님이 후원 대상(ALL/RANDOM)에서 " + action);
    }

    private void reload(CommandSender sender) {
        plugin.reloadConfig();
        messageService.load();
        donationConfig.load(plugin.getConfig());
        targetExclusionConfig.load();
        sender.sendMessage("config.yml, messages.yml, targets.yml 리로드 완료.");
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
