from pathlib import Path
import re

ROOT = Path('src/main/java')


def read(rel):
    return (ROOT / rel).read_text(encoding='utf-8')


def write(rel, text):
    (ROOT / rel).write_text(text, encoding='utf-8')


# -----------------------------------------------------------------------------
# Legacy missing-mapping hooks
# MissingMappingsEvent / ForgeRegistries from the old Forge API no longer exist
# at these package names. Keep the useful legacy NBT conversion, but drop the
# obsolete registry-event layer for this compile pass.
# -----------------------------------------------------------------------------
write('drzhark/mocreatures/compat/datafixes/BlockIDFixer.java', '''/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.compat.datafixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

/**
 * Legacy block-id fixer placeholder.
 *
 * Old Forge MissingMappingsEvent based remapping was removed from the 1.21.1
 * NeoForge compile path. The historical mapping table can be restored using
 * the modern registry migration facilities after the main port compiles.
 */
public class BlockIDFixer extends DataFix {
    public BlockIDFixer(Schema outputSchema, boolean changesType) {
        super(outputSchema, changesType);
    }

    @Override
    public TypeRewriteRule makeRule() {
        return null;
    }
}
''')

rel = 'drzhark/mocreatures/compat/datafixes/EntityIDFixer.java'
s = read(rel)
s = s.replace('import net.minecraft.resources.ResourceLocation;\n', '')
s = s.replace('import net.minecraft.world.entity.EntityType;\n', '')
s = s.replace('import net.neoforged.neoforge.common.NeoForge;\n', '')
s = s.replace('import net.neoforged.bus.api.SubscribeEvent;\n', '')
s = s.replace('import net.neoforged.neoforge.registries.ForgeRegistries;\n', '')
s = s.replace('import net.neoforged.neoforge.registries.MissingMappingsEvent;\n', '')
s = re.sub(r'\n\s*public EntityIDFixer\(\) \{\s*NeoForge\.EVENT_BUS\.register\(this\);\s*}\s*', '\n', s, count=1)
s = re.sub(r'\n\s*@SuppressWarnings\("removal"\)\s*@SubscribeEvent\s*public void onMissingEntityMappings\(MissingMappingsEvent event\) \{[\s\S]*?\n\s*}\n\s*}\s*$', '\n}\n', s, count=1)
write(rel, s)


# -----------------------------------------------------------------------------
# ArmorMaterial was changed from an interface to a registered value in 1.21.1.
# Use NeoForge's documented Holder<ArmorMaterial> registration model.
# -----------------------------------------------------------------------------
write('drzhark/mocreatures/util/MoCArmorMaterial.java', '''package drzhark.mocreatures.util;

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
''')

rel = 'drzhark/mocreatures/item/MoCItemArmor.java'
s = read(rel)
s = s.replace('import net.minecraft.world.item.ArmorMaterial;\n', 'import net.minecraft.world.item.ArmorMaterial;\nimport net.minecraft.core.Holder;\n')
s = s.replace('public MoCItemArmor(Item.Properties properties, ArmorMaterial materialIn, Type type) {',
              'public MoCItemArmor(Item.Properties properties, Holder<ArmorMaterial> materialIn, Type type) {')
write(rel, s)

rel = 'drzhark/mocreatures/MoCreatures.java'
s = read(rel)
if 'import drzhark.mocreatures.util.MoCArmorMaterial;' not in s:
    s = s.replace('import drzhark.mocreatures.world.MoCSpawnBiomeModifier;\n',
                  'import drzhark.mocreatures.world.MoCSpawnBiomeModifier;\nimport drzhark.mocreatures.util.MoCArmorMaterial;\n')
s = s.replace('        MoCItems.ITEMS.register(modBus);',
              '        MoCArmorMaterial.ARMOR_MATERIALS.register(modBus);\n        MoCItems.ITEMS.register(modBus);')
write(rel, s)


# -----------------------------------------------------------------------------
# Tree grower API was replaced by final TreeGrower in modern Minecraft.
# Keep one deterministic configured-feature entry for now; once compilation is
# green the large/alternate selection can be restored on top of the new API.
# -----------------------------------------------------------------------------
write('drzhark/mocreatures/block/WyvwoodTreeGrower.java', '''/*
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
''')

rel = 'drzhark/mocreatures/block/MoCBlockSapling.java'
s = read(rel).replace('super(new WyvwoodTreeGrower(), properties', 'super(WyvwoodTreeGrower.GROWER, properties')
write(rel, s)


# -----------------------------------------------------------------------------
# Pathfinding enum rename.
# -----------------------------------------------------------------------------
rel = 'drzhark/mocreatures/entity/ai/EntityAIMoverHelperMoC.java'
s = read(rel)
s = s.replace('import net.minecraft.world.level.pathfinder.BlockPathTypes;',
              'import net.minecraft.world.level.pathfinder.PathType;')
s = s.replace('BlockPathTypes.WALKABLE', 'PathType.WALKABLE')
write(rel, s)


# -----------------------------------------------------------------------------
# ForgeRegistries was an old Forge registry facade. For vanilla registries,
# DeferredRegister can target the vanilla Registries keys directly.
# This handles the common registration-only usages across the source tree.
# -----------------------------------------------------------------------------
registry_map = {
    'ForgeRegistries.BLOCKS': 'Registries.BLOCK',
    'ForgeRegistries.ITEMS': 'Registries.ITEM',
    'ForgeRegistries.ENTITY_TYPES': 'Registries.ENTITY_TYPE',
    'ForgeRegistries.SOUND_EVENTS': 'Registries.SOUND_EVENT',
    'ForgeRegistries.PARTICLE_TYPES': 'Registries.PARTICLE_TYPE',
    'ForgeRegistries.FEATURES': 'Registries.FEATURE',
    'ForgeRegistries.POI_TYPES': 'Registries.POINT_OF_INTEREST_TYPE',
}
for p in ROOT.rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    old = s
    for a, b in registry_map.items():
        s = s.replace(a, b)
    if 'ForgeRegistries.' not in s:
        s = s.replace('import net.neoforged.neoforge.registries.ForgeRegistries;\n', '')
    if 'Registries.' in s and 'import net.minecraft.core.registries.Registries;' not in s:
        # Insert with the imports, preserving package declaration.
        m = re.search(r'(?m)^import ', s)
        if m:
            s = s[:m.start()] + 'import net.minecraft.core.registries.Registries;\n' + s[m.start():]
    if s != old:
        p.write_text(s, encoding='utf-8')

print('Applied stage3 NeoForge 1.21.1 fixes')
