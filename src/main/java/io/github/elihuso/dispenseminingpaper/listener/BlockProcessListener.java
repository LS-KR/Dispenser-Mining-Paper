package io.github.elihuso.dispenseminingpaper.listener;

import io.github.elihuso.dispenseminingpaper.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BlockProcessListener implements Listener {
    private final Plugin plugin;

    public BlockProcessListener(Plugin plugin) {
        this.plugin = plugin;
    }

    private static final Material[][] WOOD = {
            {Material.OAK_LOG, Material.STRIPPED_OAK_LOG},
            {Material.SPRUCE_LOG, Material.STRIPPED_SPRUCE_LOG},
            {Material.BIRCH_LOG, Material.STRIPPED_BIRCH_LOG},
            {Material.JUNGLE_LOG, Material.STRIPPED_JUNGLE_LOG},
            {Material.ACACIA_LOG, Material.STRIPPED_ACACIA_LOG},
            {Material.DARK_OAK_LOG, Material.STRIPPED_DARK_OAK_LOG},
            {Material.CRIMSON_STEM, Material.STRIPPED_CRIMSON_STEM},
            {Material.WARPED_STEM, Material.STRIPPED_WARPED_STEM}
    };

    @EventHandler
    public void OnProcessWood(BlockDispenseEvent event) {
        if (event.isCancelled())
            return;
        if (!event.getBlock().getType().equals(Material.DROPPER))
            return;

//        if (!Utils.LocalConfigs.allowProcess) {
//            return;
//        }

        Block dropperBlock = event.getBlock();
        ItemStack item = event.getItem();
        Block target = dropperBlock.getRelative(((Directional) dropperBlock.getBlockData()).getFacing());

        if (!item.getType().name().contains("_AXE"))
            return;

        for (Material[] v : WOOD) {
            if (v[0].equals(target.getType())) {
                event.setCancelled(true);
                target.setType(v[1]);
                target.setBlockData(v[1].createBlockData());
                target.getState().update();
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Dropper dropper = (Dropper) (dropperBlock.getState());
            Inventory inventory = dropper.getInventory();
            for (int i = 0; i < inventory.getSize(); ++i) {
                if (item.equals(inventory.getItem(i))) {
                    inventory.setItem(i, Utils.damage(item, dropperBlock));
                }
            }
        }, 1);
    }

    @EventHandler
    public void OnProcessByShovel(BlockDispenseEvent event) {
        if (event.isCancelled())
            return;
        if (!event.getBlock().getType().equals(Material.DROPPER))
            return;

//        if (!Utils.LocalConfigs.allowProcess) {
//            return;
//        }

        Block dropperBlock = event.getBlock();
        ItemStack item = event.getItem();
        Block target = dropperBlock.getRelative(((Directional) dropperBlock.getBlockData()).getFacing());
        Block base = target.getRelative(0, -1, 0);

        if (!item.getType().name().contains("_SHOVEL"))
            return;

        if (!target.getType().isAir() || !base.getType().isAir()) {
            for (Material v : Utils.DIRT) {
                if (v.equals(target.getType())) {
                    event.setCancelled(true);
                    target.setType(Material.DIRT_PATH);
                    target.setBlockData(Material.DIRT_PATH.createBlockData());
                    target.getState().update();
                    break;
                }
            }
        }

        if (!event.isCancelled()) {
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Dropper dropper = (Dropper) (dropperBlock.getState());
            Inventory inventory = dropper.getInventory();
            for (int i = 0; i < inventory.getSize(); ++i) {
                if (item.equals(inventory.getItem(i))) {
                    inventory.setItem(i, Utils.damage(item, dropperBlock));
                }
            }
        }, 1);
    }

    @EventHandler
    public void OnProcessByHoe(BlockDispenseEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!event.getBlock().getType().equals(Material.DROPPER)) {
            return;
        }

//        if (!Utils.LocalConfigs.allowProcess) {
//            return;
//        }

        Block dropperBlock = event.getBlock();
        ItemStack item = event.getItem();
        Block target = dropperBlock.getRelative(((Directional) dropperBlock.getBlockData()).getFacing());
        Block base = target.getRelative(0, -1, 0);

        if (!item.getType().name().contains("_HOE"))
            return;

        if (!target.getType().isAir() || !base.getType().isAir()) {
            for (Material v : Utils.DIRT) {
                if (v.equals(target.getType())) {
                    event.setCancelled(true);
                    target.setType(Material.FARMLAND);
                    target.setBlockData(Material.FARMLAND.createBlockData());
                    target.getState().update();
                    break;
                }
            }
        }

        if (!event.isCancelled()) {
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Dropper dropper = (Dropper) (dropperBlock.getState());
            Inventory inventory = dropper.getInventory();
            for (int i = 0; i < inventory.getSize(); ++i) {
                if (item.equals(inventory.getItem(i))) {
                    inventory.setItem(i, Utils.damage(item, dropperBlock));
                }
            }
        }, 1);
    }
}
