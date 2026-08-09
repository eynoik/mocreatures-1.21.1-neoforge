package drzhark.mocreatures.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MoCTags {
    public static class Items {
        @SuppressWarnings("removal")
        public static final TagKey<Item> COOKED_FISHES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "cooked_fishes"));
        @SuppressWarnings("removal")
        public static final TagKey<Item> RAW_FISHES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "raw_fishes"));
    }
}
