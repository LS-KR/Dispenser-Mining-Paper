package io.github.elihuso.dispenseminingpaper.listener;

import io.github.elihuso.dispenseminingpaper.persistence.DamageData;
import io.github.elihuso.dispenseminingpaper.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        if (!event.getBlock().getType().equals(Material.DISPENSER)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        var dispenserBlock = event.getBlock();
        var tool = event.getItem();
        var name = tool.getType().name();

        if (!(name.contains("_AXE")
                || name.contains("_PICKAXE")
                || name.contains("_SHOVEL")
                || name.contains("_HOE")
                || name.contains("_SWORD"))) {
            return;
        }

        Block target = dispenserBlock.getRelative(((Directional) dispenserBlock.getBlockData()).getFacing());
        if (target.getType().isAir()) {
            event.setItem(ItemStack.empty());
        } else if (!target.getDrops(tool).isEmpty()) {
            var damaged = Utils.damage(tool, dispenserBlock);

            var data = new DamageData();
            data.originalStack = tool;
            data.newStack = damaged;

            var finalStack = Utils.setDamageData(damaged, data);
            event.setItem(finalStack);

            target.breakNaturally(tool, true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void monitorBlockDispense(BlockDispenseEvent event) {
        var stack = event.getItem();
        var block = event.getBlock();
        var data = Utils.getAndEraseDamageData(stack);
        if (data != null) {
            event.setItem(ItemStack.empty());

            Dispenser dispenser = (Dispenser) (block.getState());
            dispenser.getInventory().removeItem(data.originalStack);
            dispenser.getInventory().addItem(data.newStack);
        }
    }
}
