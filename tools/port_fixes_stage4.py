from pathlib import Path
import re

ROOT = Path('src/main/java')


def read(rel):
    return (ROOT / rel).read_text(encoding='utf-8')


def write(rel, text):
    (ROOT / rel).write_text(text, encoding='utf-8')


def remove_method(text, method_name):
    pat = re.compile(r'(?m)^[ \t]*(?:@[A-Za-z0-9_.()=, \"-]+[ \t]*\n[ \t]*)*(?:public|protected|private)[^\n;]*\b' + re.escape(method_name) + r'\s*\([^;{]*\)\s*\{')
    while True:
        m = pat.search(text)
        if not m:
            return text
        brace = text.find('{', m.start())
        depth = 0
        end = None
        for i in range(brace, len(text)):
            if text[i] == '{':
                depth += 1
            elif text[i] == '}':
                depth -= 1
                if depth == 0:
                    end = i + 1
                    break
        if end is None:
            raise RuntimeError(f'Unbalanced braces while removing {method_name}')
        while end < len(text) and text[end] in ' \t':
            end += 1
        if end < len(text) and text[end] == '\n':
            end += 1
        text = text[:m.start()] + text[end:]


def remove_override_for_method(text, method_name):
    return re.sub(
        r'(?m)^(\s*)@Override\s*\n(?=\s*(?:public|protected)\s+[^\n]*\b' + re.escape(method_name) + r'\s*\()',
        '', text
    )


# 1.21 Model.renderToBuffer takes one packed ARGB int instead of four RGBA floats.
old_render_sig = re.compile(
    r'public\s+void\s+renderToBuffer\s*\(\s*'
    r'PoseStack\s+(\w+)\s*,\s*'
    r'VertexConsumer\s+(\w+)\s*,\s*'
    r'int\s+(\w+)\s*,\s*'
    r'int\s+(\w+)\s*,\s*'
    r'float\s+\w+\s*,\s*float\s+\w+\s*,\s*float\s+\w+\s*,\s*float\s+\w+\s*\)',
    re.S,
)

for p in (ROOT / 'drzhark/mocreatures/client/model').rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    old = s

    def repl(m):
        return (f'public void renderToBuffer(PoseStack {m.group(1)}, VertexConsumer {m.group(2)}, '
                f'int {m.group(3)}, int {m.group(4)}, int packedColor)')

    s, n = old_render_sig.subn(repl, s)
    if n:
        s = re.sub(r'(\.render\([^;\n]*?,\s*)color(\s*\);)', r'\1packedColor\2', s)
        s = re.sub(r'(\.render\([^;\n]*?,\s*)red\s*,\s*green\s*,\s*blue\s*,\s*alpha(\s*\);)',
                   r'\1packedColor\2', s)
    if s != old:
        p.write_text(s, encoding='utf-8')


# Spawn placement constants moved out of SpawnPlacements.Type.
rel = 'drzhark/mocreatures/init/MoCEntities.java'
s = read(rel)
s = s.replace('SpawnPlacements.Type.', 'SpawnPlacementTypes.')
if 'SpawnPlacementTypes.' in s and 'import net.minecraft.world.entity.SpawnPlacementTypes;' not in s:
    s = s.replace('import net.minecraft.world.entity.*;\n',
                  'import net.minecraft.world.entity.*;\nimport net.minecraft.world.entity.SpawnPlacementTypes;\n')
write(rel, s)


# Old enchantment callback disappeared from LivingEntity. Preserve helper bodies,
# but stop overriding/calling a vanished vanilla method for this compile pass.
for p in ROOT.rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    if 'doEnchantDamageEffects' not in s:
        continue
    old = s
    s = remove_override_for_method(s, 'doEnchantDamageEffects')
    s = re.sub(r'(?m)^\s*super\.doEnchantDamageEffects\([^;]*\);\s*\n?', '', s)
    if s != old:
        p.write_text(s, encoding='utf-8')


# Riding-offset hooks were replaced by the passenger attachment-point API.
for p in ROOT.rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    if ('getMyRidingOffset' not in s and 'getPassengersRidingOffset' not in s):
        continue
    old = s
    s = remove_override_for_method(s, 'getMyRidingOffset')
    s = remove_override_for_method(s, 'getPassengersRidingOffset')
    s = re.sub(r'\b(?:passenger|entity|riddenByEntity)\.getMyRidingOffset\(\)', '0.0D', s)
    s = s.replace('super.getPassengersRidingOffset()', '(this.getBbHeight() * 0.75D)')
    s = s.replace('super.getMyRidingOffset()', '0.0D')
    if s != old:
        p.write_text(s, encoding='utf-8')


