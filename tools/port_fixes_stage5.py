from pathlib import Path
import re

ROOT = Path('src/main/java')

def read(rel):
    return (ROOT / rel).read_text(encoding='utf-8')

def write(rel, s):
    (ROOT / rel).write_text(s, encoding='utf-8')

def remove_override_for_method(s, name):
    return re.sub(
        r'(?m)^(\s*)@Override\s*\n(?=\s*(?:public|protected)\s+[^\n]*\b' + re.escape(name) + r'\s*\()',
        '', s
    )

def remove_method(s, name):
    pat = re.compile(r'(?m)^[ \t]*(?:@[A-Za-z0-9_.()=, \"-]+[ \t]*\n[ \t]*)*(?:public|protected|private)[^\n;]*\b' + re.escape(name) + r'\s*\([^;{]*\)\s*\{')
    while True:
        m = pat.search(s)
        if not m:
            return s
        brace = s.find('{', m.start())
        depth = 0
        end = None
        for i in range(brace, len(s)):
            if s[i] == '{': depth += 1
            elif s[i] == '}':
                depth -= 1
                if depth == 0:
                    end = i + 1
                    break
        if end is None: raise RuntimeError(f'Unbalanced method {name}')
        while end < len(s) and s[end] in ' \t': end += 1
        if end < len(s) and s[end] == '\n': end += 1
        s = s[:m.start()] + s[end:]

def extract_calls(s, prefix):
    found=[]
    pos=0
    while True:
        start=s.find(prefix,pos)
        if start<0: break
        par=s.find('(',start)
        depth=0; end=None
        in_str=None; esc=False
        for i in range(par,len(s)):
            ch=s[i]
            if in_str:
                if esc: esc=False
                elif ch=='\\': esc=True
                elif ch==in_str: in_str=None
                continue
            if ch == '"': in_str=ch; continue
            if ch=='(': depth+=1
            elif ch==')':
                depth-=1
                if depth==0:
                    j=i+1
                    while j<len(s) and s[j] in ' \t\r\n': j+=1
                    if j<len(s) and s[j]==';':
                        end=j+1
                        break
        if end is None: raise RuntimeError(f'Unbalanced call {prefix}')
        line_start=s.rfind('\n',0,start)+1
        found.append((line_start,end,s[line_start:end]))
        pos=end
    return found

# 1) Spawn placements: vanilla register is private. NeoForge exposes a mod-bus event.
rel='drzhark/mocreatures/init/MoCEntities.java'
s=read(rel)
calls=extract_calls(s,'SpawnPlacements.register')
if calls:
    transformed=[]
    for _,_,stmt in calls:
        indent=re.match(r'\s*',stmt).group(0)
        body=stmt[len(indent):].replace('SpawnPlacements.register(', 'event.register(', 1)
        body=re.sub(r'\);\s*$', ', RegisterSpawnPlacementsEvent.Operation.REPLACE);', body)
        transformed.append('        ' + body.replace('\n'+indent, '\n        '))
    for a,b,_ in reversed(calls):
        s=s[:a]+s[b:]
    marker='    /**\n     * Register entity attributes\n     */'
    method='''    /** Register natural spawn placement predicates on the NeoForge mod bus. */\n    @SubscribeEvent\n    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {\n''' + '\n'.join(transformed) + '''\n    }\n\n'''
    if marker not in s: raise RuntimeError('attribute marker not found')
    s=s.replace(marker, method+marker,1)
if 'RegisterSpawnPlacementsEvent' in s and 'import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;' not in s:
    s=s.replace('import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;\n',
                'import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;\nimport net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;\n')
write(rel,s)

# 2) Legacy hooks no longer overriding vanilla methods. Keep helpers for later semantic wiring.
for p in ROOT.rglob('*.java'):
    s=p.read_text(encoding='utf-8'); old=s
    for name in ['getStandingEyeHeight','getAttackReachSqr','canChangeDimensions','getUseDuration','onArmorTick','canApplyAtEnchantingTable','isValidBonemealTarget','getExpDrop']:
        s=remove_override_for_method(s,name)
    if s!=old: p.write_text(s,encoding='utf-8')

# 3) Final getDimensions cannot be shadowed; remove these two legacy dynamic-size methods.
for rel in ['drzhark/mocreatures/entity/passive/MoCEntityFilchLizard.java',
            'drzhark/mocreatures/entity/hostile/MoCEntityWerewolf.java']:
    s=remove_method(read(rel),'getDimensions')
    write(rel,s)

# jumpFromGround is public in 1.21.
rel='drzhark/mocreatures/entity/ambient/MoCEntitySnail.java'
s=read(rel).replace('protected void jumpFromGround()', 'public void jumpFromGround()')
write(rel,s)

