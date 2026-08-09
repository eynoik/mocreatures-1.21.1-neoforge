package drzhark.mocreatures.init;

import java.util.function.Supplier;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.item.ItemHorseGuide;
import drzhark.mocreatures.item.MoCItemEgg;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@Mod.EventBusSubscriber(modid = MoCConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoCCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoCConstants.MOD_ID);

    public static final Supplier<CreativeModeTab> MOC_TAB = CREATIVE_MODE_TABS.register("mocreatures_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mocreatures_tab"))
                    .icon(() -> MoCItems.AMULET_FAIRY_FULL.get() != null ? 
                           new ItemStack(MoCItems.AMULET_FAIRY_FULL.get()) : 
                           ItemStack.EMPTY)
                    .displayItems((params, output) -> {
                        // Add block items first
                        for (var blockEntry : MoCBlocks.BLOCKS.getEntries()) {
                            // DeferredRegister entries are bound before creative tab contents are built.
                            String blockPath = blockEntry.getId().getPath();
                            MoCBlocks.ITEMS.getEntries().stream()
                                    .filter(itemObj -> itemObj.getId().getPath().equals(blockPath))
                                    .findFirst()
                                    .ifPresent(itemObj -> output.accept(itemObj.get()));
                        }
                        
                        // Add standalone items (not block items)
                        for (var itemEntry : MoCItems.ITEMS.getEntries()) {
                            Item item = itemEntry.get();

                            // Special handling for the horse guide item to add the display stack
                            if (item == MoCItems.HORSE_GUIDE.get()) {
                                output.accept(ItemHorseGuide.createDisplayStack());
                                continue;
                            }

                            // Special handling for the egg item to add all variants
                            if (item instanceof MoCItemEgg eggItem) {
                                eggItem.fillItemCategory(output);
                            } else {
                                output.accept(item);
                            }
                        }
                    })
                    .build()
    );

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
