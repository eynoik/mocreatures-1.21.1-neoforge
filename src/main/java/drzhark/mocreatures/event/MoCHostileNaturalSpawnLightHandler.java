/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.event;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

/**
 * Adds only the vanilla darkness requirement to natural Mo' Creatures hostile
 * spawns, without replacing the mod's existing spawn-placement predicate.
 *
 * This keeps all existing biome/config/ground checks intact and avoids the
 * over-restrictive double predicate introduced by the v1.0.8 hotfix.
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public final class MoCHostileNaturalSpawnLightHandler {

    private MoCHostileNaturalSpawnLightHandler() {
    }

    @SubscribeEvent
    public static void onSpawnPlacementCheck(MobSpawnEvent.SpawnPlacementCheck event) {
        // Only biome-driven natural spawning is relevant here. Commands, spawn
        // eggs, spawners and other explicit spawn mechanisms must remain untouched.
        if (event.getSpawnType() != MobSpawnType.NATURAL) {
            return;
        }

        if (!event.getDefaultResult()) {
            return;
        }

        if (event.getEntityType().getCategory() != MobCategory.MONSTER) {
            return;
        }

        ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntityType());
        if (!MoCConstants.MOD_ID.equals(entityId.getNamespace())) {
            return;
        }

        // Apply exactly the vanilla hostile-mob darkness/environment test and
        // nothing else. Existing Mo' Creatures spawn rules already passed above.
        if (!Monster.isDarkEnoughToSpawn(event.getLevel(), event.getPos(), event.getRandom())) {
            event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
        }
    }
}
