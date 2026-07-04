package net.bookpage;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookPageMod implements ModInitializer {
    public static final String MOD_ID = "bookpage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("BookPage initialized");
    }
}
