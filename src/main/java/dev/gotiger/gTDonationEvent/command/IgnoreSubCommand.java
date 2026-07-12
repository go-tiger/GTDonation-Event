package dev.gotiger.gTDonationEvent.command;

import dev.gotiger.gTDonationEvent.config.TargetExclusionConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IgnoreSubCommand implements SubCommand {

    private final TargetExclusionConfig targetExclusionConfig;

    public IgnoreSubCommand(TargetExclusionConfig targetExclusionConfig) {
        this.targetExclusionConfig = targetExclusionConfig;
    }

    @Override
    public String name() {
        return "ignore";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
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
}