# 4) 1.21 packed ARGB model rendering cleanup.
for rel in ['drzhark/mocreatures/client/model/MoCModelGrasshopper.java',
            'drzhark/mocreatures/client/model/MoCModelFly.java',
            'drzhark/mocreatures/client/model/MoCModelBee.java']:
    s=read(rel)
    s=s.replace('int packedOverlay, int color)', 'int packedOverlay, int packedColor)')
    s=s.replace('packedOverlay, color);', 'packedOverlay, packedColor);')
    write(rel,s)

rel='drzhark/mocreatures/client/model/MoCModelJellyFish.java'
s=read(rel).replace('packedOverlayIn, red, green, blue, getTransparencyValue());',
                    'packedOverlayIn, color);')
write(rel,s)

for rel in ['drzhark/mocreatures/client/model/MoCModelAnt.java',
            'drzhark/mocreatures/client/model/MoCModelBear.java']:
    s=read(rel)
    s=re.sub(r'public void renderToBuffer\(\s*@NotNull PoseStack (\w+),\s*@NotNull VertexConsumer (\w+),\s*int (\w+),\s*int (\w+),\s*float red,\s*float green,\s*float blue,\s*float alpha\s*\)',
             r'public void renderToBuffer(@NotNull PoseStack \1, @NotNull VertexConsumer \2, int \3, int \4, int packedColor)', s, flags=re.S)
    s=s.replace('packedOverlay, color);','packedOverlay, packedColor);')
    write(rel,s)

# Renderer callsites use packed ARGB too.
rel='drzhark/mocreatures/client/renderer/entity/MoCRenderFirefly.java'
s=read(rel)
s=re.sub(r'(this\.getParentModel\(\)\.renderToBuffer\(poseStack, vertexConsumer, packedLightIn,\s*OverlayTexture\.NO_OVERLAY),\s*1\.0F, 1\.0F, 1\.0F, 1\.0F\);', r'\1, 0xFFFFFFFF);', s)
write(rel,s)
rel='drzhark/mocreatures/client/renderer/entity/MoCRenderWerewolf.java'
s=read(rel)
s=re.sub(r'(humanModel\.renderToBuffer\(poseStack, vertexConsumer, packedLightIn, OverlayTexture\.NO_OVERLAY),\s*1\.0F, 1\.0F, 1\.0F, 1\.0F\);', r'\1, 0xFFFFFFFF);', s)
write(rel,s)
rel='drzhark/mocreatures/client/renderer/entity/MoCRenderKittyBed.java'
s=read(rel)
s=s.replace('float[] rgb = DyeColor.byId(j).getTextureDiffuseColors();', 'int packedColor = DyeColor.byId(j).getTextureDiffuseColor();')
s=re.sub(r'(packedLightIn, OverlayTexture\.NO_OVERLAY), rgb\[0\], rgb\[1\], rgb\[2\], 1\.0F\);', r'\1, packedColor);', s)
write(rel,s)

# 5) Removed enchantment callback: vanilla 1.21 applies enchantment effects elsewhere.
for rel in ['drzhark/mocreatures/entity/MoCEntityAquatic.java','drzhark/mocreatures/entity/MoCEntityAnimal.java','drzhark/mocreatures/entity/MoCEntityMob.java']:
    s=read(rel)
    s=re.sub(r'(?m)^\s*this\.doEnchantDamageEffects\([^;]+\);\s*\n?', '', s)
    write(rel,s)

# Riding offset API changed; preserve approximate old behavior for compile port.
for rel in ['drzhark/mocreatures/entity/neutral/MoCEntityElephant.java','drzhark/mocreatures/entity/passive/MoCEntityHorse.java']:
    s=read(rel)
    s=s.replace('passenger.getPassengersRidingOffset()', '0.0D')
    write(rel,s)

# 6) Small vanilla/NeoForge 1.21 API migrations.
rel='drzhark/mocreatures/entity/hostile/MoCEntityWerewolf.java'
s=read(rel).replace('((SwordItem) stack.getItem()).getDamage()', 'stack.getItem().getDamage(stack)')
write(rel,s)

rel='drzhark/mocreatures/MoCTools.java'
s=read(rel)
s=s.replace('if (!NeoForge.EVENT_BUS.post(breakEvent)) {', 'NeoForge.EVENT_BUS.post(breakEvent);\n                    if (!breakEvent.isCanceled()) {')
s=s.replace('CompoundTag tag = entity.serializeNBT();', 'CompoundTag tag = entity.serializeNBT(entity.registryAccess());')
s=s.replace('return item.isEdible()', 'return new ItemStack(item).has(DataComponents.FOOD)')
if 'DataComponents.FOOD' in s and 'import net.minecraft.core.component.DataComponents;' not in s:
    i=s.find('import '); s=s[:i]+'import net.minecraft.core.component.DataComponents;\n'+s[i:]
