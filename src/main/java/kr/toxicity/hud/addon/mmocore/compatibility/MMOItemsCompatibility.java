package kr.toxicity.hud.addon.mmocore.compatibility;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import io.lumine.mythic.lib.gson.JsonElement;
import io.lumine.mythic.lib.gson.JsonSyntaxException;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import kr.toxicity.hud.addon.mmocore.BetterHudMMOCore;
import kr.toxicity.hud.api.popup.Popup;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.stat.data.AbilityData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static io.lumine.mythic.lib.skill.trigger.TriggerType.*;

public class MMOItemsCompatibility implements Compatibility {
    private int i = 0;
    private final Map<TriggerType, Integer> typeIntegerMap = new HashMap<>();

    private void register(@NotNull TriggerType type) {
        typeIntegerMap.put(type, i++);
    }

    {

        register(KILL_ENTITY);
        register(KILL_PLAYER);
        register(ATTACK);
        register(DAMAGED);
        register(DAMAGED_BY_ENTITY);
        register(DEATH);
        register(PLACE_BLOCK);
        register(BREAK_BLOCK);
        register(SHOOT_BOW);
        register(ARROW_TICK);
        register(ARROW_HIT);
        register(ARROW_LAND);
        register(SHOOT_TRIDENT);
        register(TRIDENT_TICK);
        register(TRIDENT_HIT);
        register(TRIDENT_LAND);
        register(LOGIN);
        register(TELEPORT);
        register(EQUIP_ARMOR);
        register(UNEQUIP_ARMOR);
        register(TIMER);
        register(CAST);
        register(API);

        register(LEFT_CLICK);
        register(RIGHT_CLICK);
        register(SHIFT_LEFT_CLICK);
        register(SHIFT_RIGHT_CLICK);
        register(SNEAK);
        register(DROP_ITEM);
        register(SHIFT_DROP_ITEM);
        register(SWAP_ITEMS);
        register(SHIFT_SWAP_ITEMS);
    }

    @Override
    public void apply(@NotNull Player player, @NotNull Map<Integer, Popup> popupMap) {
        var configPopup = BetterHudMMOCore.getInstance()
                .getConfigManager()
                .getSkillPopupMap();
        var weaponMap = BetterHudMMOCore.getInstance()
                .getConfigManager()
                .getWeaponSkillIndexMap();
        var i = 0;
        for (ComparableSkill ability : getSkillFromItem(player.getInventory().getItemInMainHand())) {
            var popup = configPopup.get(ability.handler.getId());
            if (popup != null) {
                popupMap.put(weaponMap.getOrDefault(i, 0) + i, popup);
            }
            i++;
        }
    }

    @NotNull
    private List<ComparableSkill> getSkillFromItem(@NotNull ItemStack itemStack) {
        var item = MythicLib.plugin.getVersion()
                .getWrapper()
                .getNBTItem(itemStack);
        var relevantTags = new ArrayList<ItemTag>();
        if (item.hasTag(ItemStats.ABILITIES.getNBTPath())) {
            relevantTags.add(ItemTag.getTagAtPath(ItemStats.ABILITIES.getNBTPath(), item, SupportedNBTTagValues.STRING));
        }
        var jsonCompact = ItemTag.getTagAtPath(ItemStats.ABILITIES.getNBTPath(), relevantTags);
        var list = new ArrayList<ComparableSkill>();
        if (jsonCompact != null && jsonCompact.getValue() instanceof String string) {
            try {
                for (JsonElement e : io.lumine.mythic.lib.gson.JsonParser.parseString(string).getAsJsonArray()) {
                    if (!e.isJsonObject()) continue;
                    var data = new AbilityData(e.getAsJsonObject());
                    list.add(new ComparableSkill(data.getHandler(), data.getTrigger()));
                }
            } catch (IllegalStateException | JsonSyntaxException ignored) {
                //don't care
            }
        }
        list.sort(Comparator.naturalOrder());
        return list;
    }

    private class ComparableSkill implements Comparable<ComparableSkill> {
        private final SkillHandler<?> handler;
        private final int index;
        private ComparableSkill(@NotNull SkillHandler<?> handler, @NotNull TriggerType type) {
            this.handler = handler;
            index = typeIntegerMap.getOrDefault(type, - 1);
        }

        @Override
        public int compareTo(@NotNull ComparableSkill o) {
            return index >= 0 && o.index >= 0 ? Integer.compare(index, o.index) : 0;
        }
    }
}
