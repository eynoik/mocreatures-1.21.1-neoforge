package drzhark.mocreatures.client.renderer.fx.data;

// Note: In 1.20.1, texture stitching is typically handled via atlas JSON files
// This class may need to be replaced with proper atlas configuration
// For now, commenting out to prevent compilation errors

/*
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.TextureStitchEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mocreatures", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MoCTextureStitcher {
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_PARTICLES)) {
            event.addSprite(ResourceLocation.fromNamespaceAndPath("mocreatures", "particle/fx_undead1"));
            event.addSprite(ResourceLocation.fromNamespaceAndPath("mocreatures", "particle/fx_undead2"));
        }
    }
}
*/

public class MoCTextureStitcher {
    // TODO: Implement proper 1.20.1 texture atlas configuration via JSON files
    // See: https://www.minecraft.net/da-dk/article/minecraft-java-edition-1-19-3
}
