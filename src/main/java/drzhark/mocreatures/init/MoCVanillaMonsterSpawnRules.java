/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.entity.MoCEntityMob;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

/**
 * Restores the vanilla darkness requirement used by the classic/Nostalgia
 * hostile Mo' Creatures spawn rules.
 *
 * Aura Edition already registers its own REPLACE spawn predicates for these
 * entities. NeoForge ignores AND/OR predicates whenever a replacement predicate
 * exists, so this handler runs at LOWEST priority and installs the final
 * replacement predicate. The final predicate requires BOTH the existing
 * Mo' Creatures spawn rules and vanilla Monster.checkMonsterSpawnRules.
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class MoCVanillaMonsterSpawnRules {

    private MoCVanillaMonsterSpawnRules() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerVanillaNightRules(RegisterSpawnPlacementsEvent event) {
        // Ogres
        replaceWithVanillaNightRule(event, MoCEntities.CAVE_OGRE.get());
        replaceWithVanillaNightRule(event, MoCEntities.FIRE_OGRE.get());
        replaceWithVanillaNightRule(event, MoCEntities.GREEN_OGRE.get());

        // Golems
        replaceWithVanillaNightRule(event, MoCEntities.BIG_GOLEM.get());
        replaceWithVanillaNightRule(event, MoCEntities.MINI_GOLEM.get());

        // Undead/night horse
        replaceWithVanillaNightRule(event, MoCEntities.HORSE_MOB.get());

        // Rats
        replaceWithVanillaNightRule(event, MoCEntities.HELL_RAT.get());
        replaceWithVanillaNightRule(event, MoCEntities.RAT.get());

        // Manticores
        replaceWithVanillaNightRule(event, MoCEntities.DARK_MANTICORE.get());
        replaceWithVanillaNightRule(event, MoCEntities.FIRE_MANTICORE.get());
        replaceWithVanillaNightRule(event, MoCEntities.FROST_MANTICORE.get());
        replaceWithVanillaNightRule(event, MoCEntities.PLAIN_MANTICORE.get());
        replaceWithVanillaNightRule(event, MoCEntities.TOXIC_MANTICORE.get());

        // Scorpions
        replaceWithVanillaNightRule(event, MoCEntities.CAVE_SCORPION.get());
        replaceWithVanillaNightRule(event, MoCEntities.DIRT_SCORPION.get());
        replaceWithVanillaNightRule(event, MoCEntities.FIRE_SCORPION.get());
        replaceWithVanillaNightRule(event, MoCEntities.FROST_SCORPION.get());
        replaceWithVanillaNightRule(event, MoCEntities.UNDEAD_SCORPION.get());

        // Other classic hostile mobs
        replaceWithVanillaNightRule(event, MoCEntities.SILVER_SKELETON.get());
        replaceWithVanillaNightRule(event, MoCEntities.FLAME_WRAITH.get());
        replaceWithVanillaNightRule(event, MoCEntities.WRAITH.get());
        replaceWithVanillaNightRule(event, MoCEntities.WEREWOLF.get());
        replaceWithVanillaNightRule(event, MoCEntities.WWOLF.get());
    }

    private static <T extends Monster> void replaceWithVanillaNightRule(
            RegisterSpawnPlacementsEvent event,
            EntityType<T> entityType) {
        event.register(
                entityType,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCVanillaMonsterSpawnRules::checkMoCRulesAndVanillaDarkness,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private static <T extends Monster> boolean checkMoCRulesAndVanillaDarkness(
            EntityType<T> entityType,
            ServerLevelAccessor world,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random) {
        // Spawn eggs are manual spawns and must not be blocked by light level.
        if (spawnType == MobSpawnType.SPAWN_EGG) {
            return true;
        }

        return MoCEntityMob.checkMobSpawnRules(entityType, world, spawnType, pos, random)
                && Monster.checkMonsterSpawnRules(entityType, world, spawnType, pos, random);
    }
}
