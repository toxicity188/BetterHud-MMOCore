package kr.toxicity.hud.addon.mmocore.player;

import kr.toxicity.hud.addon.mmocore.BetterHudMMOCore;
import kr.toxicity.hud.api.BetterHud;
import kr.toxicity.hud.api.player.HudPlayer;
import kr.toxicity.hud.api.popup.Popup;
import kr.toxicity.hud.api.popup.PopupUpdater;
import kr.toxicity.hud.api.scheduler.HudTask;
import kr.toxicity.hud.api.update.UpdateEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static kr.toxicity.hud.addon.mmocore.util.PluginUtil.*;

public final class SkillPlayer {
    private Collection<Popup> popups = Collections.emptyList();
    private final List<PopupUpdater> updaters = new ArrayList<>();
    private final HudTask task = asyncTaskTimer(1, 1, this::update);
    private final HudPlayer hudPlayer;
    public SkillPlayer(@NotNull Player player) {
        hudPlayer = BetterHud.getInstance().getHudPlayer(player);
    }

    public void update() {
        if (!BetterHudMMOCore.getInstance().getCompatibilityManager().isCasting(hudPlayer.getBukkitPlayer()) && BetterHudMMOCore.getInstance().getConfigManager().isDisableWhenNonCastingMode()) {
            if (!updaters.isEmpty()) clear();
            return;
        }
        var skills = new TreeMap<Integer, Popup>(Comparator.naturalOrder());
        BetterHudMMOCore.getInstance().getCompatibilityManager().apply(hudPlayer.getBukkitPlayer(), skills);
        if (!contentEquals(popups, skills.values())) {
            clear();
            popups = skills.values();
            var applyIndex = BetterHudMMOCore.getInstance().getConfigManager().isApplyIndex();
            skills.forEach((key, value) -> {
                var updater = value.show(UpdateEvent.EMPTY, hudPlayer);
                if (updater != null) {
                    if (applyIndex) updater.setIndex(BetterHudMMOCore.getInstance().getConfigManager().isReversed() ? value.getLastIndex() - key : key);
                    updaters.add(updater);
                }
            });
        } else {
            updaters.forEach(PopupUpdater::update);
        }
    }
    public void clear() {
        popups = Collections.emptyList();
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
