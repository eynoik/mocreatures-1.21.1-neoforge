package drzhark.mocreatures.item;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;

public class MoCItemRecord extends Item {
    @SuppressWarnings("removal")
    public static final ResourceLocation RECORD_SHUFFLE_RESOURCE = ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "shuffling");

    public MoCItemRecord(int comparatorValueIn, java.util.function.Supplier<SoundEvent> soundSupplier, Item.Properties properties) {
        super(properties);
    }
}
