package kr.toxicity.hud.addon.mmocore.util;

import kr.toxicity.hud.addon.mmocore.BetterHudMMOCore;
import kr.toxicity.hud.api.BetterHud;
import kr.toxicity.hud.api.scheduler.HudTask;
import org.jetbrains.annotations.NotNull;

public class PluginUtil {
    private PluginUtil() {
        throw new RuntimeException();
    }
    public static void info(@NotNull String message) {
        BetterHudMMOCore.getInstance().getLogger().info(message);
    }
    public static void warn(@NotNull String message) {
        BetterHudMMOCore.getInstance().getLogger().warning(message);
    }
    public static @NotNull HudTask asyncTask(@NotNull Runnable runnable) {
        return BetterHud.getInstance().getScheduler().asyncTask(BetterHudMMOCore.getInstance(), runnable);
    }
    public static @NotNull HudTask asyncTaskTimer(long delay, long period, @NotNull Runnable runnable) {
        return BetterHud.getInstance().getScheduler().asyncTaskTimer(BetterHudMMOCore.getInstance(), delay, period, runnable);
    }
}
