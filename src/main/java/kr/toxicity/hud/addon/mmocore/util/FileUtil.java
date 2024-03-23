package kr.toxicity.hud.addon.mmocore.util;

import kr.toxicity.hud.addon.mmocore.BetterHudMMOCore;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileUtil {
    private FileUtil() {
        throw new RuntimeException();
    }

    public static @NotNull File getDataFolder() {
        var dataFolder = BetterHudMMOCore.getInstance().getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();
        return dataFolder;
    }
}
