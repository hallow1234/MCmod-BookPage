package net.bookpage;

import net.fabricmc.api.ClientModInitializer;

public class BookPageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BookPageMod.LOGGER.info("BookPage client initialized");
    }
}
