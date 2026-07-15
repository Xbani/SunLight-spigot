package su.nightexpress.sunlight.module;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.configuration.ConfigTypes;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.Locale;

public record ModuleDefinition(boolean enabled, @NotNull String name, @NotNull String prefix) implements Writeable {

    @NotNull
    public static ModuleDefinition named(@NotNull String name) {
        return new ModuleDefinition(true, name, defaultPrefix(name));
    }

    @NotNull
    public static ModuleDefinition read(@NotNull FileConfig config, @NotNull String path) {
        boolean enabled = config.get(ConfigTypes.BOOLEAN, path + ".Enabled", true);
        String name = config.get(ConfigTypes.STRING, path + ".Name", "HG-PvP");
        if (name.isBlank() || name.equalsIgnoreCase("null")) {
            name = "HG-PvP";
        }
        String prefix = config.get(ConfigTypes.STRING, path + ".Prefix", defaultPrefix(name));
        if (prefix.isBlank() || prefix.toUpperCase(Locale.ROOT).contains("NULL")) {
            prefix = defaultPrefix(name);
        }

        return new ModuleDefinition(enabled, name, prefix);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Enabled", this.enabled);
        config.set(path + ".Name", this.name);
        config.set(path + ".Prefix", this.prefix);
    }

    @NotNull
    private static String defaultPrefix(@NotNull String name) {
        return TagWrappers.GRADIENT_3.with("#FFAA00", "#FF8833", "#FF5500").wrap(TagWrappers.BOLD.wrap(name.toUpperCase(Locale.ROOT))) + TagWrappers.DARK_GRAY.wrap(" » ");
    }
}
