package io.github.elihuso.dispenseminingpaper.utils;

import io.github.elihuso.dispenseminingpaper.DispenserMiningPaper;
import io.github.elihuso.dispenseminingpaper.persistence.DamageData;
import io.github.elihuso.dispenseminingpaper.persistence.DamagePersistenceType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.Random;

public class Utils {
    public static final Random RAND = new Random();

    public static final Material[] DIRT = {
            Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.MYCELIUM, Material.PODZOL, Material.FARMLAND
    };

    public static final Material[] SUGAR_CANE_DIRT = {
            Material.DIRT, Material.COARSE_DIRT, Material.MYCELIUM, Material.PODZOL, Material.GRASS_BLOCK, Material.SAND, Material.RED_SAND
    };

    public static final Material[] FLOWERS = {
            Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY, Material.LILY_OF_THE_VALLEY, Material.CORNFLOWER, Material.WITHER_ROSE, Material.FERN, Material.DEAD_BUSH
    };

    public static final Material[] SANDS = {
            Material.SAND, Material.RED_SAND
    };

    public static ItemStack damage(ItemStack stack, @Nullable Block block) {
        var item = stack.clone();

        if (item.getItemMeta().isUnbreakable()) {
            return item;
        }

        ItemMeta itemMeta = item.getItemMeta();
        int durability = 0;

        if (itemMeta.hasEnchant(Enchantment.DURABILITY)) {
            durability = itemMeta.getEnchantLevel(Enchantment.DURABILITY);
        }

        if (itemMeta instanceof Damageable damageable) {
            if (RAND.nextInt(durability + 1) == 0) {
                damageable.setDamage(damageable.getDamage() + 1);
            }
            item.setItemMeta(itemMeta);

            if (!DispenserMiningPaper.getInstance().getConfigManager().getAllowNegativeDuration()) {
                if (item.getType().getMaxDurability() <= damageable.getDamage()) {
                    item.setAmount(0);
                    if (block != null) {
                        block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ITEM_BREAK, 24, 0);
                    }
                }
            }
        }
        return item;
    }

    public static ItemStack damage(ItemStack item) {
        return damage(item, null);
    }

    public static boolean couldPlace(ItemStack item, Block base) {
        if (item.hasItemMeta()) {
            if (item.getItemMeta() instanceof BlockStateMeta) {
                // Todo: qyl27: block state specific code.
                return false;
            }
        }

        if (item.getType().name().contains("_SAPLING")) {
            for (Material v : DIRT) {
                if (v.equals(base.getType())) {
                    return true;
                }
            }
        } else if (item.getType().name().contains("_MUSHROOM")) {
            return !base.getType().isAir();
        } else if (item.getType().equals(Material.BAMBOO)) {
            if (base.getType().equals(Material.BAMBOO)) {
                return true;
            } else {
                for (Material v : SUGAR_CANE_DIRT) {
                    if (v.equals(base.getType())) {
                        return true;
                    }
                }
            }
        } else if (item.getType().equals(Material.SUGAR_CANE)) {
            if (base.getType().equals(Material.SUGAR_CANE)) {
                return true;
            } else {
                for (Material v : SUGAR_CANE_DIRT) {
                    if (v.equals(base.getType())) {
                        Block[] roundblock = {
                                base.getRelative(1, 0, 0),
                                base.getRelative(0, 0, 1),
                                base.getRelative(-1, 0, 0),
                                base.getRelative(0, 0, -1)
                        };
                        for (Block u : roundblock) {
                            if ((u.getBlockData() instanceof Waterlogged)) {
                                if (((Waterlogged) u.getBlockData()).isWaterlogged()) {
                                    return true;
                                }
                            }
                            if (u.getType().equals(Material.WATER)) {
                                return true;
                            }
                            if (u.getType().equals(Material.FROSTED_ICE)) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (item.getType().equals(Material.NETHER_WART)) {
            return base.getType().equals(Material.SOUL_SAND);
        } else if (item.getType().equals(Material.CACTUS)) {
            if (base.getType().equals(Material.CACTUS)) {
                return true;
            } else {
                for (var v : SANDS) {
                    if (v.equals(base.getType())) {
                        return true;
                    }
                }
            }
        } else {
            boolean q = true;
            for (var v : FLOWERS) {
                if (v.equals(item.getType())) {
                    q = false;
                    for (var u : DIRT) {
                        if (base.getType().equals(u)) {
                            return true;
                        }
                    }
                }
            }
            return q;
        }
        return false;
    }

    public static ItemStack setDamageData(ItemStack stack, DamageData data) {
        var meta = stack.getItemMeta();
        var container = meta.getPersistentDataContainer();
        container.set(DamagePersistenceType.DATA_NAME, DamagePersistenceType.INSTANCE, data);
        stack.setItemMeta(meta);
        return stack;
    }

    public static @Nullable DamageData getAndEraseDamageData(ItemStack stack) {
        var meta = stack.getItemMeta();
        var container = meta.getPersistentDataContainer();
        if (!container.has(DamagePersistenceType.DATA_NAME, DamagePersistenceType.INSTANCE)) {
            return null;
        }
        var data = container.get(DamagePersistenceType.DATA_NAME, DamagePersistenceType.INSTANCE);
        container.remove(DamagePersistenceType.DATA_NAME);
        stack.setItemMeta(meta);
        return data;
    }
}
