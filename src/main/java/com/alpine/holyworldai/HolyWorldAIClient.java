package com.alpine.holyworldai;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class HolyWorldAIClient implements ClientModInitializer {

    public static boolean autoReply = true;
    public static ChatMonitor monitor;

    @Override
    public void onInitializeClient() {

        monitor = new ChatMonitor();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(ClientCommandManager.literal("ai")

                .then(ClientCommandManager.literal("start")
                    .executes(ctx -> {
                        autoReply = true;
                        ctx.getSource().sendFeedback(Text.literal("§aAI ENABLED"));
                        return 1;
                    }))

                .then(ClientCommandManager.literal("stop")
                    .executes(ctx -> {
                        autoReply = false;
                        ctx.getSource().sendFeedback(Text.literal("§cAI DISABLED"));
                        return 1;
                    }))
            );
        });
    }
}
