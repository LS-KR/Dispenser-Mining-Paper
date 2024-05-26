package io.github.elihuso.dispenseminingpaper;

import io.github.elihuso.dispenseminingpaper.config.ConfigManager;
import io.github.elihuso.dispenseminingpaper.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DispenserMiningPaper extends JavaPlugin {
    private static DispenserMiningPaper INSTANCE;
    private final ConfigManager configManager;

    public DispenserMiningPaper() {
        INSTANCE = this;

        configManager = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        if (!configManager.getEnabled()) {
            return;
        }

        if (configManager.getFeatureBreaking()) {
            Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        }

        if (configManager.getFeaturePlacing()) {
            Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        }

        if (configManager.getFeaturePlacing()) {
            Bukkit.getPluginManager().registerEvents(new BlockBoneMealListener(this), this);
        }

        if (configManager.getFeaturePlanting()) {
            Bukkit.getPluginManager().registerEvents(new BlockPlantListener(this), this);
        }

        if (configManager.getFeatureProcessing()) {
            Bukkit.getPluginManager().registerEvents(new BlockProcessListener(this), this);
        }

        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Bye~");
    }

    public static DispenserMiningPaper getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}