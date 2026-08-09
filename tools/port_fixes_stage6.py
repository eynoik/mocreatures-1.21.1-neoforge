from pathlib import Path

ROOT = Path('src/main/java')


def read(rel):
    return (ROOT / rel).read_text(encoding='utf-8')


def write(rel, text):
    (ROOT / rel).write_text(text, encoding='utf-8')


# Tool item constructors moved their attack attributes into Item.Properties.
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

# MerchantOffer now uses ItemCost for inputs.
rel = 'drzhark/mocreatures/init/MoCVillagerTrades.java'
s = read(rel)
if 'import net.minecraft.world.item.trading.ItemCost;' not in s:
    s = s.replace(
        'import net.minecraft.world.item.trading.MerchantOffer;',
        'import net.minecraft.world.item.trading.ItemCost;\nimport net.minecraft.world.item.trading.MerchantOffer;'
    )
s = s.replace(
    'new MerchantOffer(new ItemStack(Items.PAPER), new ItemStack(Items.FEATHER), new ItemStack(MoCItems.SCROLLFREEDOM.get()), 12, 10, 0.1F)',
    'new MerchantOffer(new ItemCost(Items.PAPER), java.util.Optional.of(new ItemCost(Items.FEATHER)), new ItemStack(MoCItems.SCROLLFREEDOM.get()), 12, 10, 0.1F)'
)
s = s.replace(
    'return new MerchantOffer(in, out, maxUses, xp, priceMult);',
    'return new MerchantOffer(new ItemCost(in.getItem(), in.getCount()), out, maxUses, xp, priceMult);'
)
write(rel, s)

# BookViewScreen exposes BookAccess.fromItem instead of WrittenBookAccess.
rel = 'drzhark/mocreatures/item/ItemHorseGuide.java'
s = read(rel)
s = s.replace(
    'Minecraft.getInstance().setScreen(new BookViewScreen(new BookViewScreen.WrittenBookAccess(bookStack)));',
    'Minecraft.getInstance().setScreen(new BookViewScreen(BookViewScreen.BookAccess.fromItem(bookStack)));'
)
write(rel, s)

# BlockState exposes the public survival check; BushBlock's method is protected.
rel = 'drzhark/mocreatures/block/MoCBlockGrass.java'
s = read(rel)
s = s.replace(
    'if (((MoCBlockTallGrass) MoCBlocks.tallWyvgrass.get()).canSurvive(tallState, world, spawnPos)) {',
    'if (tallState.canSurvive(world, spawnPos)) {'
)
write(rel, s)

# Loot table constants are ResourceKey<LootTable> in 1.21.x.
rel = 'drzhark/mocreatures/entity/passive/MoCEntityFilchLizard.java'
s = read(rel)
s = s.replace('return MoCLootTables.FILCH_LIZARD_SPAWN;', 'return MoCLootTables.FILCH_LIZARD_SPAWN.location();')
s = s.replace('return MoCLootTables.FILCH_LIZARD_STEAL;', 'return MoCLootTables.FILCH_LIZARD_STEAL.location();')
write(rel, s)

# The inherited legacy riding-offset helper disappeared; preserve the old horse offset locally.
rel = 'drzhark/mocreatures/entity/passive/MoCEntityHorse.java'
s = read(rel)
marker = '    @Override\n    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {'
if '    public double getPassengersRidingOffset() {' not in s and marker in s:
    s = s.replace(
        marker,
        '    public double getPassengersRidingOffset() {\n'
        '        return (this.getBbHeight() * 0.75D) - 0.1D;\n'
        '    }\n\n'
        + marker
    )
write(rel, s)

# Resolve the registered POI holder through the level registry using the existing ResourceKey.
for rel in [
    'drzhark/mocreatures/dimension/worldgen/MoCDirectTeleporter.java',
    'drzhark/mocreatures/dimension/worldgen/MoCWorldGenPortal.java',
]:
    s = read(rel)
    if 'import net.minecraft.core.registries.Registries;' not in s:
        s = s.replace('import net.minecraft.core.BlockPos;', 'import net.minecraft.core.BlockPos;\nimport net.minecraft.core.registries.Registries;')
    s = s.replace(
        'MoCPOI.WYVERN_PORTAL.getHolder().ifPresent(holder -> poiManager.add(pos, holder));',
        'world.registryAccess()\n'
        '                .registryOrThrow(Registries.POINT_OF_INTEREST_TYPE)\n'
        '                .getHolder(MoCPOI.WYVERN_PORTAL_KEY)\n'
        '                .ifPresent(holder -> poiManager.add(pos, holder));'
    )
    s = s.replace(
        'poiManager.add(pos, MoCPOI.WYVERN_PORTAL.getHolder().get());',
        'world.registryAccess()\n'
        '                .registryOrThrow(Registries.POINT_OF_INTEREST_TYPE)\n'
        '                .getHolder(MoCPOI.WYVERN_PORTAL_KEY)\n'
        '                .ifPresent(holder -> poiManager.add(pos, holder));'
    )
    write(rel, s)

print('Applied stage6 NeoForge 1.21.1 compile fixes')
