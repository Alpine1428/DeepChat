
package com.alpine.holyworldai;

import net.minecraft.client.MinecraftClient;
import java.util.*;
import java.util.regex.*;

public class ChatMonitor {

    private final List<String> lessons = new ArrayList<>();
    private static final Pattern COLOR = Pattern.compile("§.");
    private static final Pattern CHECK = Pattern.compile("\\[CHECK\\]\\s*(\\S+)\\s*->\\s*(.+)");

    public void clear() {
        lessons.clear();
    }

    public void onSend(String msg) {
        if (HolyWorldAIClient.learning && !msg.startsWith("/")) {
            lessons.add("MOD: " + msg);
        }
    }

    public void onReceive(String msg) {

        String clean = COLOR.matcher(msg).replaceAll("");
        Matcher m = CHECK.matcher(clean);

        if (m.find()) {
            String playerMsg = m.group(2);

            if (HolyWorldAIClient.learning) {
                lessons.add("PLAYER: " + playerMsg);
            }

            if (HolyWorldAIClient.autoReply) {
                new Thread(() -> {
                    String response = DeepSeekService.ask(playerMsg, lessons);
                    if (response != null) {
                        MinecraftClient.getInstance().execute(() ->
                            MinecraftClient.getInstance().player.networkHandler.sendChatMessage(response)
                        );
                    }
                }).start();
            }
        }
    }

    public void analyzeLearning() {
        DeepSeekService.learnFromDialogue(lessons);
    }
}
