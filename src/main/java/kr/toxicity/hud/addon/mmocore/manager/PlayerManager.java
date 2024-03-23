package kr.toxicity.hud.addon.mmocore.manager;

import kr.toxicity.hud.addon.mmocore.player.SkillPlayer;
import kr.toxicity.hud.api.event.HudPlayerJoinEvent;
import kr.toxicity.hud.api.event.HudPlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static kr.toxicity.hud.addon.mmocore.util.EventUtil.*;

public class PlayerManager implements Manager {
    private final Map<UUID, SkillPlayer> skillPlayerMap = new ConcurrentHashMap<>();
    @Override
    public void start() {
        register(new Listener() {
            @EventHandler
            public void join(@NotNull HudPlayerJoinEvent e) {
                var player = e.getPlayer();
                skillPlayerMap.computeIfAbsent(player.getUniqueId(), uuid -> new SkillPlayer(player));
            }
            @EventHandler
            public void quit(@NotNull HudPlayerQuitEvent e) {
                var remove = skillPlayerMap.remove(e.getPlayer().getUniqueId());
                if (remove != null) remove.cancel();
            }
        });
    }

    @Override
    public void reload() {
        skillPlayerMap.values().forEach(SkillPlayer::clear);
    }

    @Override
    public void end() {
        skillPlayerMap.values().forEach(SkillPlayer::cancel);
    }
}
