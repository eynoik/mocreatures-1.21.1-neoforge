package drzhark.mocreatures.world;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MoCSpawnBiomeModifier implements BiomeModifier {
    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MoCConstants.MOD_ID);

    public static final Supplier<MapCodec<MoCSpawnBiomeModifier>> SERIALIZER =
            SERIALIZERS.register("moc_spawns", () -> MapCodec.unit(new MoCSpawnBiomeModifier()));

    public MoCSpawnBiomeModifier() {
    }

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            String biomeName = biome.unwrap().map(key -> key.location().toString(), obj -> "unknown");
            MoCreatures.LOGGER.debug("MoCSpawnBiomeModifier: Processing biome {} in phase {}", biomeName, phase);
            MoCWorldRegistry.addBiomeSpawns(biome, builder);
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }
}