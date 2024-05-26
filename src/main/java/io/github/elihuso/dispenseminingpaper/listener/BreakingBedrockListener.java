package io.github.elihuso.dispenseminingpaper.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BreakingBedrockListener implements Listener {
    @EventHandler
    public void onDispenseEnchantedGoldenApple(BlockDispenseEvent event) {
        if (!event.getBlock().getType().equals(Material.DISPENSER)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        Block dispenserBlock = event.getBlock();
        ItemStack item = event.getItem();

        if (!item.getType().equals(Material.ENCHANTED_GOLDEN_APPLE)) {
            return;
        }

        Block target = dispenserBlock.getRelative(((Directional) dispenserBlock.getBlockData()).getFacing());
        if (!target.getType().equals(Material.BEDROCK)) {
            return;
        }
        target.setType(Material.AIR);//Break Bedrock but no drop.

//        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
//            Dispenser dispenser = (Dispenser) (dispenserBlock.getState());
//            Inventory inventory = dispenser.getInventory();
//
//            for (int i = 0; i < inventory.getSize(); ++i) {
//                if (item.equals(inventory.getItem(i))) {
//                    ItemStack invItem = inventory.getItem(i);
//                    invItem.setAmount(invItem.getAmount() - 1);
//                    inventory.setItem(i, invItem);
//                }
//            }
//        }, 1);
    }
}
