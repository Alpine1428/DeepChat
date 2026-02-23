package com.alpine.holyworldai;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public static void learnFromDialogue(java.util.List<String> dialogue) {
        learnedStyle = String.join("\n", dialogue);
    }

    public static String ask(String playerMessage, java.util.List<String> dialogue) {

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

            String safeMessage = playerMessage.replace("\"", "");

            String jsonBody =
                    "{"
                            + "\"model\":\"deepseek-chat\","
                            + "\"messages\":["
                            + "{"
                            + "\"role\":\"system\","
                            + "\"content\":\"Ты модератор HolyWorld. Отвечай строго и профессионально.\""
                            + "},"
                            + "{"
                            + "\"role\":\"user\","
                            + "\"content\":\"" + safeMessage + "\""
                            + "}"
                            + "]"
                            + "}";

            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            String resp = response.toString();

            int start = resp.indexOf("\"content\":\"") + 11;
            int end = resp.indexOf("\"", start);

            if (start < 11 || end < 0) {
                return "AI parsing error";
            }

            return resp.substring(start, end);

        } catch (Exception e) {
            e.printStackTrace();
            return "AI error";
        }
    }
}
