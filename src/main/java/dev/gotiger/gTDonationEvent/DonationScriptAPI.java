package dev.gotiger.gTDonationEvent;

import dev.gotiger.gTDonationEvent.action.DonationActionRegistry;
import dev.gotiger.gTDonationEvent.action.chat.mining.ChatMiningManager;
import dev.gotiger.gTDonationEvent.config.DonationTarget;
import org.bukkit.entity.Player;

public class DonationScriptAPI {

    private final DonationActionRegistry actionRegistry;
    private final ChatMiningManager chatMiningManager;

    public DonationScriptAPI(DonationActionRegistry actionRegistry, ChatMiningManager chatMiningManager) {
        this.actionRegistry = actionRegistry;
        this.chatMiningManager = chatMiningManager;
    }

    public void getChatMining(Player player, int seconds, String word1, String word2, int radius) {
        chatMiningManager.start(player, seconds, word1, word2, radius, DonationTarget.PLAYER);
    }

    public void getChatMining(Player player, int seconds, String word1, String word2, int radius, DonationTarget target) {
        chatMiningManager.start(player, seconds, word1, word2, radius, target);
    }

    public void getBread(Player player, int amount) {
        run("BREAD", player, amount, DonationTarget.PLAYER);
    }

    public void getBread(Player player, int amount, DonationTarget target) {
        run("BREAD", player, amount, target);
    }

    public void getSteak(Player player, int amount) {
        run("STEAK", player, amount, DonationTarget.PLAYER);
    }

    public void getSteak(Player player, int amount, DonationTarget target) {
        run("STEAK", player, amount, target);
    }

    private void run(String actionName, Player player, int amount, DonationTarget target) {
        actionRegistry.get(actionName).ifPresent(action -> action.applyTo(player, amount, target));
    }
}
