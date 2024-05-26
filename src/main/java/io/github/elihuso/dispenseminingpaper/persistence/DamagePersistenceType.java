package io.github.elihuso.dispenseminingpaper.persistence;

import io.github.elihuso.dispenseminingpaper.DispenserMiningPaper;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class DamagePersistenceType implements PersistentDataType<PersistentDataContainer, DamageData> {
    public static final PersistentDataType<PersistentDataContainer, DamageData> INSTANCE = new DamagePersistenceType();
    public static final NamespacedKey DATA_NAME = new NamespacedKey(DispenserMiningPaper.getInstance(), "damage");

    private static final Base64.Encoder BASE_64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE_64_DECODER = Base64.getDecoder();

    private static final NamespacedKey KEY_ORIGINAL = new NamespacedKey(DispenserMiningPaper.getInstance(), "original_stack");
    private static final NamespacedKey KEY_NEW = new NamespacedKey(DispenserMiningPaper.getInstance(), "new_stack");

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<DamageData> getComplexType() {
        return DamageData.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull DamageData damageData,
                                                        @NotNull PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
        container.set(KEY_ORIGINAL, PersistentDataType.STRING, BASE_64_ENCODER.encodeToString(damageData.originalStack.serializeAsBytes()));
        container.set(KEY_NEW, PersistentDataType.STRING, BASE_64_ENCODER.encodeToString(damageData.newStack.serializeAsBytes()));
        return container;
    }

    @Override
    public @NotNull DamageData fromPrimitive(@NotNull PersistentDataContainer container,
                                             @NotNull PersistentDataAdapterContext context) {
        var data = new DamageData();
        data.originalStack = ItemStack.deserializeBytes(BASE_64_DECODER.decode(container.get(KEY_ORIGINAL, PersistentDataType.STRING)));
        data.newStack = ItemStack.deserializeBytes(BASE_64_DECODER.decode(container.get(KEY_NEW, PersistentDataType.STRING)));
        return data;
    }
}
