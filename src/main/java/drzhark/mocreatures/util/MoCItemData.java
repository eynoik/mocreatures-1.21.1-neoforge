package drzhark.mocreatures.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.function.Consumer;

/**
 * Compatibility helpers for the 1.21+ ItemStack data-component model.
 * Legacy Mo' Creatures code used the stack's root CompoundTag directly.
 * We preserve that mod-owned payload under minecraft:custom_data.
 */
public final class MoCItemData {
    private MoCItemData() {
    }

    public static CompoundTag read(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }

    public static void write(ItemStack stack, CompoundTag tag) {
        if (tag == null || tag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

    public static void update(ItemStack stack, Consumer<CompoundTag> mutator) {
        CompoundTag tag = read(stack);
        mutator.accept(tag);
        write(stack, tag);
    }
}
