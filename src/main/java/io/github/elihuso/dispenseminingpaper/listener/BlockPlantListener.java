package io.github.elihuso.dispenseminingpaper.listener;

import io.github.elihuso.dispenseminingpaper.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BlockPlantListener implements Listener {
    private final Plugin plugin;

    public BlockPlantListener(Plugin plugin) {
        this.plugin = plugin;
    }

    static Material[][] plants = {
            {Material.WHEAT_SEEDS, Material.WHEAT},
            {Material.BEETROOT_SEEDS, Material.BEETROOTS},
            {Material.CARROT, Material.CARROTS},
            {Material.POTATO, Material.POTATOES},
            {Material.PUMPKIN_SEEDS, Material.PUMPKIN_STEM},
            {Material.MELON_SEEDS, Material.MELON_STEM}
    };
    Material[] Dirts = {
            Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.MYCELIUM, Material.PODZOL, Material.FARMLAND
    };
    static Material[][] flowerPotPlants = {
            {Material.DANDELION, Material.POTTED_DANDELION},
            {Material.POPPY, Material.POTTED_POPPY},
            {Material.BLUE_ORCHID, Material.POTTED_BLUE_ORCHID},
            {Material.ALLIUM, Material.POTTED_ALLIUM},
            {Material.AZURE_BLUET, Material.POTTED_AZURE_BLUET},
            {Material.RED_TULIP, Material.POTTED_RED_TULIP},
            {Material.ORANGE_TULIP, Material.POTTED_ORANGE_TULIP},
            {Material.WHITE_TULIP, Material.POTTED_WHITE_TULIP},
            {Material.PINK_TULIP, Material.POTTED_PINK_TULIP},
            {Material.OXEYE_DAISY, Material.POTTED_OXEYE_DAISY},
            {Material.LILY_OF_THE_VALLEY, Material.POTTED_LILY_OF_THE_VALLEY},
            {Material.CORNFLOWER, Material.POTTED_CORNFLOWER},
            {Material.WITHER_ROSE, Material.POTTED_WITHER_ROSE},
            {Material.OAK_SAPLING, Material.POTTED_OAK_SAPLING},
            {Material.SPRUCE_SAPLING, Material.POTTED_SPRUCE_SAPLING},
            {Material.BIRCH_SAPLING, Material.POTTED_BIRCH_SAPLING},
            {Material.JUNGLE_SAPLING, Material.POTTED_JUNGLE_SAPLING},
            {Material.ACACIA_SAPLING, Material.POTTED_ACACIA_SAPLING},
            {Material.DARK_OAK_SAPLING, Material.POTTED_DARK_OAK_SAPLING},
            {Material.RED_MUSHROOM, Material.POTTED_RED_MUSHROOM},
            {Material.BROWN_MUSHROOM, Material.POTTED_BROWN_MUSHROOM},
            {Material.FERN, Material.POTTED_FERN},
            {Material.DEAD_BUSH, Material.POTTED_DEAD_BUSH},
            {Material.CACTUS, Material.POTTED_CACTUS},
            {Material.BAMBOO, Material.POTTED_BAMBOO},
            {Material.CRIMSON_FUNGUS, Material.POTTED_CRIMSON_FUNGUS},
            {Material.CRIMSON_ROOTS, Material.POTTED_CRIMSON_ROOTS},
            {Material.WARPED_FUNGUS, Material.POTTED_WARPED_FUNGUS},
            {Material.WARPED_ROOTS, Material.POTTED_WARPED_ROOTS}
    };

    @EventHandler
    public void OnPlant(BlockDispenseEvent event) {
        if (event.isCancelled())
            return;
        if (!event.getBlock().getType().equals(Material.DISPENSER))
            return;

//        if (!Utils.LocalConfigs.allowPlace) {
//            return;
//        }

        Block dispenserBlock = event.getBlock();
        ItemStack item = event.getItem();
        boolean isselected = false;

        for (Material[] v : plants) {
            if (v[0].equals(item.getType())) {
                isselected = true;
                break;
            }
        }
        if (!isselected)
            return;

        Block target = dispenserBlock.getRelative(((Directional) dispenserBlock.getBlockData()).getFacing());
        Block base = target.getRelative(0, -1, 0);

        if (!target.getType().isAir())
            return;
        if (!base.getType().equals(Material.FARMLAND))
            return;

        event.setCancelled(true);

        isselected = false;
        for (Material[] v : plants) {
            if (v[0].equals(item.getType())) {
                target.setType(v[1]);
                target.setBlockData(v[1].createBlockData());
                target.getState().update();
                isselected = true;
            }
        }

        if (!isselected)
            return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Dispenser dispenser = (Dispenser) (dispenserBlock.getState());
            Inventory inventory = dispenser.getInventory();

            for (int i = 0; i < inventory.getSize(); ++i) {
                ItemStack invItem = inventory.getItem(i);
                if (item.isSimilar(invItem) && (invItem != null)) {
                    invItem.setAmount(invItem.getAmount() - 1);
                    inventory.setItem(i, invItem);
                    break;
                }
            }
        }, 1);
    }

    @EventHandler
    public void OnPlantCocoa(BlockDispenseEvent event) {
        if (event.isCancelled())
            return;
        if (!event.getBlock().getType().equals(Material.DISPENSER))
            return;

//        if (!Utils.LocalConfigs.allowPlace) {
//            return;
//        }

        Block dispenserBlock = event.getBlock();
        ItemStack item = event.getItem();

        BlockFace facing = ((Directional) dispenserBlock.getBlockData()).getFacing();
        Block target = dispenserBlock.getRelative(facing);
        Block base = target.getRelative(facing);

        if (!item.getType().equals(Material.COCOA_BEANS))
            return;
        if (!target.getType().isAir())
            return;
        if (!base.getType().equals(Material.JUNGLE_LOG))
            return;

        event.setCancelled(true);

        target.setType(Material.COCOA);
        Cocoa c = (Cocoa) Material.COCOA.createBlockData();
        c.setFacing(facing);
        target.setBlockData(c);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Dispenser dispenser = (Dispenser) (dispenserBlock.getState());
            Inventory inventory = dispenser.getInventory();

            for (int i = 0; i < inventory.getSize(); ++i) {
                ItemStack invItem = inventory.getItem(i);
                if (item.isSimilar(invItem) && (invItem != null)) {
                    invItem.setAmount(invItem.getAmount() - 1);
                    inventory.setItem(i, invItem);
                    break;
                }
            }
        }, 1);
    }

    @EventHandler
    public void OnPlantSweetBerry(BlockDispenseEvent event) {
        if (event.isCancelled())
            return;
        if (!event.getBlock().getType().equals(Material.DISPENSER))
            return;

//        if (!Utils.LocalConfigs.allowPlace) {
//            return;
//        }

        Block dispenserBlock = event.getBlock();
        ItemStack item = event.getItem();

        BlockFace facing = ((Directional) dispenserBlock.getBlockData()).getFacing();
        Block target = dispenserBlock.getRelative(facing);
        Block base = target.getRelative(0, -1, 0);

        if (!item.getType().equals(Material.SWEET_BERRIES))
            return;
        if (!target.getType().isAir())
            return;
        for (Material v : Dirts) {
            if (v.equals(base.getType())) {
                event.setCancelled(true);
                break;
            }
        }

        if (!event.isCancelled())
            return;
        target.setType(Material.SWEET_BERRY_BUSH);
        target.setBlockData(Material.SWEET_BERRY_BUSH.createBlockData());
        target.getState().update();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Dispenser dispenser = (Dispenser) (dispenserBlock.getState());
            Inventory inventory = dispenser.getInventory();

            for (int i = 0; i < inventory.getSize(); ++i) {
                ItemStack invItem = inventory.getItem(i);
                if (item.isSimilar(invItem) && (invItem != null)) {
                    invItem.setAmount(invItem.getAmount() - 1);
                    inventory.setItem(i, invItem);
                    break;
                }
            }
        }, 1);
    }

    @EventHandler
    public void OnPlantInPot(BlockDispenseEvent event) {

        if (event.isCancelled())
            return;
        if (!event.getBlock().getType().equals(Material.DISPENSER))
            return;

//        if (!Utils.LocalConfigs.allowPlace) {
//            return;
//        }

        Block dispenserBlock = event.getBlock();
        ItemStack item = event.getItem();

        BlockFace facing = ((Directional) dispenserBlock.getBlockData()).getFacing();
        Block target = dispenserBlock.getRelative(facing);

        if (!target.getType().equals(Material.FLOWER_POT))
            return;

        for (Material[] v : flowerPotPlants) {
            if (v[0].equals(item.getType())) {
                event.setCancelled(true);
                target.setType(v[1]);
                target.setBlockData(v[1].createBlockData());
                target.getState().update();
            }
        }

        if (!event.isCancelled())
            return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Dispenser dispenser = (Dispenser) (dispenserBlock.getState());
            Inventory inventory = dispenser.getInventory();

            for (int i = 0; i < inventory.getSize(); ++i) {
                ItemStack invItem = inventory.getItem(i);
                if (item.isSimilar(invItem) && (invItem != null)) {
                    invItem.setAmount(invItem.getAmount() - 1);
                    inventory.setItem(i, invItem);
                    break;
                }
            }
        }, 1);
    }
}
