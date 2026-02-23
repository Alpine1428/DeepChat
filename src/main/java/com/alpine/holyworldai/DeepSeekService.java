
package com.alpine.holyworldai;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class DeepSeekService {

    private static String apiKey = null;
    private static String learnedStyle = "";

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            Path path = Paths.get("config/holyworldai/config.json");
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "{\n  \"api_key\": \"PUT_YOUR_KEY_HERE\"\n}");
            }
            String content = Files.readString(path);
            apiKey = content.split(":")[1].replaceAll("[\"\s}]", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void learnFromDialogue(List<String> dialogue) {
        learnedStyle = String.join("\n", dialogue);
    }

    public static String ask(String playerMessage, List<String> dialogue) {

        if (apiKey == null || apiKey.contains("PUT_YOUR_KEY_HERE")) {
            return "API key not set.";
        }

        try {
            URL url = new URL("https://api.deepseek.com/v1/chat/completions");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setDoOutput(true);

            String body = "{\"model\":\"deepseek-chat\",\"messages\":["
                + "{\"role\":\"system\",\"content\":\"Ты модератор HolyWorld. Отвечай строго.\"},"
                + "{\"role\":\"user\",\"content\":\"" + playerMessage.replace(""", "") + "\"}"
                + "]}";

            try (OutputStream os = con.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) response.append(line);

            String resp = response.toString();
            int start = resp.indexOf("content":"") + 10;
            int end = resp.indexOf(""", start);
            return resp.substring(start, end);

        } catch (Exception e) {
            e.printStackTrace();
            return "AI error";
        }
    }
}
