package kr.toxicity.hud.addon.mmocore.manager;

import kr.toxicity.hud.addon.mmocore.BetterHudMMOCore;
import kr.toxicity.hud.addon.mmocore.util.FileUtil;
import kr.toxicity.hud.api.BetterHud;
import kr.toxicity.hud.api.popup.Popup;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static kr.toxicity.hud.addon.mmocore.util.PluginUtil.warn;

@Getter
public final class ConfigManager implements Manager {
    private final Map<String, Popup> skillPopupMap = new HashMap<>();
    private boolean disableWhenNonCastingMode = false;
    private boolean applyIndex = false;
    private String skillGroupName = "mmocore_skill";

    @Override
    public void start() {

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void reload() {
        //config.yml
        var configFile = new File(FileUtil.getDataFolder(), "config.yml");
        if (!configFile.exists()) BetterHudMMOCore.getInstance().saveResource("config.yml", false);
        try {
            var yaml = YamlConfiguration.loadConfiguration(configFile);
            disableWhenNonCastingMode = yaml.getBoolean("disable-when-non-casting-mode");
            applyIndex = yaml.getBoolean("apply-index");
            var skillGroupNameNullable = yaml.getString("skill-group-name");
            if (skillGroupNameNullable != null) skillGroupName = skillGroupNameNullable;
            else warn("You should define your skill group name!");
        } catch (Exception e) {
            warn("Unable to load config.yml.");
            warn("Reason: " + e.getMessage());
        }

        //skills.yml
        skillPopupMap.clear();
        var skillsFile = new File(FileUtil.getDataFolder(), "skills.yml");
        if (!skillsFile.exists()) {
            try {
                skillsFile.createNewFile();
            } catch (Exception e) {
                warn("Cannot create skills.yml");
                warn("Reason: " + e.getMessage());
                return;
            }
        }
        try {
            var yaml = YamlConfiguration.loadConfiguration(skillsFile);
            yaml.getKeys(false).forEach(k -> {
                var popup = Optional.ofNullable(yaml.getString(k))
                        .map(BetterHud.getInstance().getPopupManager()::getPopup)
                        .orElse(null);
                if (popup == null) warn("This popup doesn't exist: " + k);
                else skillPopupMap.put(k, popup);
            });
        } catch (Exception e) {
            warn("Unable to load skills.yml.");
            warn("Reason: " + e.getMessage());
        }
    }

    @Override
    public void end() {

    }
}
