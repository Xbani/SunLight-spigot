package su.nightexpress.sunlight.module.greetings.message;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.sunlight.SLPlaceholders;

import java.util.Set;

public class GreetingMessage implements Writeable {

    private final int         priority;
    private final String      message;
    private final Set<String> ranks;
    private final DisplayMode displayMode;

    public GreetingMessage(int priority, @NotNull String message, @NotNull Set<String> ranks, @NotNull DisplayMode displayMode) {
        this.ranks = ranks;
        this.priority = priority;
        this.message = message;
        this.displayMode = displayMode;
    }

    @NotNull
    public static GreetingMessage read(@NotNull FileConfig config, @NotNull String path) {
        Set<String> ranks = Lists.modify(config.getStringSet(path + ".Ranks"), String::toLowerCase);
        int priority = config.getInt(path + ".Priority", 0);
        String message = config.getString(path + ".Message", "");
        DisplayMode displayMode = config.getEnum(path + ".DisplayMode", DisplayMode.class, DisplayMode.CHAT);

        return new GreetingMessage(priority, message, ranks, displayMode);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Ranks", this.ranks);
        config.set(path + ".Priority", this.priority);
        config.set(path + ".Message", this.message);
        config.set(path + ".DisplayMode", this.displayMode.name());
    }

    public boolean isApplicable(@NotNull Player player) {
        if (this.ranks.isEmpty()) return false;
        if (this.ranks.contains(SLPlaceholders.WILDCARD)) return true;

        Set<String> groups = Players.getInheritanceGroups(player);
        return this.ranks.stream().anyMatch(groups::contains);
    }

    @NotNull
    public Set<String> getRanks() {
        return this.ranks;
    }

    public int getPriority() {
        return this.priority;
    }

    @NotNull
    public String getMessage() {
        return this.message;
    }

    @NotNull
    public DisplayMode getDisplayMode() {
        return this.displayMode;
    }
}
