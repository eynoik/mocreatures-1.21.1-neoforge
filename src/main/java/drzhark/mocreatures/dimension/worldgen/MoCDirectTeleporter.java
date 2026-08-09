package drzhark.mocreatures.dimension.worldgen;

import drzhark.mocreatures.registry.MoCPOI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

/**
 * 1.21.1 replacement for the old Forge ITeleporter implementation.
 * Prepares the Mo' Creatures portal landing area and performs the vanilla
 * DimensionTransition used by Minecraft 1.21.x.
 */
public final class MoCDirectTeleporter {
    private MoCDirectTeleporter() {}

    public static ServerPlayer teleport(ServerPlayer player, ServerLevel destination, BlockPos pos, boolean generateStructure) {
        if (generateStructure) {
            prepareDestination(destination, pos);
        } else {
            ensureSafeLanding(destination, pos);
        }

        Vec3 target = new Vec3(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
        DimensionTransition transition = new DimensionTransition(
                destination,
                target,
                Vec3.ZERO,
                player.getYRot(),
                player.getXRot(),
                false,
                DimensionTransition.DO_NOTHING
        );
        return (ServerPlayer) player.changeDimension(transition);
    }

    private static void prepareDestination(ServerLevel world, BlockPos pos) {
        BlockState block = world.getBlockState(pos);
        if (!block.is(Blocks.QUARTZ_BLOCK)) {
            MoCWorldGenPortal portalGen = new MoCWorldGenPortal(
                    Blocks.QUARTZ_PILLAR.defaultBlockState(),
                    Blocks.QUARTZ_STAIRS.defaultBlockState(),
                    Blocks.QUARTZ_BLOCK.defaultBlockState(),
                    Blocks.QUARTZ_BLOCK.defaultBlockState()
            );
            portalGen.generate(world, world.getRandom(), pos);
            registerPortalPOI(world, pos);
        }
        ensureSafeLanding(world, pos);
    }

    private static void ensureSafeLanding(ServerLevel world, BlockPos pos) {
        for (int y = 1; y <= 5; y++) {
            if (!world.getBlockState(pos.below(y)).isAir()) {
                return;
            }
        }

        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                world.setBlock(pos.offset(dx, -1, dz), Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
            }
        }
    }

    private static void registerPortalPOI(ServerLevel world, BlockPos pos) {
        PoiManager poiManager = world.getPoiManager();
        poiManager.ensureLoadedAndValid(world, pos, 8);
        world.registryAccess()
                .registryOrThrow(Registries.POINT_OF_INTEREST_TYPE)
                .getHolder(MoCPOI.WYVERN_PORTAL_KEY)
                .ifPresent(holder -> poiManager.add(pos, holder));
    }
}
