/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;

import net.minecraft.core.registries.Registries;
import java.util.function.Supplier;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.worldgen.feature.FirestoneClusterFeature;
import drzhark.mocreatures.worldgen.feature.WyvernNestFeature;
import drzhark.mocreatures.worldgen.structure.WyvernIslandFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = MoCConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MoCFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MoCConstants.MOD_ID);

    public static final Supplier<Feature<NoneFeatureConfiguration>> FIRESTONE_CLUSTER = 
            FEATURES.register("firestone_cluster", 
                    () -> new FirestoneClusterFeature(NoneFeatureConfiguration.CODEC));
    
    public static final Supplier<Feature<NoneFeatureConfiguration>> WYVERN_ISLAND = 
            FEATURES.register("wyvern_island", 
                    () -> new WyvernIslandFeature(NoneFeatureConfiguration.CODEC));
    
    public static final Supplier<Feature<NoneFeatureConfiguration>> WYVERN_NEST = 
            FEATURES.register("wyvern_nest", 
                    () -> new WyvernNestFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        MoCreatures.LOGGER.info("Registering MoCreatures features");
        FEATURES.register(eventBus);
    }
    
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            MoCreatures.LOGGER.info("MoCreatures features initialized");
            MoCreatures.LOGGER.info("Registered features: " + FEATURES.getEntries().size());
        });
    }
}