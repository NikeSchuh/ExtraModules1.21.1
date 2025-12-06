package de.nike.extramodules2.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class TranslationUtils {

    public static String getTranslation(String key) {
        return Component.translatable(key).withStyle(Style.EMPTY).getString();
    }

    public static Component string(String s) {
        return Component.literal(s);
    }
}
