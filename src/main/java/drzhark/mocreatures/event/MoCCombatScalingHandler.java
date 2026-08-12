/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.event;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.config.MoCCombatScalingConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * Applies the global combat multipliers to Mo' Creatures entities.
 */
public final class MoCCombatScalingHandler {

    private static final ResourceLocation HEALTH_MULTIPLIER_ID =
            ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "combat_health_multiplier");

    private MoCCombatScalingHandler() {
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity living) || !isMoCreaturesEntity(living)) {
            return;
        }

        AttributeInstance maxHealth = living.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        float oldMaxHealth = Math.max(0.0001F, living.getMaxHealth());
        float healthFraction = Math.max(0.0F, Math.min(1.0F, living.getHealth() / oldMaxHealth));

        maxHealth.addOrReplacePermanentModifier(new AttributeModifier(
                HEALTH_MULTIPLIER_ID,
                MoCCombatScalingConfig.healthMultiplier - 1.0D,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));

        float newMaxHealth = living.getMaxHealth();
        living.setHealth(Math.max(0.0F, Math.min(newMaxHealth, newMaxHealth * healthFraction)));
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!isMoCreaturesEntity(attacker)) {
            return;
        }

        event.setAmount((float) (event.getAmount() * MoCCombatScalingConfig.damageMultiplier));
    }

    private static boolean isMoCreaturesEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return id != null && MoCConstants.MOD_ID.equals(id.getNamespace());
    }
}
