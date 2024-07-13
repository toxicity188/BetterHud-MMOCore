package kr.toxicity.hud.addon.mmocore.manager;

import kr.toxicity.hud.addon.mmocore.compatibility.Compatibility;
import kr.toxicity.hud.addon.mmocore.compatibility.MMOCoreCompatibility;
import kr.toxicity.hud.addon.mmocore.compatibility.MMOItemsCompatibility;
import kr.toxicity.hud.api.popup.Popup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompatibilityManager implements Manager {
    private MMOCoreCompatibility mmoCoreCompatibility;
    private MMOItemsCompatibility mmoItemsCompatibility;

    private final List<Compatibility> compatibilities = new ArrayList<>();

    @Override
    public void start() {
        var manager = Bukkit.getPluginManager();
        if (manager.isPluginEnabled("MMOCore")) {
            mmoCoreCompatibility = new MMOCoreCompatibility();
            compatibilities.add(mmoCoreCompatibility);
        }
        if (manager.isPluginEnabled("MMOItems")) {
            mmoItemsCompatibility = new MMOItemsCompatibility();
            compatibilities.add(mmoItemsCompatibility);
        }
    }

    public void apply(@NotNull Player player, @NotNull Map<Integer, Popup> popupMap) {
        compatibilities.forEach(i -> i.apply(player, popupMap));
    }

    public boolean isCasting(@NotNull Player player) {
        var api = mmoCoreCompatibility;
        return api != null && api.isCasting(player);
    }

    @Override
    public void reload() {

    }

    @Override
    public void end() {

    }
}
