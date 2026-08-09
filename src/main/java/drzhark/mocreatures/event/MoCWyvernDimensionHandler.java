package drzhark.mocreatures.event;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Event handler specifically for managing the Wyvern dimension mob spawning
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCWyvernDimensionHandler {

    // Use suppliers to delay initialization until the entities are actually registered
    private static final Supplier<List<EntityType<?>>> ALLOWED_ENTITIES_SUPPLIER = () -> Arrays.asList(
        MoCEntities.WYVERN.get(),
        MoCEntities.BUNNY.get(),
        MoCEntities.SNAKE.get(),
        MoCEntities.FILCH_LIZARD.get(),
        MoCEntities.DRAGONFLY.get(),
        MoCEntities.FIREFLY.get(),
        MoCEntities.GRASSHOPPER.get()
    );
    
    // Lazy initialization - will only be called when needed
    private static List<EntityType<?>> getAllowedEntities() {
        return ALLOWED_ENTITIES_SUPPLIER.get();
    }

    /**
     * Handle mob spawning - use lower priority to allow other systems to work first
     */

    // @SubscribeEvent
    // public static void onServerAboutToStart(ServerAboutToStartEvent event) {
    //     MoCreatures.LOGGER.info("Adding wyvern island features to biomes directly");
        
    //     var biomeRegistry = event.getServer().registryAccess().registryOrThrow(Registries.BIOME);
        
    //     ResourceKey<?>[] biomeKeys = {
    //         ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvernlairlands")),
    //         ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvernlairlandsforest")),
    //         ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvernlair_mountains")),
    //         ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvernlair_desertlands"))
    //     };
        
    //     for (ResourceKey<?> biomeKey : biomeKeys) {
    //         MoCreatures.LOGGER.info("Attempting to add wyvern islands to biome: {}", biomeKey.location());
            
    //         if (biomeRegistry.containsKey(ResourceKey.create(Registries.BIOME, biomeKey.location()))) {
    //             MoCreatures.LOGGER.info("Found biome {} in registry", biomeKey.location());
    //         } else {
    //             MoCreatures.LOGGER.error("Could not find biome {} in registry", biomeKey.location());
    //         }
    //     }
        
    //     MoCreatures.LOGGER.info("Wyvern island feature registration complete");
        
    //     // Log allowed entities for debugging
    //     MoCreatures.LOGGER.info("Allowed entities in Wyvern dimension:");
    //     for (EntityType<?> entityType : getAllowedEntities()) {
    //         MoCreatures.LOGGER.info("  - {}", EntityType.getKey(entityType));
    //     }
    // }
} 