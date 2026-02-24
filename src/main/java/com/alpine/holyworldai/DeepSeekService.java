package com.alpine.holyworldai;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class DeepSeekService {

    private static String apiKey = null;

    static {
        loadKey();
    }

    private static void loadKey() {
        try {
            Path path = Paths.get("config/holyworldai/config.json");

            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path,
                        "{\n  \"api_key\": \"PUT_YOUR_KEY_HERE\"\n}");
            }

            String content = Files.readString(path);
            apiKey = content
                    .replaceAll(".*\"api_key\"\\s*:\\s*\"", "")
                    .replaceAll("\".*", "")
                    .trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendFullLesson(String lessonText) {

        if (apiKey == null || apiKey.contains("PUT_YOUR_KEY_HERE")) {
            return "API key not set";
        }

        try {
            URL url = new URL("https://api.deepseek.com/v1/chat/completions");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setDoOutput(true);

            String safeText = lessonText.replace("\"", "");

            String json =
                    "{"
                            + "\"model\":\"deepseek-chat\","
                            + "\"messages\":["
                            + "{"
                            + "\"role\":\"system\","
                            + "\"content\":\"Проанализируй стиль модератора.\""
                            + "},"
                            + "{"
                            + "\"role\":\"user\","
                            + "\"content\":\""
                            + safeText
                            + "\""
                            + "}"
                            + "]"
                            + "}";

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "AI error";
        }
    }
}
