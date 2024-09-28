package kr.toxicity.hud.addon.mmocore;

import kr.toxicity.hud.addon.mmocore.manager.CompatibilityManager;
import kr.toxicity.hud.addon.mmocore.manager.ConfigManager;
import kr.toxicity.hud.addon.mmocore.manager.Manager;
import kr.toxicity.hud.addon.mmocore.manager.PlayerManager;
import kr.toxicity.hud.api.bukkit.event.PluginReloadedEvent;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static kr.toxicity.hud.addon.mmocore.util.PluginUtil.*;
import static kr.toxicity.hud.addon.mmocore.util.EventUtil.*;

@Getter
public final class BetterHudMMOCore extends JavaPlugin {
    private static BetterHudMMOCore instance;

    private final ConfigManager configManager = new ConfigManager();
    private final PlayerManager playerManager = new PlayerManager();
    private final CompatibilityManager compatibilityManager = new CompatibilityManager();

    private final List<Manager> managers = List.of(
            configManager,
            playerManager,
            compatibilityManager
    );

    @Override
    public void onLoad() {
        if (instance != null) throw new RuntimeException();
        instance = this;
    }

    private void reload() {
        managers.forEach(Manager::reload);
    }

    @Override
    public void onEnable() {
        managers.forEach(Manager::start);
        asyncTask(this::reload);
        taskLater(1, () -> register(new Listener() {
            @EventHandler
            public void hudReload(@NotNull PluginReloadedEvent e) {
                asyncTask(() -> {
                    reload();
                    info("BetterHud reload detected - reload complete.");
                });
            }
        }));
    }

    @Override
    public void onDisable() {
        managers.forEach(Manager::end);
        info("Plugin disabled.");
    }

    public static @NotNull BetterHudMMOCore getInstance() {
        return Objects.requireNonNull(instance);
    }
}
