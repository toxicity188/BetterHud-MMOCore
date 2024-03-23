package kr.toxicity.hud.addon.mmocore.util;

import kr.toxicity.hud.addon.mmocore.BetterHudMMOCore;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class EventUtil {
    private EventUtil() {
        throw new RuntimeException();
    }

    public static void register(@NotNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, BetterHudMMOCore.getInstance());
    }
}
