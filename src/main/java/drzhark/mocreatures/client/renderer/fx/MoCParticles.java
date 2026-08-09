package drzhark.mocreatures.client.renderer.fx;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.client.renderer.fx.data.StarParticleData;
import drzhark.mocreatures.client.renderer.fx.data.VacuumParticleData;
import drzhark.mocreatures.client.renderer.fx.data.VanishParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoCConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoCParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MoCConstants.MOD_ID);

    public static final Supplier<SimpleParticleType> UNDEAD_FX =
            PARTICLES.register("undead_fx", () -> new SimpleParticleType(false));

    public static final Supplier<ParticleType<VanishParticleData>> VANISH_FX =
            PARTICLES.register("vanish_fx", () -> new ParticleType<VanishParticleData>(false, VanishParticleData.DESERIALIZER) {
                @Override
                public Codec<VanishParticleData> codec() {
                    return Codec.unit(new VanishParticleData(1.0F, 1.0F, 1.0F, false));
                }
            });

    public static final Supplier<ParticleType<StarParticleData>> STAR_FX =
            PARTICLES.register("star_fx", () -> new ParticleType<StarParticleData>(false, StarParticleData.DESERIALIZER) {
                @Override
                public Codec<StarParticleData> codec() {
                    return Codec.unit(new StarParticleData(1.0F, 1.0F, 1.0F)); // default white
                }
            });

    public static final Supplier<ParticleType<VacuumParticleData>> VACUUM_FX =
            PARTICLES.register("vacuum_fx", () -> new ParticleType<VacuumParticleData>(false, VacuumParticleData.DESERIALIZER) {
                @Override
                public Codec<VacuumParticleData> codec() {
                    return Codec.unit(new VacuumParticleData(1.0F, 1.0F, 1.0F));
                }
            });
}