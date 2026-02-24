package com.alpine.holyworldai;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMonitor {

    // Удаляем цвет-коды §a §l и т.д.
    private static final Pattern COLOR_PATTERN = Pattern.compile("§.");

    // Формат:
    // [CHECK] Nick -> message
    private static final Pattern CHECK_PATTERN =
            Pattern.compile("\\[CHECK\\]\\s+(\\S+)\\s+->\\s+(.+)");

    private final List<String> playerMessages = new ArrayList<>();
    private final List<String> moderatorMessages = new ArrayList<>();

    public void clear() {
        playerMessages.clear();
        moderatorMessages.clear();
    }

    // ===================== СООБЩЕНИЕ ИГРОКА =====================

    public void onChatMessage(String fullMessage) {

        // Убираем цвет
        String clean = COLOR_PATTERN.matcher(fullMessage).replaceAll("");

        Matcher matcher = CHECK_PATTERN.matcher(clean);

        if (matcher.find()) {

            String nick = matcher.group(1);
            String message = matcher.group(2);

            playerMessages.add(message);

            System.out.println("✅ CHECK PLAYER DETECTED");
            System.out.println("Nick: " + nick);
            System.out.println("Message: " + message);
        }
    }

    // ===================== ТВОИ СООБЩЕНИЯ =====================

    public void onSendMessage(String message) {

        if (!message.startsWith("/")) {
            moderatorMessages.add(message);
            System.out.println("✅ MOD MESSAGE: " + message);
        }
    }

    public List<String> getPlayerMessages() {
        return playerMessages;
    }

    public List<String> getModeratorMessages() {
        return moderatorMessages;
    }
}
