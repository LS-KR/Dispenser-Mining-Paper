package io.github.elihuso.dispenseminingpaper.config;

import io.github.elihuso.dispenseminingpaper.DispenserMiningPaper;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(DispenserMiningPaper plugin) {
        plugin.saveDefaultConfig();

        config = plugin.getConfig();
    }

    public boolean getEnabled() {
        return config.getBoolean("enabled");
    }

    public boolean getFeatureBreaking() {
        return config.getBoolean("features.breaking");
    }

    public boolean getFeaturePlacing() {
        return config.getBoolean("features.placing");
    }

    public boolean getFeatureProcessing() {
        return config.getBoolean("features.processing");
    }

    public boolean getFeaturePlanting() {
        return config.getBoolean("features.planting");
    }

    public boolean getFeatureBreakBedrocks() {
        return config.getBoolean("features.breakBedrocks");
    }

    public boolean getAllowNegativeDuration() {
        return config.getBoolean("allowToolNegativeDuration");
    }
}
