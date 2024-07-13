package kr.toxicity.hud.addon.mmocore.util;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ConfigUtil {
    private ConfigUtil() {
        throw new RuntimeException();
    }

    private static <T> void forEach(@NotNull ConfigurationSection section, @NotNull BiFunction<ConfigurationSection, String, T> mapper, @NotNull BiConsumer<String, T> consumer) {
        section.getKeys(false).forEach(s -> {
            var map = mapper.apply(section, s);
            if (map != null) consumer.accept(s, map);
        });
    }

    public static void forEachSubString(@NotNull ConfigurationSection section, @NotNull BiConsumer<String, String> stringConsumer) {
        forEach(section, ConfigurationSection::getString, stringConsumer);
    }
    public static void forEachSubInt(@NotNull ConfigurationSection section, @NotNull BiConsumer<String, Integer> stringConsumer) {
        forEach(section, ConfigurationSection::getInt, stringConsumer);
    }
}
