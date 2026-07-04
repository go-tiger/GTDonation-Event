package dev.gotiger.gTDonationEvent.action.buff;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;

public final class BuffMessageSender {

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
    private static Object lightPurpleColor;

    static {
        boolean available;
        try {
            Class<?> componentClass = Class.forName("net.kyori.adventure.text.Component");
            Class<?> translatableComponentClass = Class.forName("net.kyori.adventure.text.TranslatableComponent");
            Class<?> namedTextColorClass = Class.forName("net.kyori.adventure.text.format.NamedTextColor");

            translationKeyMethod = PotionEffectType.class.getMethod("translationKey");
            translatableMethod = Class.forName("net.kyori.adventure.text.Component")
                    .getMethod("translatable", String.class);
            textMethod = componentClass.getMethod("text", String.class);
            appendMethod = componentClass.getMethod("append", Class.forName("net.kyori.adventure.text.ComponentLike"));
            colorMethod = componentClass.getMethod("color", Class.forName("net.kyori.adventure.text.format.TextColor"));
            sendMessageComponentMethod = Player.class.getMethod("sendMessage", componentClass);

            grayColor = namedTextColorClass.getField("GRAY").get(null);
            aquaColor = namedTextColorClass.getField("AQUA").get(null);
            whiteColor = namedTextColorClass.getField("WHITE").get(null);
            lightPurpleColor = namedTextColorClass.getField("LIGHT_PURPLE").get(null);

            available = true;
        } catch (ReflectiveOperationException e) {
            available = false;
        }
        PAPER_AVAILABLE = available;
    }

    private BuffMessageSender() {
    }

    public static void broadcastBuffMessage(Player recipient, String donorName, PotionEffectType effectType) {
        if (PAPER_AVAILABLE) {
            try {
                sendPaperMessage(recipient, donorName, effectType);
                return;
            } catch (ReflectiveOperationException ignored) {
                // fall back to legacy message below
            }
        }

        recipient.getServer().broadcastMessage(
                ChatColor.AQUA + "[후원] " + ChatColor.WHITE + donorName + ChatColor.GRAY + "님이 "
                        + ChatColor.LIGHT_PURPLE + BuffNameKo.of(effectType) + ChatColor.GRAY + " 버프를 지급"
        );
    }

    private static void sendPaperMessage(Player recipient, String donorName, PotionEffectType effectType) throws ReflectiveOperationException {
        String translationKey = (String) translationKeyMethod.invoke(effectType);
        Object buffNameComponent = colorMethod.invoke(translatableMethod.invoke(null, translationKey), lightPurpleColor);

        Object message = colorMethod.invoke(textMethod.invoke(null, "[후원] "), aquaColor);
        message = appendMethod.invoke(message, colorMethod.invoke(textMethod.invoke(null, donorName), whiteColor));
        message = appendMethod.invoke(message, colorMethod.invoke(textMethod.invoke(null, "님이 "), grayColor));
        message = appendMethod.invoke(message, buffNameComponent);
        message = appendMethod.invoke(message, colorMethod.invoke(textMethod.invoke(null, " 버프를 지급"), grayColor));

        for (Player online : recipient.getServer().getOnlinePlayers()) {
            sendMessageComponentMethod.invoke(online, message);
        }
    }
}
