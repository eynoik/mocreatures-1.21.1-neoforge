package drzhark.mocreatures.init;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.block.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = MoCConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoCBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoCConstants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoCConstants.MOD_ID);

    public static final Supplier<Block> ancientSilverBlock = register("ancient_silver_block", () ->
            new MoCBlockMetal(BlockBehaviour.Properties.of().strength(3.0F, 10.0F).mapColor(MapColor.METAL)));

    public static final Supplier<Block> cobbledWyvstone = register("cobbled_wyvstone", () ->
            new MoCBlockRock(BlockBehaviour.Properties.of().strength(2.0F, 10.0F).mapColor(MapColor.STONE)));

    public static final Supplier<Block> cobbledDeepWyvstone = register("cobbled_deep_wyvstone", () ->
            new MoCBlockRock(BlockBehaviour.Properties.of().strength(3.5F, 10.0F).mapColor(MapColor.STONE)));

    public static final Supplier<Block> wyvstone = register("wyvstone", () ->
            new MoCBlockRock(BlockBehaviour.Properties.of().strength(1.5F, 10.0F).mapColor(MapColor.STONE)));

    public static final Supplier<Block> deepWyvstone = register("deep_wyvstone", () ->
            new MoCBlockRock(BlockBehaviour.Properties.of().strength(3.0F, 10.0F).mapColor(MapColor.STONE)));

    public static final Supplier<Block> mossyCobbledWyvstone = register("mossy_cobbled_wyvstone", () ->
            new MoCBlockRock(BlockBehaviour.Properties.of().strength(1.5F, 10.0F).mapColor(MapColor.STONE)));

    public static final Supplier<Block> mossyCobbledDeepWyvstone = register("mossy_cobbled_deep_wyvstone", () ->
            new MoCBlockRock(BlockBehaviour.Properties.of().strength(1.5F, 10.0F).mapColor(MapColor.STONE)));

    public static final Supplier<Block> gleamingGlass = register("gleaming_glass", () ->
            new MoCBlockGlass(BlockBehaviour.Properties.of().strength(0.4F).mapColor(MapColor.COLOR_LIGHT_GRAY).noOcclusion()));

    public static final Supplier<Block> silverSand = register("silver_sand", () ->
            new MoCBlockSand(BlockBehaviour.Properties.of().strength(0.6F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final Supplier<Block> silverSandstone = register("silver_sandstone", () ->
            new MoCBlockRock(BlockBehaviour.Properties.of().strength(1.2F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final Supplier<Block> carvedSilverSandstone = register("carved_silver_sandstone", () ->
            new MoCBlockRock(BlockBehaviour.Properties.of().strength(1.2F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final Supplier<Block> smoothSilverSandstone = register("smooth_silver_sandstone", () ->
            new MoCBlockRock(BlockBehaviour.Properties.of().strength(1.2F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final Supplier<Block> ancientOre = register("ancient_ore", () ->
            new MoCBlockOre(BlockBehaviour.Properties.of().strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final Supplier<Block> firestone = register("firestone", () ->
            new MoCBlockFirestone(BlockBehaviour.Properties.of().strength(3.0F).lightLevel(state -> 7).mapColor(MapColor.COLOR_ORANGE)));

    public static final Supplier<Block> wyvernDiamondOre = register("wyvern_diamond_ore", () ->
            new MoCBlockOre(BlockBehaviour.Properties.of().strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final Supplier<Block> wyvernEmeraldOre = register("wyvern_emerald_ore", () ->
            new MoCBlockOre(BlockBehaviour.Properties.of().strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final Supplier<Block> wyvernGoldOre = register("wyvern_gold_ore", () ->
            new MoCBlockOre(BlockBehaviour.Properties.of().strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final Supplier<Block> wyvernIronOre = register("wyvern_iron_ore", () ->
            new MoCBlockOre(BlockBehaviour.Properties.of().strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final Supplier<Block> wyvernLapisOre = register("wyvern_lapis_ore", () ->
            new MoCBlockOre(BlockBehaviour.Properties.of().strength(1.5F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final Supplier<Block> wyvgrass = register("wyvgrass", () ->
            new MoCBlockGrass(BlockBehaviour.Properties.of().strength(0.7F).mapColor(MapColor.COLOR_CYAN)));

    public static final Supplier<Block> wyvdirt = register("wyvdirt", () ->
            new MoCBlockDirt(BlockBehaviour.Properties.of().strength(0.6F).mapColor(MapColor.DIRT)));

    public static final Supplier<Block> wyvwoodLeaves = register("wyvwood_leaves", () ->
            new MoCBlockLeaf(BlockBehaviour.Properties.of().strength(0.2F).mapColor(MapColor.COLOR_LIGHT_BLUE).noOcclusion()));

    public static final Supplier<Block> wyvwoodSapling = register("wyvwood_sapling", () ->
            new MoCBlockSapling(BlockBehaviour.Properties.of().noCollission().randomTicks().strength(0.0F).mapColor(MapColor.PLANT)));

    public static final Supplier<Block> wyvwoodLog = register("wyvwood_log", () ->
            new MoCBlockLog(BlockBehaviour.Properties.of().strength(2.0F).mapColor(MapColor.COLOR_CYAN)));

    public static final Supplier<Block> tallWyvgrass = register("tall_wyvgrass", () ->
            new MoCBlockTallGrass(BlockBehaviour.Properties.of().noCollission().strength(0.0F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final Supplier<Block> wyvwoodPlanks = register("wyvwood_planks", () ->
            new MoCBlockPlanks(BlockBehaviour.Properties.of().strength(2.0F, 5.0F).mapColor(MapColor.COLOR_BLUE)));

    public static final Supplier<Block> wyvernNestBlock = register("wyvern_nest_block", () ->
            new MoCBlockNest(BlockBehaviour.Properties.of().strength(0.5F).mapColor(MapColor.COLOR_YELLOW)));


    private static Supplier<Block> register(String name, Supplier<Block> blockSupplier) {
        Supplier<Block> block = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> {
            return new BlockItem(block.get(), new Item.Properties());
        });
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MoCConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerRenderLayers(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemBlockRenderTypes.setRenderLayer(wyvwoodLeaves.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(wyvwoodSapling.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(tallWyvgrass.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(gleamingGlass.get(), RenderType.translucent());
            });
        }
    }
}
