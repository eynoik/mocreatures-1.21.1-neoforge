package drzhark.mocreatures.client.renderer.fx.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import drzhark.mocreatures.client.renderer.fx.MoCParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class StarParticleData implements ParticleOptions {
    public static final MapCodec<StarParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("red").forGetter(v -> v.red),
            Codec.FLOAT.fieldOf("green").forGetter(v -> v.green),
            Codec.FLOAT.fieldOf("blue").forGetter(v -> v.blue)
    ).apply(instance, StarParticleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StarParticleData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public StarParticleData decode(RegistryFriendlyByteBuf buf) {
            return new StarParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, StarParticleData value) {
            buf.writeFloat(value.red);
            buf.writeFloat(value.green);
            buf.writeFloat(value.blue);
        }
    };

    public final float red, green, blue;

    public StarParticleData(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public ParticleType<StarParticleData> getType() {
        return MoCParticles.STAR_FX.get();
    }
}
