package org.drappula.arcadeCore.util;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.kyori.adventure.util.Ticks;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class MessageUtil {
    public static void sendMessage(Player player, String text) {
        sendMessage(player, text, TagResolver.empty(), 1);
    }
    public static void sendMessage(Player player, String text, TagResolver resolver) {
        sendMessage(player, text, resolver, 1);
    }
    public static void sendMessage(Player player, String text, TagResolver resolver, int bossbarProgress) {
        if (text.isEmpty()) return;
        List<String> messages = Arrays.stream(text.split("(?<!\\\\);")).toList();
        for (String message : messages) {
            if (message.isEmpty()) continue;
            String msgUnprefix = message.replaceFirst("^[a-z]+:", "");
            if (text.startsWith("title:")) player.sendTitlePart(TitlePart.TITLE, MiniMessage.miniMessage().deserialize(msgUnprefix, resolver));
            if (text.startsWith("subtitle:")) player.sendTitlePart(TitlePart.SUBTITLE, MiniMessage.miniMessage().deserialize(msgUnprefix, resolver));
            if (text.startsWith("titletime:")) {
                List<String> times = Arrays.stream(msgUnprefix.split(":")).toList();
                Duration fadeIn = times.get(0).isEmpty() ? Ticks.duration(10) : Duration.ofMillis(Integer.parseInt(times.get(0)));
                Duration stay = times.get(1).isEmpty() ? Ticks.duration(70) : Duration.ofMillis(Integer.parseInt(times.get(1)));
                Duration fadeOut = times.get(2).isEmpty() ? Ticks.duration(20) : Duration.ofMillis(Integer.parseInt(times.get(2)));
                Title.Times titleTimes = Title.Times.times(fadeIn, stay, fadeOut);
                player.sendTitlePart(TitlePart.TIMES, titleTimes);
            }
            if (text.startsWith("actionbar:")) player.sendActionBar(MiniMessage.miniMessage().deserialize(text, resolver));
            if (text.startsWith("message:")) {
                player.sendRichMessage(msgUnprefix.replace("\n", "<newline>"), resolver);
            }
            if (text.startsWith("bossbar:")) {
                List<String> sections = Arrays.stream(msgUnprefix.split(":")).toList();
                String givenProgressString = sections.get(1);
                float progress = givenProgressString.isEmpty() ? bossbarProgress : Float.parseFloat(givenProgressString);
                BossBar.Color color = sections.get(2).isEmpty() ? BossBar.Color.WHITE : BossBar.Color.valueOf(sections.get(2));
                BossBar.Overlay overlay = sections.get(3).isEmpty() ? BossBar.Overlay.PROGRESS : BossBar.Overlay.valueOf(sections.get(3));
                player.showBossBar(BossBar.bossBar(MiniMessage.miniMessage().deserialize(sections.getFirst(), resolver), progress, color, overlay));
            }
        }
    }
}
