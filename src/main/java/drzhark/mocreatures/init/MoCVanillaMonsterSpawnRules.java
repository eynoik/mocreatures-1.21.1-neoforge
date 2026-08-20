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
 * Restores the vanilla darkness requirement used by the classic/Nostalgia
 * hostile Mo' Creatures spawn rules.
 *
 * Aura Edition already has biome/config spawn predicates for these entities.
 * This handler combines vanilla Monster.checkMonsterSpawnRules with those
 * predicates using AND, so a natural spawn must satisfy both the Mo' Creatures
 * rules and normal hostile-mob darkness rules.
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class MoCVanillaMonsterSpawnRules {

    private MoCVanillaMonsterSpawnRules() {
    }

    @SubscribeEvent
    public static void registerVanillaNightRules(RegisterSpawnPlacementsEvent event) {
        // Ogres
        addVanillaNightRule(event, MoCEntities.CAVE_OGRE.get());
        addVanillaNightRule(event, MoCEntities.FIRE_OGRE.get());
        addVanillaNightRule(event, MoCEntities.GREEN_OGRE.get());

        // Golems
        addVanillaNightRule(event, MoCEntities.BIG_GOLEM.get());
        addVanillaNightRule(event, MoCEntities.MINI_GOLEM.get());

        // Undead/night horse
        addVanillaNightRule(event, MoCEntities.HORSE_MOB.get());

        // Rats
        addVanillaNightRule(event, MoCEntities.HELL_RAT.get());
        addVanillaNightRule(event, MoCEntities.RAT.get());

        // Manticores
        addVanillaNightRule(event, MoCEntities.DARK_MANTICORE.get());
        addVanillaNightRule(event, MoCEntities.FIRE_MANTICORE.get());
        addVanillaNightRule(event, MoCEntities.FROST_MANTICORE.get());
        addVanillaNightRule(event, MoCEntities.PLAIN_MANTICORE.get());
        addVanillaNightRule(event, MoCEntities.TOXIC_MANTICORE.get());

        // Scorpions
        addVanillaNightRule(event, MoCEntities.CAVE_SCORPION.get());
        addVanillaNightRule(event, MoCEntities.DIRT_SCORPION.get());
        addVanillaNightRule(event, MoCEntities.FIRE_SCORPION.get());
        addVanillaNightRule(event, MoCEntities.FROST_SCORPION.get());
        addVanillaNightRule(event, MoCEntities.UNDEAD_SCORPION.get());

        // Other classic hostile mobs
        addVanillaNightRule(event, MoCEntities.SILVER_SKELETON.get());
        addVanillaNightRule(event, MoCEntities.FLAME_WRAITH.get());
        addVanillaNightRule(event, MoCEntities.WRAITH.get());
        addVanillaNightRule(event, MoCEntities.WEREWOLF.get());
        addVanillaNightRule(event, MoCEntities.WWOLF.get());
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
        // Spawn eggs are manual spawns and must not be blocked by light level.
        if (spawnType == MobSpawnType.SPAWN_EGG) {
            return true;
        }

        return Monster.checkMonsterSpawnRules(entityType, world, spawnType, pos, random);
    }
}
