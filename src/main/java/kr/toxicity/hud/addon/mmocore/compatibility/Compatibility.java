package kr.toxicity.hud.addon.mmocore.compatibility;

import kr.toxicity.hud.api.popup.Popup;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface Compatibility {
    void apply(@NotNull Player player, @NotNull Map<Integer, Popup> popupMap);
}
