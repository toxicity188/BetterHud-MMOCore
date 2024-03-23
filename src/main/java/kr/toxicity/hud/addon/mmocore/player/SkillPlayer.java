package kr.toxicity.hud.addon.mmocore.player;

import kr.toxicity.hud.addon.mmocore.BetterHudMMOCore;
import kr.toxicity.hud.api.BetterHud;
import kr.toxicity.hud.api.player.HudPlayer;
import kr.toxicity.hud.api.popup.Popup;
import kr.toxicity.hud.api.popup.PopupUpdater;
import kr.toxicity.hud.api.scheduler.HudTask;
import kr.toxicity.hud.api.update.UpdateEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static kr.toxicity.hud.addon.mmocore.util.FunctionUtil.*;
import static kr.toxicity.hud.addon.mmocore.util.PluginUtil.*;

public final class SkillPlayer {
    private static final List<Popup> EMPTY_LIST = new ArrayList<>();
    private List<Popup> popups = EMPTY_LIST;
    private final List<PopupUpdater> updaters = new ArrayList<>();
    private final HudTask task = asyncTaskTimer(1, 1, this::update);
    private final PlayerData data;
    private final HudPlayer hudPlayer;
    public SkillPlayer(@NotNull Player player) {
        data = BetterHudMMOCore.getInstance().getMmoCoreAPI().getPlayerData(player);
        hudPlayer = BetterHud.getInstance().getHudPlayer(player);
    }

    public void update() {
        if (!data.isCasting() && BetterHudMMOCore.getInstance().getConfigManager().isDisableWhenNonCastingMode()) {
            if (!updaters.isEmpty()) clear();
            return;
        }
        var skills = data.getBoundSkills().stream().map(skill -> BetterHudMMOCore.getInstance()
                .getConfigManager()
                .getSkillPopupMap()
                .get(skill.getSkill()
                        .getHandler()
                        .getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!contentEquals(popups, skills)) {
            clear();
            popups = skills;
            var applyIndex = BetterHudMMOCore.getInstance().getConfigManager().isApplyIndex();
            forEachIndexed(popups, (i, p) -> {
                var updater = p.show(UpdateEvent.EMPTY, hudPlayer);
                if (updater != null) {
                    if (applyIndex) updater.setIndex(i);
                    updaters.add(updater);
                }
            });
        } else {
            updaters.forEach(PopupUpdater::update);
        }
    }
    public void clear() {
        popups.clear();
        updaters.forEach(PopupUpdater::remove);
        updaters.clear();
        var iterator = hudPlayer.getPopupGroupIteratorMap().remove(getSkillGroupName());
        if (iterator != null) iterator.clear();
    }
    private static @NotNull String getSkillGroupName() {
        return BetterHudMMOCore.getInstance().getConfigManager().getSkillGroupName();
    }

    public void cancel() {
        task.cancel();
    }

    private static <T> boolean contentEquals(@NotNull Collection<T> one, @NotNull Collection<T> two) {
        if (one.size() != two.size()) return false;
        var oneIterator = one.iterator();
        var twoIterator = two.iterator();
        for (int i = 0; i < one.size(); i++) {
            if (!oneIterator.next().equals(twoIterator.next())) return false;
        }
        return true;
    }
}
