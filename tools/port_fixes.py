from pathlib import Path
import re

root = Path('src/main/java')
if not root.exists():
    raise SystemExit('src/main/java not found')

for p in root.rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    old = s
    if 'Mod.EventBusSubscriber' in s:
        s = s.replace('Mod.EventBusSubscriber.Bus.MOD', 'EventBusSubscriber.Bus.MOD')
        s = s.replace('Mod.EventBusSubscriber.Bus.FORGE', 'EventBusSubscriber.Bus.GAME')
        s = s.replace('@Mod.EventBusSubscriber', '@EventBusSubscriber')
        if 'import net.neoforged.fml.common.EventBusSubscriber;' not in s:
            if 'import net.neoforged.fml.common.Mod;' in s:
                s = s.replace('import net.neoforged.fml.common.Mod;', 'import net.neoforged.fml.common.Mod;\nimport net.neoforged.fml.common.EventBusSubscriber;')
            else:
                i = s.find('import ')
                s = s[:i] + 'import net.neoforged.fml.common.EventBusSubscriber;\n' + s[i:]
        if '@Mod' not in s and 'Mod.' not in s:
            s = s.replace('import net.neoforged.fml.common.Mod;\n', '')
    if s != old:
        p.write_text(s, encoding='utf-8')

for p in root.rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    old = s
    if 'MobType' not in s:
        continue
    s = s.replace('import net.minecraft.world.entity.MobType;\n', '')
    while True:
        m = re.search(r'(?m)^\s*@Override\s*\n\s*public\s+MobType\s+getMobType\s*\(\s*\)\s*\{', s)
        if not m:
            break
        start = m.start()
        pre = s[:start]
        jm = re.search(r'(?s)/\*\*.*?MobType.*?\*/\s*$', pre)
        if jm and pre[jm.start():].count('\n') <= 8:
            start = jm.start()
        brace = s.find('{', m.start())
        depth = 0
        end = None
        for i in range(brace, len(s)):
            if s[i] == '{':
                depth += 1
            elif s[i] == '}':
                depth -= 1
                if depth == 0:
                    end = i + 1
                    break
        if end is None:
            raise RuntimeError(f'Unbalanced getMobType in {p}')
        while end < len(s) and s[end] in ' \t':
            end += 1
        if end < len(s) and s[end] == '\n':
            end += 1
        s = s[:start] + s[end:]
    if s != old:
        p.write_text(s, encoding='utf-8')

for p in root.rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    old = s
    if 'SynchedEntityData.Builder' in s and 'import net.minecraft.network.syncher.SynchedEntityData;' not in s:
        i = s.find('import ')
        s = s[:i] + 'import net.minecraft.network.syncher.SynchedEntityData;\n' + s[i:]
    if s != old:
        p.write_text(s, encoding='utf-8')

simple = {
    'MoCPOI.java': {'ForgeRegistries.POI_TYPES': 'Registries.POINT_OF_INTEREST_TYPE'},
    'MoCParticles.java': {'ForgeRegistries.PARTICLE_TYPES': 'Registries.PARTICLE_TYPE'},
    'MoCSpawnEggs.java': {'ForgeRegistries.ITEMS': 'Registries.ITEM'},
    'MoCSoundEvents.java': {'ForgeRegistries.SOUND_EVENTS': 'Registries.SOUND_EVENT'},
    'MoCFeatures.java': {'ForgeRegistries.FEATURES': 'Registries.FEATURE'},
    'MoCEntities.java': {'ForgeRegistries.ENTITY_TYPES': 'Registries.ENTITY_TYPE'},
    'MoCBlocks.java': {'ForgeRegistries.BLOCKS': 'Registries.BLOCK', 'ForgeRegistries.ITEMS': 'Registries.ITEM'},
}
for p in root.rglob('*.java'):
    if p.name not in simple:
        continue
    s = p.read_text(encoding='utf-8')
    old = s
    for a, b in simple[p.name].items():
        s = s.replace(a, b)
    s = s.replace('import net.neoforged.neoforge.registries.ForgeRegistries;\n', '')
    if 'Registries.' in s and 'import net.minecraft.core.registries.Registries;' not in s:
        i = s.find('import ')
        s = s[:i] + 'import net.minecraft.core.registries.Registries;\n' + s[i:]
    if s != old:
        p.write_text(s, encoding='utf-8')

for rel in ['drzhark/mocreatures/entity/tameable/MoCEntityTameableAmbient.java', 'drzhark/mocreatures/entity/tameable/MoCEntityTameableAquatic.java']:
    p = root / rel
    s = p.read_text(encoding='utf-8')
    old = s
    s = s.replace('ForgeRegistries.ENTITY_TYPES.getValue(', 'BuiltInRegistries.ENTITY_TYPE.get(')
    s = s.replace('import net.neoforged.neoforge.registries.ForgeRegistries;\n', '')
    if 'BuiltInRegistries.' in s and 'import net.minecraft.core.registries.BuiltInRegistries;' not in s:
        i = s.find('import ')
        s = s[:i] + 'import net.minecraft.core.registries.BuiltInRegistries;\n' + s[i:]
    if s != old:
        p.write_text(s, encoding='utf-8')

p = root / 'drzhark/mocreatures/config/biome/BiomeConfig.java'
s = p.read_text(encoding='utf-8')
old = s
s = s.replace('import net.neoforged.neoforge.registries.ForgeRegistries;\n', '')
s = s.replace('return test(spawns, biome, ForgeRegistries.BIOMES.getKey(biome.value()));', 'return biome.unwrapKey().map(key -> test(spawns, biome, key.location())).orElse(false);')
if s != old:
    p.write_text(s, encoding='utf-8')

p = root / 'drzhark/mocreatures/network/command/multision/MoCDebugSpawnCommand.java'
s = p.read_text(encoding='utf-8')
s2 = s.replace('import net.neoforged.neoforge.registries.ForgeRegistries;\n', '')
if s2 != s:
    p.write_text(s2, encoding='utf-8')

p = root / 'drzhark/mocreatures/init/MoCSpawnEggs.java'
s = p.read_text(encoding='utf-8')
old = s
s = s.replace('import net.neoforged.neoforge.common.ForgeSpawnEggItem;', 'import net.neoforged.neoforge.common.DeferredSpawnEggItem;')
s = s.replace('new ForgeSpawnEggItem(', 'new DeferredSpawnEggItem(')
if s != old:
    p.write_text(s, encoding='utf-8')
