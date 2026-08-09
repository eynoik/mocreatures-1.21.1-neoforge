package drzhark.mocreatures.client.renderer.fx.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import drzhark.mocreatures.client.renderer.fx.MoCParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class VanishParticleData implements ParticleOptions {
    public static final MapCodec<VanishParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("red").forGetter(v -> v.red),
            Codec.FLOAT.fieldOf("green").forGetter(v -> v.green),
            Codec.FLOAT.fieldOf("blue").forGetter(v -> v.blue), Codec.BOOL.fieldOf("implode").forGetter(v -> v.implode)
    ).apply(instance, VanishParticleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, VanishParticleData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public VanishParticleData decode(RegistryFriendlyByteBuf buf) {
            return new VanishParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, VanishParticleData value) {
            buf.writeFloat(value.red);
            buf.writeFloat(value.green);
            buf.writeFloat(value.blue);
            buf.writeBoolean(value.implode);
        }
    };

    public final float red, green, blue;
    public final boolean implode;

    public VanishParticleData(float red, float green, float blue, boolean implode) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.implode = implode;
    }

    @Override
    public ParticleType<VanishParticleData> getType() {
        return MoCParticles.VANISH_FX.get();
    }
}
