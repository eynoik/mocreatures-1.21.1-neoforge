/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class WyvwoodTreeGrower {
    private static final ResourceKey<ConfiguredFeature<?, ?>> WYVWOOD_DARK_OAK =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath("mocreatures", "wyvwood_dark_oak"));

    public static final TreeGrower GROWER = new TreeGrower(
            "mocreatures:wyvwood",
            Optional.empty(),
            Optional.of(WYVWOOD_DARK_OAK),
            Optional.empty()
    );

    private WyvwoodTreeGrower() {}
}