write(rel,s)

rel='drzhark/mocreatures/item/MoCItemFood.java'
s=read(rel)
s=s.replace('.saturationMod(saturation)', '.saturationModifier(saturation)')
s=s.replace('                this.foodBuilder.meat();\n','')
s=s.replace('.alwaysEat()', '.alwaysEdible()')
write(rel,s)

for rel in ['drzhark/mocreatures/compat/CompatScreen.java','drzhark/mocreatures/client/gui/MoCGUIEntityNamer.java']:
    s=read(rel)
    s=re.sub(r'this\.renderBackground\((graphics|poseStack)\);', r'this.renderBackground(\1, mouseX, mouseY, partialTicks);',s)
    write(rel,s)

rel='drzhark/mocreatures/worldgen/structure/WyvernIslandFeature.java'
s=read(rel); s=re.sub(r'\s*\.setKeepLiquids\(false\)\s*//[^\n]*', '', s); write(rel,s)

rel='drzhark/mocreatures/config/biome/SpawnBiomeData.java'
s=read(rel).replace('biomeHolder.getTagKeys()', 'biomeHolder.tags()'); write(rel,s)

rel='drzhark/mocreatures/worldgen/feature/WyvernNestFeature.java'
s=read(rel)
s=s.replace('setLootTable(ResourceLocation.parse("mocreatures:chests/wyvern_nest"), random.nextLong())',
            'setLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("mocreatures:chests/wyvern_nest")), random.nextLong())')
if 'ResourceKey.create(Registries.LOOT_TABLE' in s:
    if 'import net.minecraft.resources.ResourceKey;' not in s:
        i=s.find('import '); s=s[:i]+'import net.minecraft.resources.ResourceKey;\n'+s[i:]
    if 'import net.minecraft.core.registries.Registries;' not in s:
        i=s.find('import '); s=s[:i]+'import net.minecraft.core.registries.Registries;\n'+s[i:]
write(rel,s)

# RecordItem was replaced with the JUKEBOX_PLAYABLE data component.
rel='drzhark/mocreatures/entity/passive/MoCEntityHorse.java'
s=read(rel)
s=s.replace('stack.getItem() instanceof RecordItem', 'stack.has(DataComponents.JUKEBOX_PLAYABLE)')
s=s.replace('jukebox.getFirstItem()', 'jukebox.getTheItem()')
s=s.replace('import net.minecraft.world.item.RecordItem;\n','')
if 'DataComponents.JUKEBOX_PLAYABLE' in s and 'import net.minecraft.core.component.DataComponents;' not in s:
    i=s.find('import '); s=s[:i]+'import net.minecraft.core.component.DataComponents;\n'+s[i:]
write(rel,s)

# FallingBlock now requires a codec.
rel='drzhark/mocreatures/block/MoCBlockSand.java'
s=read(rel)
if 'MapCodec<MoCBlockSand>' not in s:
    s=s.replace('package drzhark.mocreatures.block;\n', 'package drzhark.mocreatures.block;\n\nimport com.mojang.serialization.MapCodec;\n')
    s=s.replace('public class MoCBlockSand extends FallingBlock {', 'public class MoCBlockSand extends FallingBlock {\n\n    public static final MapCodec<MoCBlockSand> CODEC = simpleCodec(MoCBlockSand::new);\n\n    @Override\n    protected MapCodec<? extends FallingBlock> codec() {\n        return CODEC;\n    }')
write(rel,s)

# Tier now asks for the incorrect-block tag instead of numeric level.
rel='drzhark/mocreatures/util/MoCItemTier.java'
s=read(rel)
s=s.replace('@Override public int getLevel() { return level; }', 'public int getLevel() { return level; }')
if 'getIncorrectBlocksForDrops()' not in s:
    s=s.replace('import net.minecraft.world.item.Tier;\n', 'import net.minecraft.world.item.Tier;\nimport net.minecraft.world.item.Tiers;\nimport net.minecraft.tags.TagKey;\nimport net.minecraft.world.level.block.Block;\n')
    idx=s.rfind('}')
    meth='''\n    @Override\n    public TagKey<Block> getIncorrectBlocksForDrops() {\n        return switch (level) {\n            case 0 -> Tiers.WOOD.getIncorrectBlocksForDrops();\n            case 1 -> Tiers.STONE.getIncorrectBlocksForDrops();\n            case 2 -> Tiers.IRON.getIncorrectBlocksForDrops();\n            case 3 -> Tiers.DIAMOND.getIncorrectBlocksForDrops();\n            default -> Tiers.NETHERITE.getIncorrectBlocksForDrops();\n        };\n    }\n'''
    s=s[:idx]+meth+s[idx:]
write(rel,s)

print(f'Applied stage5 fixes; moved {len(calls)} spawn placement registrations')
