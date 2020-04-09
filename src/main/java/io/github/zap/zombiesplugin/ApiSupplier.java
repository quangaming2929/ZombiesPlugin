package io.github.zap.zombiesplugin;

import com.mojang.datafixers.types.Func;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.scoreboard.IInGameScoreboard;
import io.github.zap.zombiesplugin.scoreboard.InGameScoreBoard;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides a custom element such as scoreboard,...
 */
public class ApiSupplier {
    public final Function<GameManager, IInGameScoreboard> inGameScoreboardSupplier;

    public ApiSupplier(Function<GameManager, IInGameScoreboard> inGameScoreboardSupplier) {
        this.inGameScoreboardSupplier = inGameScoreboardSupplier;
    }

    /**
     * Create a default supplier
     * @return
     */
    public static ApiSupplier defaultSupplier() {
        return new ApiSupplier(InGameScoreBoard::new);
    }
}
