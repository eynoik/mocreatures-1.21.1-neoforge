/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.config;

import drzhark.mocreatures.MoCreatures;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;

/**
 * Global combat scaling for Mo' Creatures entities.
 * Kept in a separate file so gameplay balancing does not get buried in the legacy settings config.
 */
public final class MoCCombatScalingConfig {

    private static final String CATEGORY_COMBAT_SCALING = "combat-scaling";
    private static final double MIN_MULTIPLIER = 0.1D;
    private static final double MAX_MULTIPLIER = 100.0D;

    public static double healthMultiplier = 1.0D;
    public static double damageMultiplier = 1.0D;

    private MoCCombatScalingConfig() {
    }

    public static void init() {
        MoCConfiguration config = new MoCConfiguration(new File(
                FMLPaths.CONFIGDIR.get().toString(),
                "MoCreatures" + File.separator + "MoCCombatScaling.cfg"
        ));
        config.load();

        healthMultiplier = clamp(config.get(
                CATEGORY_COMBAT_SCALING,
                "HealthMultiplier",
                1.0D,
                "Global Mo' Creatures maximum-health multiplier. 1.0 = normal, 2.0 = double health, 0.5 = half health."
        ).getDouble(1.0D));

        damageMultiplier = clamp(config.get(
                CATEGORY_COMBAT_SCALING,
                "DamageMultiplier",
                1.0D,
                "Global damage multiplier for attacks caused by Mo' Creatures entities. 1.0 = normal, 2.0 = double damage, 0.5 = half damage."
        ).getDouble(1.0D));

        config.save();
        MoCreatures.LOGGER.info("Mo' Creatures combat scaling: health x{}, damage x{}", healthMultiplier, damageMultiplier);
    }

    private static double clamp(double value) {
        if (!Double.isFinite(value)) {
            return 1.0D;
        }
        return Math.max(MIN_MULTIPLIER, Math.min(MAX_MULTIPLIER, value));
    }
}