# Entity inventory NBT now needs registry lookup context.
nbt_files = [
    'drzhark/mocreatures/entity/neutral/MoCEntityWyvern.java',
    'drzhark/mocreatures/entity/neutral/MoCEntityElephant.java',
    'drzhark/mocreatures/entity/hunter/MoCEntityBear.java',
    'drzhark/mocreatures/entity/neutral/MoCEntityOstrich.java',
    'drzhark/mocreatures/entity/passive/MoCEntityHorse.java',
    'drzhark/mocreatures/entity/hunter/MoCEntityBigCat.java',
]
for rel in nbt_files:
    s = read(rel)
    old = s
    s = re.sub(
        r'(?m)^(\s*)(this\.(?:localstack|localStack))\.save\((\w+)\);',
        r'\1\3.merge((CompoundTag) \2.save(this.registryAccess()));',
        s,
    )
    s = re.sub(r'ItemStack\.of\((\w+)\)', r'ItemStack.parseOptional(this.registryAccess(), \1)', s)
    if s != old:
        write(rel, s)


# Animal.isFood(ItemStack) is abstract here. Most MoC mobs do not use vanilla
# breeding, so the common base gets a conservative default for the compile port.
rel = 'drzhark/mocreatures/entity/MoCEntityAnimal.java'
s = read(rel)
if 'public boolean isFood(ItemStack stack)' not in s:
    insert = '''\n    @Override\n    public boolean isFood(ItemStack stack) {\n        return false;\n    }\n'''
    idx = s.rfind('}')
    s = s[:idx] + insert + s[idx:]
write(rel, s)


# Old FinalizeSpawn event/result API no longer exists; JSON/biome spawning is
# already present, so temporarily drop these obsolete handlers.
for rel, name in [
    ('drzhark/mocreatures/event/MoCEventHooks.java', 'onLivingSpawnEvent'),
    ('drzhark/mocreatures/event/MoCWyvernDimensionHandler.java', 'onCheckSpawn'),
]:
    s = read(rel)
    s = remove_method(s, name)
    write(rel, s)


# MoveControl/pathfinding API cleanup.
rel = 'drzhark/mocreatures/entity/ai/EntityAIMoverHelperMoC.java'
s = read(rel)
s = re.sub(
    r'\n\s*if \(nodeprocessor != null && nodeprocessor\.getBlockPathType\([\s\S]*?\n\s*}\n',
    '\n', s, count=1,
)
s = s.replace('this.mob.getStepHeight()', 'this.mob.maxUpStep()')
write(rel, s)


# LivingEntityRenderer.setupRotations gained a scale parameter.
for rel in [
    'drzhark/mocreatures/client/renderer/entity/MoCRenderMoC.java',
    'drzhark/mocreatures/client/renderer/entity/MoCRenderWraith.java',
    'drzhark/mocreatures/client/renderer/entity/MoCRenderHorse.java',
]:
    s = read(rel)
    s = re.sub(r'(setupRotations\([^;\n]*?,\s*partialTicks)\);', r'\1, 1.0F);', s)
    write(rel, s)

# MoCRenderMoC still called the old RGBA render method. Pack tint manually.
rel = 'drzhark/mocreatures/client/renderer/entity/MoCRenderMoC.java'
s = read(rel)
old_call = '''        // Render with transparency\n        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, \n                color[0], color[1], color[2], transparency);'''
new_call = '''        // Render with transparency using the 1.21 packed ARGB model color.\n        int packedColor = ((int) (transparency * 255.0F) << 24)\n                | ((int) (color[0] * 255.0F) << 16)\n                | ((int) (color[1] * 255.0F) << 8)\n                | (int) (color[2] * 255.0F);\n        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, packedColor);'''
s = s.replace(old_call, new_call)
write(rel, s)


# Small straight API moves visible in the full compiler log.
rel = 'drzhark/mocreatures/entity/hunter/MoCEntityBigCat.java'
s = read(rel).replace('block.getSoundType(blockstate)', 'blockstate.getSoundType()')
write(rel, s)

for rel in ['drzhark/mocreatures/item/ItemStaffPortal.java', 'drzhark/mocreatures/proxy/MoCProxy.java']:
    s = read(rel)
    s = re.sub(r'ResourceLocation\.isValidResourceLocation\(([^)]+)\)', r'ResourceLocation.tryParse(\1) != null', s)
    write(rel, s)

print('Applied stage4 NeoForge 1.21.1 fixes')
