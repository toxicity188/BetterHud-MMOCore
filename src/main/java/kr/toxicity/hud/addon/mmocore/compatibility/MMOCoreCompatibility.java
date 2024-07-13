package kr.toxicity.hud.addon.mmocore.compatibility;

import kr.toxicity.hud.addon.mmocore.BetterHudMMOCore;
import kr.toxicity.hud.api.popup.Popup;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.MMOCoreAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MMOCoreCompatibility implements Compatibility {

    @Override
    public void apply(@NotNull Player player, @NotNull Map<Integer, Popup> popupMap) {
        var data = MMOCore.plugin.playerDataManager.getOrNull(player.getUniqueId());
        if (data == null) return;
        var configPopup = BetterHudMMOCore.getInstance()
                .getConfigManager()
                .getSkillPopupMap();
        var boundMap = BetterHudMMOCore.getInstance()
                .getConfigManager()
                .getBoundSkillIndexMap();
        for (int i = 0; i < 9; i++) {
            var skill = data.getBoundSkill(i + 1);
            if (skill != null) {
                var popup = configPopup.get(skill.getSkill().getHandler().getId());
                if (popup != null) {
                    popupMap.put(boundMap.getOrDefault(i, 0) + i, popup);
                }
            }
        }
    }

    public boolean isCasting(@NotNull Player player) {
        var data = MMOCore.plugin.playerDataManager.getOrNull(player.getUniqueId());
        return data != null && data.isCasting();
    }
}
