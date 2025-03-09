package kr.toxicity.hud.addon.mmocore.compatibility;

import kr.toxicity.hud.addon.mmocore.BetterHudMMOCore;
import kr.toxicity.hud.api.popup.Popup;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.skill.binding.BoundSkillInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;

public class MMOCoreCompatibility implements Compatibility {

    @Override
    public void apply(@NotNull Player player, @NotNull Map<Integer, Popup> popupMap) {
        if (!BetterHudMMOCore.getInstance().getConfigManager().isEnableMMOCore()) return;
        var data = MMOCore.plugin.playerDataManager.getOrNull(player.getUniqueId());
        if (data == null) return;
        var configPopup = BetterHudMMOCore.getInstance()
                .getConfigManager()
                .getSkillPopupMap();
        var boundMap = BetterHudMMOCore.getInstance()
                .getConfigManager()
                .getBoundSkillIndexMap();
        var skills = data.getBoundSkills()
                .values()
                .stream()
                .sorted(Comparator.comparingInt(info -> info.getSkillSlot().getSlot()))
                .toList();
        if (skills.isEmpty()) return;
        var first = skills.getFirst().getSkillSlot().getSlot();
        for (BoundSkillInfo skill : skills) {
            var i = skill.getSkillSlot().getSlot() - first;
            var popup = configPopup.get(skill.getClassSkill().getSkill().getHandler().getId());
            if (popup != null) {
                popupMap.put(boundMap.getOrDefault(i, 0) + i, popup);
            }
        }
    }

    public boolean isCasting(@NotNull Player player) {
        var data = MMOCore.plugin.playerDataManager.getOrNull(player.getUniqueId());
        return data != null && data.isCasting();
    }
}
