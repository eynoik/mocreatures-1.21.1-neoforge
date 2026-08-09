# Mo' Creatures 1.21.1 NeoForge port — status

Target: Minecraft 1.21.1, NeoForge 21.1.219, Java 21.

This repository contains a substantial source-level port of the supplied 1.20.1 Forge project. The port now **passes a full `./gradlew clean build` directly from the repository source** on GitHub Actions with Java 21 and Gradle 9.2.1. It is not yet claimed to be release-ready because client/server runtime behavior still needs in-game validation.

## Build status

- Full repository-source `./gradlew clean build`: **PASS**.
- Build no longer depends on the staged `tools/port_fixes*.py` scripts or workflow-time source rewriting.
- Gradle wrapper is pinned to 9.2.1 in the repository.
- Produced artifact: `mocreatures-1.21.1-1.0.0-neoforge-port.jar`.

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
- Remaining 1.21.1 compile blockers were fixed in tool items, villager trades, book rendering, block survival checks, loot keys, horse rider offsets and POI holder lookup.

## Validation completed

- Full Java/Gradle build with the real NeoForge classpath: pass.
- `net.minecraftforge` imports: 0.
- `RegistryObject` references: 0.
- `SimpleChannel` / old Forge networking references: 0.
- direct legacy ItemStack NBT calls (`getTag`, `getOrCreateTag`, `setTag`, `hasTag`): 0.
- legacy model vertex `.vertex()` / `.endVertex()` calls: 0.
- legacy RGBA `ModelPart.render(...)` tails: 0.
- JSON parse validation: 701 files, 0 malformed.

## Known high-risk / unfinished validation areas

1. Client launch and gameplay have not yet been fully runtime-tested on 1.21.1 NeoForge.
2. Clientbound packet handlers should be tested on a dedicated server to make sure no client class is eagerly classloaded during common registration.
3. Runtime rendering should be checked for translucent entities/layers (ghost horses, wraiths, insect wings, golem effect layers, legacy big cats).
4. Texture-stitch/client registration, custom render types and event signatures need confirmation by an actual client launch.
5. Worldgen/biome modifiers and spawn placement need in-world testing; datapack paths/formats were migrated but behavior is not runtime-verified.
6. Five custom mob drop routines currently set their manual looting bonus to zero; vanilla loot tables still work, but the mod-specific extra looting bonus needs a proper 1.21 enchantment-context implementation.
7. Data-component compatibility for old 1.20.1 worlds/items should be tested; the port preserves the mod's logical keys but old serialized ItemStack NBT does not automatically become new components.

## What to run next

```bash
./gradlew runClient
./gradlew runServer
```

On Windows:

```bat
gradlew.bat runClient
gradlew.bat runServer
```

The compile stage is no longer the blocker. The next useful failures are runtime/client/server issues from an actual launch and fresh-world test.
