from pathlib import Path
import re

ROOT = Path('src/main/java')


def read(rel):
    return (ROOT / rel).read_text(encoding='utf-8')


def write(rel, text):
    (ROOT / rel).write_text(text, encoding='utf-8')


def remove_method(text, method_name):
    # Remove a Java method (including immediately preceding @Override) by balanced braces.
    pat = re.compile(r'(?m)^[ \t]*(?:@Override[ \t]*\n[ \t]*)?(?:public|protected)[^\n;]*\b' + re.escape(method_name) + r'\s*\([^;{]*\)\s*\{')
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


# Show the entire compiler-error surface instead of javac stopping after 100 errors.
bg = Path('build.gradle')
s = bg.read_text(encoding='utf-8')
s = s.replace("tasks.withType(JavaCompile).configureEach { options.encoding = 'UTF-8' }",
              "tasks.withType(JavaCompile).configureEach { options.encoding = 'UTF-8'; options.compilerArgs += ['-Xmaxerrs', '1000'] }")
bg.write_text(s, encoding='utf-8')

# Player tick events were split into Pre/Post in NeoForge 1.21.1.
rel = 'drzhark/mocreatures/client/MoCInputHandler.java'
s = read(rel)
s = s.replace('import net.neoforged.neoforge.event.TickEvent;', 'import net.neoforged.neoforge.event.tick.PlayerTickEvent;')
s = s.replace('public static void onInput(TickEvent.PlayerTickEvent e) {\n        if (e.phase != TickEvent.Phase.END) return;',
              'public static void onInput(PlayerTickEvent.Post e) {')
s = s.replace('e.player', 'e.getEntity()')
write(rel, s)

# DistExecutor no longer exists in current FML. Main proxy selection can use the physical dist directly.
rel = 'drzhark/mocreatures/MoCreatures.java'
s = read(rel)
s = s.replace('import net.neoforged.fml.DistExecutor;\n', '')
s = s.replace('this.proxy = DistExecutor.unsafeRunForDist(() -> MoCProxyClient::new, () -> MoCProxy::new);',
              'this.proxy = net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT ? new MoCProxyClient() : new MoCProxy();')
write(rel, s)

# The payload is explicitly registered playToClient, so its callback already runs on the client side.
rel = 'drzhark/mocreatures/network/message/MoCMessageNameGUI.java'
s = read(rel)
s = s.replace('import net.neoforged.fml.DistExecutor;\n', '')
s = re.sub(r'\s*// We\'re already on the client when we receive this packet\n\s*if \(DistExecutor\.unsafeRunForDist\([\s\S]*?\n\s*}\n\s*ctx\.get\(\)\.setPacketHandled\(true\);',
           '\n        ctx.get().enqueueWork(() -> handleClient(message));\n        ctx.get().setPacketHandled(true);', s, count=1)
write(rel, s)

# RecordItem was removed in 1.21. Keep the item compiling first; jukebox-playable data is migrated separately.
rel = 'drzhark/mocreatures/item/MoCItemRecord.java'
write(rel, '''package drzhark.mocreatures.item;\n\nimport drzhark.mocreatures.MoCConstants;\nimport net.minecraft.resources.ResourceLocation;\nimport net.minecraft.sounds.SoundEvent;\nimport net.minecraft.world.item.Item;\n\npublic class MoCItemRecord extends Item {\n    @SuppressWarnings("removal")\n    public static final ResourceLocation RECORD_SHUFFLE_RESOURCE = ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "shuffling");\n\n    public MoCItemRecord(int comparatorValueIn, java.util.function.Supplier<SoundEvent> soundSupplier, Item.Properties properties) {\n        super(properties);\n    }\n}\n''')

# GlassBlock is no longer present under the old type; TransparentBlock has the required transparent-block behaviour.
rel = 'drzhark/mocreatures/block/MoCBlockGlass.java'
s = read(rel).replace('import net.minecraft.world.level.block.GlassBlock;', 'import net.minecraft.world.level.block.TransparentBlock;')
s = s.replace('extends GlassBlock', 'extends TransparentBlock')
write(rel, s)

