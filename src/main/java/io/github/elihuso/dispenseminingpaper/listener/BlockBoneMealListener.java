package io.github.elihuso.dispenseminingpaper.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BlockBoneMealListener implements Listener {
    private final Plugin plugin;

    public BlockBoneMealListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnDispenseBoneMeal(BlockDispenseEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!event.getBlock().getType().equals(Material.DISPENSER)) {
            return;
        }

//        if (!Utils.LocalConfigs.allowPlace) {
//            return;
//        }

        Block dispenserBlock = event.getBlock();
        Block target = dispenserBlock.getRelative(((Directional) dispenserBlock.getBlockData()).getFacing());
        Block base = target.getRelative(0, -1, 0);
        ItemStack item = event.getItem();

        if (!target.getType().isAir())
            return;
        if (!base.getType().equals(Material.GRASS_BLOCK))
            return;
        if (!item.getType().equals(Material.BONE_MEAL))
            return;

        event.setCancelled(true);
        base.applyBoneMeal(BlockFace.UP);

//        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
//            Dispenser dispenser = (Dispenser) dispenserBlock.getState();
//            Inventory inventory = dispenser.getInventory();
//
//            for (int i = 0; i < inventory.getSize(); ++i) {
//                ItemStack invItem = inventory.getItem(i);
//                if (item.isSimilar(invItem) && (invItem != null)) {
//                    invItem.setAmount(invItem.getAmount() - 1);
//                    inventory.setItem(i, invItem);
//                    break;
//                }
//            }
//        }, 1);
    }
}
