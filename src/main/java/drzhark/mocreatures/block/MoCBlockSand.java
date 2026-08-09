/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;

import com.mojang.serialization.MapCodec;

import drzhark.mocreatures.init.MoCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class MoCBlockSand extends FallingBlock {

    public static final MapCodec<MoCBlockSand> CODEC = simpleCodec(MoCBlockSand::new);

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    public MoCBlockSand(BlockBehaviour.Properties properties) {
        super(properties
                .strength(0.5F)
                .sound(SoundType.SAND));
    }

    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState state, BlockGetter world, BlockPos pos) {
        return 12107978;
    }

}