# IPlantable/canSustainPlant was removed. Drop the obsolete overrides for now; vanilla placement logic remains.
for rel in ['drzhark/mocreatures/block/MoCBlockSand.java', 'drzhark/mocreatures/block/MoCBlockGrass.java']:
    s = read(rel)
    s = remove_method(s, 'canSustainPlant')
    s = s.replace('import net.neoforged.neoforge.common.IPlantable;\n', '')
    s = s.replace('import net.minecraft.core.Direction;\n', '')
    if rel.endswith('MoCBlockGrass.java'):
        s = s.replace('import net.minecraft.world.level.block.MushroomBlock;\n', '')
    write(rel, s)

# Vanilla short grass was renamed.
for p in ROOT.rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    old = s
    s = re.sub(r'\bBlocks\.GRASS\b', 'Blocks.SHORT_GRASS', s)
    s = s.replace('sizeIn.height', 'sizeIn.height()')
    if s != old:
        p.write_text(s, encoding='utf-8')

# setMaxUpStep was replaced by the step-height attribute.
for p in ROOT.rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    if 'setMaxUpStep(' not in s:
        continue
    old = s
    s = re.sub(r'(?:(?:this)\.)?setMaxUpStep\(([^;]+)\);', r'this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(\1);', s)
    if 'Attributes.STEP_HEIGHT' in s and 'import net.minecraft.world.entity.ai.attributes.Attributes;' not in s:
        idx = s.find('import ')
        s = s[:idx] + 'import net.minecraft.world.entity.ai.attributes.Attributes;\n' + s[idx:]
    if s != old:
        p.write_text(s, encoding='utf-8')

# LivingEntity.canBreatheUnderwater is final in 1.21.1. Remove obsolete overrides; amphibious behaviour will be restored via the modern hook.
for p in ROOT.rglob('*.java'):
    s = p.read_text(encoding='utf-8')
    if 'canBreatheUnderwater(' not in s:
        continue
    ns = remove_method(s, 'canBreatheUnderwater')
    if ns != s:
        p.write_text(ns, encoding='utf-8')

# Entity canBeLeashed no longer receives the player. Keep a basic no-arg override until ownership interaction is migrated.
for rel in ['drzhark/mocreatures/entity/MoCEntityAnimal.java', 'drzhark/mocreatures/entity/MoCEntityAmbient.java']:
    s = read(rel)
    s = re.sub(r'(?s)@Override\s+public boolean canBeLeashed\(Player player\) \{.*?return super\.canBeLeashed\(player\);\s*}',
               '@Override\n    public boolean canBeLeashed() {\n        return super.canBeLeashed();\n    }', s, count=1)
    write(rel, s)

# One stale aquatic leash helper was calling canBeLeashed instead of attaching the leash; remove the invalid override annotation/call for compilation.
rel = 'drzhark/mocreatures/entity/MoCEntityAquatic.java'
s = read(rel)
s = s.replace('        super.canBeLeashed((Player) entity);', '        // Ownership check handled by interaction path in 1.21.1.')
write(rel, s)

