package com.alpine.holyworldai;

import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class ChatMonitor {

    private static final Pattern COLOR_PATTERN = Pattern.compile("§.");
    private static final Pattern CHECK_PATTERN =
            Pattern.compile("\\[CHECK\\]\\s+(\\S+)\\s+->\\s+(.+)");

    private final Path lessonFile =
            Paths.get("config/holyworldai/lesson.txt");

    public ChatMonitor() {
        try {
            Files.createDirectories(lessonFile.getParent());
            if (!Files.exists(lessonFile)) {
                Files.createFile(lessonFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= RECEIVE =================

    public void onChatMessage(String fullMessage) {

        if (!HolyWorldAIClient.learning) return;

        String clean = COLOR_PATTERN.matcher(fullMessage).replaceAll("");
        Matcher matcher = CHECK_PATTERN.matcher(clean);

        if (matcher.find()) {

            String nick = matcher.group(1);
            String message = matcher.group(2);

            writeLine("PLAYER (" + nick + "): " + message);
        }
    }

    // ================= SEND =================

    public void onSendMessage(String message) {

        if (!HolyWorldAIClient.learning) return;

        if (!message.startsWith("/")) {
            writeLine("MOD: " + message);
        }
    }

    // ================= WRITE =================

    private void writeLine(String line) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                lessonFile,
                StandardOpenOption.APPEND)) {

            writer.write(line);
            writer.newLine();

            System.out.println("Saved: " + line);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= READ ALL =================

    public String readAllLessons() {
        try {
            return Files.readString(lessonFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void clearFile() {
        try {
            Files.writeString(lessonFile, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
