package com.alpine.holyworldai;

import net.minecraft.client.MinecraftClient;

import java.util.*;
import java.util.regex.*;

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

        String playerMessage = matcher.group(2).toLowerCase().trim();

        String response = generateResponse(playerMessage);

        if (response != null && !response.isEmpty()) {
            MinecraftClient.getInstance().execute(() ->
                    MinecraftClient.getInstance().player
                            .networkHandler.sendChatMessage(response)
            );
        }
    }

    private String generateResponse(String msg) {

        // ======== ЗА ЧТО ========
        if (contains(msg, "за что", "за что?", "почему", "причина", "чё вызвал", "что я сделал")) {
            return randomFrom(
                    "Проверка проводится по многочисленным репортам.",
                    "На вас поступили жалобы. Идёт стандартная проверка.",
                    "Подозрительная активность. Проверка обязательна.",
                    "Многочисленные репорты. Начинаем проверку."
            );
        }

        // ======== Я НЕ ЧИТЕР ========
        if (contains(msg, "я не читер", "я чист", "нет читов", "без софта", "я без читов")) {
            return randomFrom(
                    "Отлично. Тогда проверка пройдёт быстро.",
                    "Если вы чисты — проблем не будет.",
                    "Тогда скачивайте AnyDesk и подтвердим.",
                    "Хорошо, сейчас это проверим."
            );
        }

        // ======== ДС МОЖНО ========
        if (contains(msg, "дс", "discord", "по дс", "через дс")) {
            return randomFrom(
                    "Проверки через Discord не проводим.",
                    "По Discord проверки запрещены.",
                    "Только AnyDesk или RustDesk.",
                    "Нет. Проверка строго через удалённый доступ."
            );
        }

        // ======== НЕТ АНИДЕСКА ========
        if (contains(msg, "нет ани", "не качается", "не могу скачать", "не работает ани")) {
            return randomFrom(
                    "Скачивайте с официального сайта anydesk.com.",
                    "Попробуйте RustDesk.",
                    "Если не работает — используйте RustDesk.",
                    "Анидеск обязателен для проверки."
            );
        }

        // ======== СКОЛЬКО ВРЕМЕНИ ========
        if (contains(msg, "сколько времени", "сколько осталось", "время", "минут")) {
            return randomFrom(
                    "У вас 3 минуты.",
                    "Осталось 2 минуты.",
                    "Время ограничено — поторопитесь.",
                    "Таймер идёт."
            );
        }

        // ======== ПРИЗНАНИЕ ========
        if (contains(msg, "я софт", "признаю", "я чит", "бань")) {
            return randomFrom(
                    "Признание уменьшает срок наказания.",
                    "Фиксирую признание.",
                    "Бан будет сокращён за признание.",
                    "Хорошо. Оформляю."
            );
        }

        // ======== ТАЙМЕР ТЯНЕТ ========
        if (contains(msg, "жду", "ну", "ало", "ты тут", "что дальше")) {
            return randomFrom(
                    "Кидайте код.",
                    "Принимайте подключение.",
                    "Жду ID.",
                    "Действуйте быстрее."
            );
        }

        // ======== АГРЕССИЯ ========
        if (contains(msg, "нахуй", "пидор", "хуесос", "долбаеб")) {
            return randomFrom(
                    "Следите за речью.",
                    "Ещё одно подобное сообщение — мут.",
                    "Адекватнее.",
                    "Предупреждение."
            );
        }

        // ======== ПО УМОЛЧАНИЮ ========
        return randomFrom(
                "AnyDesk или признание.",
                "Действуйте.",
                "Жду.",
                "+"
        );
    }

    private boolean contains(String msg, String... words) {
        for (String w : words) {
            if (msg.contains(w)) return true;
        }
        return false;
    }

    private String randomFrom(String... options) {
        return options[random.nextInt(options.length)];
    }
}
