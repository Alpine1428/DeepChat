package com.alpine.holyworldai;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class HolyWorldAIClient implements ClientModInitializer {

    public static boolean learning = true;

    public static ChatMonitor monitor;

    @Override
    public void onInitializeClient() {

        monitor = new ChatMonitor();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(ClientCommandManager.literal("ai")

                .then(ClientCommandManager.literal("stoplesson")
                    .executes(ctx -> {

                        learning = false;

                        String allText = monitor.readAllLessons();

                        ctx.getSource().sendFeedback(
                                Text.literal("§aSending lesson to DeepSeek..."));

                        new Thread(() -> {
                            String response =
                                    DeepSeekService.sendFullLesson(allText);

                            System.out.println("DeepSeek response:");
                            System.out.println(response);
                        }).start();

                        return 1;
                    }))

                .then(ClientCommandManager.literal("startlesson")
                    .executes(ctx -> {
                        learning = true;
                        ctx.getSource().sendFeedback(
                                Text.literal("§aLearning ENABLED"));
                        return 1;
                    }))
            );
        });
    }
}
