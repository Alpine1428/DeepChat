package com.alpine.holyworldai;

import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class ChatMonitor {

    private static final Pattern COLOR_PATTERN = Pattern.compile("§.");
    private static final Pattern CHECK_PATTERN =
            Pattern.compile("\\[CHECK\\]\\s+(\\S+)\\s+->\\s+(.+)");

    private static final int CONTEXT_LIMIT = 8;

    private final List<String> dialogueHistory = new ArrayList<>();

    private final Path dataFile =
            Paths.get("config/holyworldai/dialogues.txt");

    // =================== RECEIVE ===================

    public void onChatMessage(String fullMessage) {

        String clean = COLOR_PATTERN.matcher(fullMessage).replaceAll("");
        Matcher matcher = CHECK_PATTERN.matcher(clean);

        if (matcher.find()) {

            String nick = matcher.group(1);
            String message = matcher.group(2);

            if (HolyWorldAIClient.learning) {
                addLine("PLAYER: " + message);
            }

            if (HolyWorldAIClient.autoReply) {
                new Thread(() -> {
                    String response =
                            DeepSeekService.ask(message, getRecentContext());

                    if (response != null && !response.isEmpty()) {
                        MinecraftClient.getInstance().execute(() ->
                                MinecraftClient.getInstance()
                                        .player.networkHandler
                                        .sendChatMessage(response)
                        );

                        if (HolyWorldAIClient.learning) {
                            addLine("MOD: " + response);
                        }
                    }
                }).start();
            }
        }
    }

    // =================== SEND ===================

    public void onSendMessage(String message) {
        if (HolyWorldAIClient.learning && !message.startsWith("/")) {
            addLine("MOD: " + message);
        }
    }

    // =================== CONTEXT ===================

    private void addLine(String line) {
        dialogueHistory.add(line);
        System.out.println("AI LOG: " + line);
    }

    private String getRecentContext() {
        int start = Math.max(0, dialogueHistory.size() - CONTEXT_LIMIT);
        return String.join("\n", dialogueHistory.subList(start, dialogueHistory.size()));
    }

    // =================== SAVE / LOAD ===================

    public void saveToFile() {
        try {
            Files.createDirectories(dataFile.getParent());
            Files.write(dataFile, dialogueHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        try {
            if (Files.exists(dataFile)) {
                dialogueHistory.addAll(Files.readAllLines(dataFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
