# Mo' Creatures 1.21.1 NeoForge port — status

Target: Minecraft 1.21.1, NeoForge 21.1.219, Java 21.

This directory is a substantial source-level port of the supplied 1.20.1 Forge project. It is **not yet claimed to be a release-ready or runtime-tested build** because the current sandbox cannot resolve the Gradle distribution / Maven dependencies required to compile NeoForge.

## Migrated in this iteration

- Project/toolchain moved to Java 21 + NeoGradle/NeoForge 1.21.1 metadata (`neoforge.mods.toml`).
- Forge package imports removed and mod/event-bus bootstrap moved to NeoForge equivalents.
- Registration code migrated away from `RegistryObject` where required by the port.
- `ResourceLocation` construction migrated to the 1.21 factories.
- Networking replaced with 1.21 `CustomPacketPayload` / `StreamCodec` registration; compatibility wrappers preserve the old call sites for PLAYER / NEAR / TRACKING_ENTITY / sendToServer behavior.
- Biome modifier serializer migrated to `MapCodec`; datapack biome modifier namespace/path moved from Forge to NeoForge.
- Common tag namespace moved from `forge:` to `c:` and 1.21 singular datapack paths were applied.
- Recipe JSON results migrated to 1.21 format. 701 JSON files parse successfully after the resource migration.
- Direct ItemStack NBT usage migrated to data components (`DataComponents.CUSTOM_DATA` and related components).
- Loot-table keys, XP overrides, custom death-loot overrides and `finalizeSpawn` signatures migrated for 1.21.
- Item attribute modifiers migrated from the old UUID/Multimap API to `ItemAttributeModifiers`; entity interaction reach now uses the vanilla 1.21 attribute.
- Cross-dimension teleportation migrated from `ITeleporter` to `DimensionTransition`.
- Enchantment lookups in the portal staff migrated to registry `Holder<Enchantment>` access.
- Entity spawn-extra-data handling migrated to NeoForge's current complex-spawn interface where used.
- Synched entity-data definitions migrated to the builder form.
- Rendering vertex chains migrated from `vertex/color/uv/.../endVertex` to the 1.21 vertex API.
- Model rendering migrated from four float tint parameters to packed ARGB `int color` across the model package and custom renderer call sites.

## Static checks completed

- Java 21 parser pass: no syntax-style errors detected across the Java source tree (dependency symbols cannot be resolved without the NeoForge classpath).
- `net.minecraftforge` imports: 0.
- `RegistryObject` references: 0.
- `SimpleChannel` / old Forge networking references: 0.
- direct legacy ItemStack NBT calls (`getTag`, `getOrCreateTag`, `setTag`, `hasTag`): 0.
- legacy model vertex `.vertex()` / `.endVertex()` calls: 0.
- legacy RGBA `ModelPart.render(...)` tails: 0.
- JSON parse validation: 701 files, 0 malformed.

## Known high-risk / unfinished validation areas

1. A real `./gradlew clean build` has not run in this sandbox because Gradle cannot download its distribution/dependencies here. Compile-time API mismatches can therefore still remain.
2. Clientbound packet handlers should be tested on a dedicated server to make sure no client class is eagerly classloaded during common registration.
3. Runtime rendering should be checked for translucent entities/layers (ghost horses, wraiths, insect wings, golem effect layers, legacy big cats).
4. Texture-stitch/client registration, custom render types and event signatures need confirmation by an actual client launch.
5. Worldgen/biome modifiers and spawn placement need in-world testing; datapack paths/formats were migrated but behavior is not runtime-verified.
6. Five custom mob drop routines currently set their manual looting bonus to zero; vanilla loot tables still work, but the mod-specific extra looting bonus needs a proper 1.21 enchantment-context implementation.
7. Data-component compatibility for old 1.20.1 worlds/items should be tested; the port preserves the mod's logical keys but old serialized ItemStack NBT does not automatically become new components.

## What to run locally

```bash
./gradlew clean build
./gradlew runClient
./gradlew runServer
```

On Windows:

```bat
gradlew.bat clean build
gradlew.bat runClient
gradlew.bat runServer
```

The first useful artifact for the next port iteration is the **first full `clean build` error log**. Fix compile errors from the top downward; after a clean build, test a fresh client world and then a dedicated server.
