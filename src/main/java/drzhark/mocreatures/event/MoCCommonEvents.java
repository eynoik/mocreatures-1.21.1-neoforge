package drzhark.mocreatures.event;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.config.biome.BiomeSpawnConfig;
import drzhark.mocreatures.world.MoCSpawnRegistryCache;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = MoCConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoCCommonEvents {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BiomeSpawnConfig.init(); // Ensure all config loaded BEFORE any biome modifiers
            MoCSpawnRegistryCache.prepare(); // Build entity cache
        });
    }
}
