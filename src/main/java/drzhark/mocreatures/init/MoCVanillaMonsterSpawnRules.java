/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

/**
 * Restores vanilla hostile-mob darkness rules for Mo' Creatures mobs that are
 * intended to behave like ordinary night monsters.
 *
 * The legacy port already registers its own biome/config predicate. We add the
 * vanilla monster predicate with AND so both sets of restrictions must pass.
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class MoCVanillaMonsterSpawnRules {

    private MoCVanillaMonsterSpawnRules() {
    }

    @SubscribeEvent
    public static void registerVanillaNightRules(RegisterSpawnPlacementsEvent event) {
        addVanillaNightRule(event, MoCEntities.RAT.get());
        addVanillaNightRule(event, MoCEntities.MINI_GOLEM.get());

        addVanillaNightRule(event, MoCEntities.OGRE.get());
        addVanillaNightRule(event, MoCEntities.GREEN_OGRE.get());
        addVanillaNightRule(event, MoCEntities.CAVE_OGRE.get());
        addVanillaNightRule(event, MoCEntities.FIRE_OGRE.get());
    }

    private static <T extends Monster> void addVanillaNightRule(
            RegisterSpawnPlacementsEvent event,
            EntityType<T> entityType) {
        event.register(
                entityType,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCVanillaMonsterSpawnRules::checkVanillaNightMonsterSpawn,
                RegisterSpawnPlacementsEvent.Operation.AND);
    }

    private static <T extends Monster> boolean checkVanillaNightMonsterSpawn(
            EntityType<T> entityType,
            ServerLevelAccessor world,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random) {
        // Manual spawn eggs must never be blocked by natural-spawn light rules.
        if (spawnType == MobSpawnType.SPAWN_EGG) {
            return true;
        }

        return Monster.checkMonsterSpawnRules(entityType, world, spawnType, pos, random);
    }
}
