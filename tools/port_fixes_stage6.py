from pathlib import Path

ROOT = Path('src/main/java')

def read(rel):
    return (ROOT / rel).read_text(encoding='utf-8')

def write(rel, text):
    (ROOT / rel).write_text(text, encoding='utf-8')

# Horse passenger offset: the vanilla getPassengersRidingOffset hook was removed.
rel = 'drzhark/mocreatures/entity/passive/MoCEntityHorse.java'
s = read(rel)
s = s.replace(
    'moveFunction.accept(passenger, newPosX, this.getY() + this.getPassengersRidingOffset() + 0.0D, newPosZ);',
    'moveFunction.accept(passenger, newPosX, this.getY() + (this.getBbHeight() * 0.75D), newPosZ);'
)
write(rel, s)

# Filch Lizard helper methods still expose ResourceLocation; unwrap ResourceKey loot-table constants.
rel = 'drzhark/mocreatures/entity/passive/MoCEntityFilchLizard.java'
s = read(rel)
s = s.replace('return MoCLootTables.FILCH_LIZARD_SPAWN;', 'return MoCLootTables.FILCH_LIZARD_SPAWN.location();')
s = s.replace('return MoCLootTables.FILCH_LIZARD_STEAL;', 'return MoCLootTables.FILCH_LIZARD_STEAL.location();')
write(rel, s)

# MerchantOffer input stacks became ItemCost in 1.21.
rel = 'drzhark/mocreatures/init/MoCVillagerTrades.java'
s = read(rel)
if 'import net.minecraft.world.item.trading.ItemCost;' not in s:
    s = s.replace('import net.minecraft.world.item.trading.MerchantOffer;\n',
                  'import net.minecraft.world.item.trading.MerchantOffer;\nimport net.minecraft.world.item.trading.ItemCost;\n')
if 'import java.util.Optional;' not in s:
    insert = s.find('@EventBusSubscriber')
    s = s[:insert] + 'import java.util.Optional;\n\n' + s[insert:]
s = s.replace(
    'new MerchantOffer(new ItemStack(Items.PAPER), new ItemStack(Items.FEATHER), new ItemStack(MoCItems.SCROLLFREEDOM.get()), 12, 10, 0.1F)',
    'new MerchantOffer(new ItemCost(Items.PAPER), Optional.of(new ItemCost(Items.FEATHER)), new ItemStack(MoCItems.SCROLLFREEDOM.get()), 12, 10, 0.1F)'
)
s = s.replace(
    'return new MerchantOffer(in, out, maxUses, xp, priceMult);',
    'return new MerchantOffer(new ItemCost(in.getItem(), in.getCount()), out, maxUses, xp, priceMult);'
)
write(rel, s)

# Tool constructors now take attributes through Item.Properties.
rel = 'drzhark/mocreatures/item/MoCItemAxe.java'
s = read(rel)
s = s.replace(
    'super(material, damage - 1.0F, speed - 4.0F, properties);',
    'super(material, properties.attributes(AxeItem.createAttributes(material, damage - 1.0F, speed - 4.0F)));'
)
write(rel, s)

rel = 'drzhark/mocreatures/item/MoCItemSword.java'
s = read(rel)
s = s.replace(
    'super(material, 3, -2.4F, properties);',
    'super(material, properties.attributes(SwordItem.createAttributes(material, 3, -2.4F)));'
)
write(rel, s)

# BookViewScreen's legacy WrittenBookAccess class was replaced by BookAccess.fromItem.
rel = 'drzhark/mocreatures/item/ItemHorseGuide.java'
s = read(rel)
s = s.replace(
    'new BookViewScreen(new BookViewScreen.WrittenBookAccess(bookStack))',
    'new BookViewScreen(BookViewScreen.BookAccess.fromItem(bookStack))'
)
write(rel, s)

# Avoid protected BushBlock.canSurvive call through the block object; BlockState exposes the check publicly.
rel = 'drzhark/mocreatures/block/MoCBlockGrass.java'
s = read(rel)
s = s.replace(
    'if (((MoCBlockTallGrass) MoCBlocks.tallWyvgrass.get()).canSurvive(tallState, world, spawnPos)) {',
    'if (tallState.canSurvive(world, spawnPos)) {'
)
write(rel, s)

# Resolve the registered POI as a Holder; 1.21.1 Registry#getOrThrow returns the value, while PoiManager#add requires a Holder.
for rel in [
    'drzhark/mocreatures/dimension/worldgen/MoCDirectTeleporter.java',
    'drzhark/mocreatures/dimension/worldgen/MoCWorldGenPortal.java',
]:
    s = read(rel)
    if 'import net.minecraft.core.registries.BuiltInRegistries;' not in s:
        s = s.replace('import net.minecraft.core.BlockPos;\n',
                      'import net.minecraft.core.BlockPos;\nimport net.minecraft.core.registries.BuiltInRegistries;\n')
    s = s.replace(
        'MoCPOI.WYVERN_PORTAL.getHolder().ifPresent(holder -> poiManager.add(pos, holder));',
        'poiManager.add(pos, BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(MoCPOI.WYVERN_PORTAL.get()));'
    )
    s = s.replace(
        'poiManager.add(pos, MoCPOI.WYVERN_PORTAL.getHolder().get());',
        'poiManager.add(pos, BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(MoCPOI.WYVERN_PORTAL.get()));'
    )
    write(rel, s)

# Dedicated-server dist safety: never leave a direct bytecode reference to the @OnlyIn CLIENT proxy
# in the common mod constructor. RuntimeDistCleaner rejects the whole mod class otherwise.
rel = 'drzhark/mocreatures/MoCreatures.java'
s = read(rel)
s = s.replace('import drzhark.mocreatures.proxy.MoCProxyClient;\n', '')
s = s.replace(
    'this.proxy = net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT ? new MoCProxyClient() : new MoCProxy();',
    'this.proxy = createPhysicalSideProxy();'
)
if 'private static MoCProxy createPhysicalSideProxy()' not in s:
    marker = '    private void setup(final FMLCommonSetupEvent event) {'
    helper = """    private static MoCProxy createPhysicalSideProxy() {\n        if (net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT) {\n            try {\n                Class<?> proxyClass = Class.forName(\"drzhark.mocreatures.proxy.MoCProxyClient\");\n                return (MoCProxy) proxyClass.getDeclaredConstructor().newInstance();\n            } catch (ReflectiveOperationException e) {\n                throw new RuntimeException(\"Failed to create Mo' Creatures client proxy\", e);\n            }\n        }\n        return new MoCProxy();\n    }\n\n"""
    if marker not in s:
        raise RuntimeError('MoCreatures setup marker not found')
    s = s.replace(marker, helper + marker, 1)
write(rel, s)

print('Applied stage6 compile and dedicated-server dist fixes')
