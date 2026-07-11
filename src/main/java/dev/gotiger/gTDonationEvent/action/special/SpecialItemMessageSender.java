package dev.gotiger.gTDonationEvent.action.special;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public final class SpecialItemMessageSender {

    private static final boolean PAPER_AVAILABLE;
    private static Method translationKeyMethod;
    private static Method translatableMethod;
    private static Method textMethod;
    private static Method appendMethod;
    private static Method colorMethod;
    private static Method sendMessageComponentMethod;
    private static Object grayColor;
    private static Object aquaColor;
    private static Object whiteColor;
    private static Object goldColor;

    static {
        boolean available;
        try {
            Class<?> componentClass = Class.forName("net.kyori.adventure.text.Component");
            Class<?> namedTextColorClass = Class.forName("net.kyori.adventure.text.format.NamedTextColor");

            translationKeyMethod = Material.class.getMethod("translationKey");
            translatableMethod = componentClass.getMethod("translatable", String.class);
            textMethod = componentClass.getMethod("text", String.class);
            appendMethod = componentClass.getMethod("append", Class.forName("net.kyori.adventure.text.ComponentLike"));
            colorMethod = componentClass.getMethod("color", Class.forName("net.kyori.adventure.text.format.TextColor"));
            sendMessageComponentMethod = Player.class.getMethod("sendMessage", componentClass);

            grayColor = namedTextColorClass.getField("GRAY").get(null);
            aquaColor = namedTextColorClass.getField("AQUA").get(null);
            whiteColor = namedTextColorClass.getField("WHITE").get(null);
            goldColor = namedTextColorClass.getField("GOLD").get(null);

            available = true;
        } catch (ReflectiveOperationException e) {
            available = false;
        }
        PAPER_AVAILABLE = available;
    }

    private SpecialItemMessageSender() {
    }

    public static void broadcastResultMessage(Player recipient, String donorName, Material material) {
        if (PAPER_AVAILABLE) {
            try {
                sendPaperMessage(recipient, donorName, material);
                return;
            } catch (ReflectiveOperationException ignored) {
                // fall back to legacy message below
            }
        }

        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님의 룰렛 결과: "
                        + ChatColor.GOLD + ItemNameKo.of(material) + ChatColor.GRAY + " 획득!"
        );
    }

    private static void sendPaperMessage(Player recipient, String donorName, Material material) throws ReflectiveOperationException {
        String translationKey = (String) translationKeyMethod.invoke(material);
        Object itemNameComponent = colorMethod.invoke(translatableMethod.invoke(null, translationKey), goldColor);

        Object message = colorMethod.invoke(textMethod.invoke(null, "[후원] "), aquaColor);
        message = appendMethod.invoke(message, colorMethod.invoke(textMethod.invoke(null, donorName), whiteColor));
        message = appendMethod.invoke(message, colorMethod.invoke(textMethod.invoke(null, "님의 룰렛 결과: "), grayColor));
        message = appendMethod.invoke(message, itemNameComponent);
        message = appendMethod.invoke(message, colorMethod.invoke(textMethod.invoke(null, " 획득!"), grayColor));

        for (Player online : recipient.getServer().getOnlinePlayers()) {
            sendMessageComponentMethod.invoke(online, message);
        }
    }
}
