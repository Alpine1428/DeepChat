package com.alpine.holyworldai;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class HolyWorldAIClient implements ClientModInitializer {

    public static boolean learning = true;      // ✅ учится всегда
    public static boolean autoReply = false;

    public static ChatMonitor monitor;

    @Override
    public void onInitializeClient() {

        monitor = new ChatMonitor();
        monitor.loadFromFile(); // ✅ загрузка старых диалогов

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(ClientCommandManager.literal("ai")

                .then(ClientCommandManager.literal("startlesson")
                    .executes(ctx -> {
                        learning = true;
                        ctx.getSource().sendFeedback(Text.literal("§aLearning ENABLED"));
                        return 1;
                    }))

                .then(ClientCommandManager.literal("stoplesson")
                    .executes(ctx -> {
                        learning = false;
                        monitor.saveToFile(); // ✅ сохраняем
                        ctx.getSource().sendFeedback(Text.literal("§cLearning DISABLED"));
                        return 1;
                    }))

                .then(ClientCommandManager.literal("start")
                    .executes(ctx -> {
                        autoReply = true;
                        ctx.getSource().sendFeedback(Text.literal("§aAutoReply ENABLED"));
                        return 1;
                    }))

                .then(ClientCommandManager.literal("stop")
                    .executes(ctx -> {
                        autoReply = false;
                        ctx.getSource().sendFeedback(Text.literal("§cAutoReply DISABLED"));
                        return 1;
                    }))
            );
        });
    }
}
