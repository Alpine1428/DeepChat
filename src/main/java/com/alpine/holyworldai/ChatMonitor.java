package com.alpine.holyworldai;

import net.minecraft.client.MinecraftClient;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMonitor {

    private static final Pattern COLOR_PATTERN = Pattern.compile("§.");
    private static final Pattern CHECK_PATTERN =
            Pattern.compile("\\[CHECK\\]\\s+(\\S+)\\s+->\\s+(.+)");

    private final Random random = new Random();

    public void onChatMessage(String fullMessage) {

        if (!HolyWorldAIClient.autoReply) return;

        String clean = COLOR_PATTERN.matcher(fullMessage).replaceAll("");

        Matcher matcher = CHECK_PATTERN.matcher(clean);
        if (!matcher.find()) return;

        String msg = matcher.group(2).toLowerCase().trim();

        String response = generateResponse(msg);

        if (response != null) {
            MinecraftClient.getInstance().execute(() ->
                    MinecraftClient.getInstance().player
                            .networkHandler.sendChatMessage(response)
            );
        }
    }

    private String generateResponse(String msg) {

        if (msg.contains("за что") || msg.contains("причин")) {
            return randomFrom(
                    "Проверка по репортам.",
                    "Многочисленные жалобы.",
                    "Подозрительная активность."
            );
        }

        if (msg.contains("дс") || msg.contains("discord")) {
            return randomFrom(
                    "Через Discord проверки не проводим.",
                    "Только AnyDesk или RustDesk.",
                    "Нет."
            );
        }

        if (msg.contains("я не читер") || msg.contains("я чист")) {
            return randomFrom(
                    "Тогда проблем не будет.",
                    "Сейчас проверим.",
                    "Скачивайте AnyDesk."
            );
        }

        if (msg.contains("я софт") || msg.contains("признаю")) {
            return randomFrom(
                    "Признание уменьшает срок.",
                    "Фиксирую признание.",
                    "Оформляю."
            );
        }

        return randomFrom("Жду.", "+", "AnyDesk.");
    }

    private String randomFrom(String... arr) {
        return arr[random.nextInt(arr.length)];
    }
}
