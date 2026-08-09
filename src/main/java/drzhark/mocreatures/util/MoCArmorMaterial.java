package drzhark.mocreatures.util;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.init.MoCItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class MoCArmorMaterial {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, MoCConstants.MOD_ID);

    public static final Holder<ArmorMaterial> CROC = register("croc", stats(1, 3, 4, 1), 17,
            SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(MoCItems.REPTILE_HIDE.get()), 1.0F, 0.0F);
    public static final Holder<ArmorMaterial> FUR = register("fur", stats(1, 2, 2, 1), 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(MoCItems.FUR.get()), 0.0F, 0.0F);
    public static final Holder<ArmorMaterial> HIDE = register("hide", stats(1, 3, 3, 1), 18,
            SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(MoCItems.ANIMALHIDE.get()), 0.0F, 0.0F);
    public static final Holder<ArmorMaterial> SCORPD = register("scorpd", stats(2, 6, 7, 2), 16,
            SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(MoCItems.CHITIN.get()), 2.0F, 0.0F);
    public static final Holder<ArmorMaterial> SCORPF = register("scorpf", stats(2, 6, 7, 2), 16,
            SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(MoCItems.CHITINFROST.get()), 2.0F, 0.0F);
    public static final Holder<ArmorMaterial> SCORPN = register("scorpn", stats(2, 6, 7, 2), 16,
            SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(MoCItems.CHITINNETHER.get()), 2.0F, 0.0F);
    public static final Holder<ArmorMaterial> SCORPC = register("scorpc", stats(2, 6, 7, 2), 16,
            SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(MoCItems.CHITINCAVE.get()), 2.0F, 0.0F);
    public static final Holder<ArmorMaterial> SCORPU = register("scorpu", stats(2, 6, 7, 2), 16,
            SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(MoCItems.CHITINUNDEAD.get()), 2.0F, 0.0F);
    public static final Holder<ArmorMaterial> SILVER = register("silver", stats(2, 6, 5, 2), 22,
            SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(MoCItems.ANCIENTSILVERINGOT.get()), 1.5F, 0.0F);

    private MoCArmorMaterial() {}

    private static Holder<ArmorMaterial> register(String name, Map<ArmorItem.Type, Integer> defense,
                                                   int enchantmentValue, Holder<SoundEvent> equipSound,
                                                   Supplier<Ingredient> repairIngredient,
                                                   float toughness, float knockbackResistance) {
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(
                defense,
                enchantmentValue,
                equipSound,
                repairIngredient,
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, name))),
                toughness,
                knockbackResistance
        ));
    }

    private static Map<ArmorItem.Type, Integer> stats(int boots, int leggings, int chestplate, int helmet) {
        return Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, boots);
            map.put(ArmorItem.Type.LEGGINGS, leggings);
            map.put(ArmorItem.Type.CHESTPLATE, chestplate);
            map.put(ArmorItem.Type.HELMET, helmet);
            map.put(ArmorItem.Type.BODY, chestplate);
        });
    }
}
