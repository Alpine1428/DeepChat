package com.alpine.holyworldai;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HolyWorldAIClient implements ClientModInitializer {

    public static final Logger LOGGER =
            LoggerFactory.getLogger("holyworldai");

    @Override
    public void onInitializeClient() {

        LOGGER.info("=== HOLYWORLD AI LOADED SUCCESSFULLY ===");

    }
}
