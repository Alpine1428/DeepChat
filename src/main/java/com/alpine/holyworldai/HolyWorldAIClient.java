package com.alpine.holyworldai.mixin;

import com.alpine.holyworldai.HolyWorldAIClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("TAIL"))
    private void onAddMessage(Text message, CallbackInfo ci) {

        if (HolyWorldAIClient.monitor != null) {
            HolyWorldAIClient.monitor.onChatMessage(message.getString());
        }
    }
}
