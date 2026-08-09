package drzhark.mocreatures.item;

import drzhark.mocreatures.init.MoCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.resources.ResourceLocation;

public class MoCItemCrabClaw extends MoCItem {


    public final int armor;
    public final int enchantability;
    public final float reach;
    public final float toughness;

    public MoCItemCrabClaw(Item.Properties properties, int enchantability, float toughness, int armor, float reach) {
        super(properties.attributes(createAttributes(toughness, armor, reach)));
        this.armor = armor;
        this.enchantability = enchantability;
        this.reach = reach;
        this.toughness = toughness;
    }

    private static ItemAttributeModifiers createAttributes(float toughness, int armor, float reach) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(
                Attributes.ARMOR,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath("mocreatures", "crab_claw_armor"), armor, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.OFFHAND);
        builder.add(
                Attributes.ENTITY_INTERACTION_RANGE,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath("mocreatures", "crab_claw_reach"), reach, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.OFFHAND);
        builder.add(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath("mocreatures", "crab_claw_toughness"), toughness, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.OFFHAND);
        return builder.build();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Item offhand = attacker.getOffhandItem().getItem();
        if (offhand instanceof MoCItemCrabClaw) {
            if (attacker.level() instanceof ServerLevel serverLevel) {
                stack.hurtAndBreak(1, serverLevel, attacker, brokenItem -> {});
            }
        }
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F && entity.getOffhandItem().getItem() instanceof MoCItemCrabClaw) {
            if (level instanceof ServerLevel serverLevel) {
                stack.hurtAndBreak(1, serverLevel, entity, brokenItem -> {});
            }
        }
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == MoCItems.ANIMALHIDE.get() || super.isValidRepairItem(toRepair, repair);
    }

}

