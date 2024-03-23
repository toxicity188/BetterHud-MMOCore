package kr.toxicity.hud.addon.mmocore.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class FunctionUtil {
    private FunctionUtil() {
        throw new RuntimeException();
    }
    public static <T> void forEachIndexed(@NotNull Iterable<T> iterable, @NotNull BiConsumer<Integer, T> biConsumer) {
        var iterator = iterable.iterator();
        var i = 0;
        while (iterator.hasNext()) {
            biConsumer.accept(i++, iterator.next());
        }
    }
}