# ParticleOptions now use MapCodec + StreamCodec and ParticleType has a streamCodec() method.
particle_specs = {
    'StarParticleData': ('STAR_FX', False),
    'VacuumParticleData': ('VACUUM_FX', False),
    'VanishParticleData': ('VANISH_FX', True),
}
for cls, (field, has_bool) in particle_specs.items():
    rel = f'drzhark/mocreatures/client/renderer/fx/data/{cls}.java'
    bool_decl = '\n    public final boolean implode;' if has_bool else ''
    ctor_params = 'float red, float green, float blue, boolean implode' if has_bool else 'float red, float green, float blue'
    ctor_assign = '\n        this.implode = implode;' if has_bool else ''
    codec_tail = ', Codec.BOOL.fieldOf("implode").forGetter(v -> v.implode)' if has_bool else ''
    decode_tail = ', buf.readBoolean()' if has_bool else ''
    encode_tail = '\n            buf.writeBoolean(value.implode);' if has_bool else ''
    content = f'''package drzhark.mocreatures.client.renderer.fx.data;\n\nimport com.mojang.serialization.Codec;\nimport com.mojang.serialization.MapCodec;\nimport com.mojang.serialization.codecs.RecordCodecBuilder;\nimport drzhark.mocreatures.client.renderer.fx.MoCParticles;\nimport net.minecraft.core.particles.ParticleOptions;\nimport net.minecraft.core.particles.ParticleType;\nimport net.minecraft.network.RegistryFriendlyByteBuf;\nimport net.minecraft.network.codec.StreamCodec;\n\npublic class {cls} implements ParticleOptions {{\n    public static final MapCodec<{cls}> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(\n            Codec.FLOAT.fieldOf("red").forGetter(v -> v.red),\n            Codec.FLOAT.fieldOf("green").forGetter(v -> v.green),\n            Codec.FLOAT.fieldOf("blue").forGetter(v -> v.blue){codec_tail}\n    ).apply(instance, {cls}::new));\n\n    public static final StreamCodec<RegistryFriendlyByteBuf, {cls}> STREAM_CODEC = new StreamCodec<>() {{\n        @Override\n        public {cls} decode(RegistryFriendlyByteBuf buf) {{\n            return new {cls}(buf.readFloat(), buf.readFloat(), buf.readFloat(){decode_tail});\n        }}\n\n        @Override\n        public void encode(RegistryFriendlyByteBuf buf, {cls} value) {{\n            buf.writeFloat(value.red);\n            buf.writeFloat(value.green);\n            buf.writeFloat(value.blue);{encode_tail}\n        }}\n    }};\n\n    public final float red, green, blue;{bool_decl}\n\n    public {cls}({ctor_params}) {{\n        this.red = red;\n        this.green = green;\n        this.blue = blue;{ctor_assign}\n    }}\n\n    @Override\n    public ParticleType<{cls}> getType() {{\n        return MoCParticles.{field}.get();\n    }}\n}}\n'''
    write(rel, content)

rel = 'drzhark/mocreatures/client/renderer/fx/MoCParticles.java'
s = read(rel)
s = s.replace('import com.mojang.serialization.Codec;', 'import com.mojang.serialization.MapCodec;\nimport net.minecraft.network.RegistryFriendlyByteBuf;\nimport net.minecraft.network.codec.StreamCodec;')
s = s.replace('new ParticleType<VanishParticleData>(false, VanishParticleData.DESERIALIZER) {', 'new ParticleType<VanishParticleData>(false) {')
s = s.replace('new ParticleType<StarParticleData>(false, StarParticleData.DESERIALIZER) {', 'new ParticleType<StarParticleData>(false) {')
s = s.replace('new ParticleType<VacuumParticleData>(false, VacuumParticleData.DESERIALIZER) {', 'new ParticleType<VacuumParticleData>(false) {')
s = s.replace('public Codec<VanishParticleData> codec() {\n                    return Codec.unit(new VanishParticleData(1.0F, 1.0F, 1.0F, false));\n                }',
              'public MapCodec<VanishParticleData> codec() { return VanishParticleData.CODEC; }\n                @Override public StreamCodec<? super RegistryFriendlyByteBuf, VanishParticleData> streamCodec() { return VanishParticleData.STREAM_CODEC; }')
s = s.replace('public Codec<StarParticleData> codec() {\n                    return Codec.unit(new StarParticleData(1.0F, 1.0F, 1.0F)); // default white\n                }',
              'public MapCodec<StarParticleData> codec() { return StarParticleData.CODEC; }\n                @Override public StreamCodec<? super RegistryFriendlyByteBuf, StarParticleData> streamCodec() { return StarParticleData.STREAM_CODEC; }')
s = s.replace('public Codec<VacuumParticleData> codec() {\n                    return Codec.unit(new VacuumParticleData(1.0F, 1.0F, 1.0F));\n                }',
              'public MapCodec<VacuumParticleData> codec() { return VacuumParticleData.CODEC; }\n                @Override public StreamCodec<? super RegistryFriendlyByteBuf, VacuumParticleData> streamCodec() { return VacuumParticleData.STREAM_CODEC; }')
write(rel, s)

print('Applied stage2 NeoForge 1.21.1 fixes')
