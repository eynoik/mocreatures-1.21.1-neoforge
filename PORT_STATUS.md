# Mo' Creatures 1.21.1 NeoForge port — status

Target: Minecraft 1.21.1, NeoForge 21.1.219, Java 21.

This repository contains a substantial source-level port of the supplied 1.20.1 Forge project. The checked-in source now **passes a full `./gradlew clean build`** and a dedicated-server `runServer` smoke test on GitHub Actions. The server reaches `Done` on a fresh world and the migrated Mo' Creatures loot tables / recipes load without parser errors. It is still not claimed to be release-ready because the client and real gameplay/rendering behavior need in-game validation.

## Build/runtime status

- Full repository-source `./gradlew clean build`: **PASS**.
- Dedicated NeoForge server launch (`./gradlew runServer`): **PASS** — fresh world reaches `Done`.
- Mo' Creatures loot-table / recipe parser errors during server load: **0 in the validated smoke-test log**.
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
- Dedicated-server classloading fixed so the common mod entrypoint does not directly load the client proxy.
- Invalid empty event-bus subscribers removed from dormant/registration-only classes.
- Common tag namespace moved from `forge:` to `c:` where ported and 1.21 singular datapack paths were applied.
- Recipe JSON results migrated to 1.21 format.
- 80 remaining recipe/loot JSON files migrated away from removed 1.20-era forms, including legacy looting conditions/functions, `set_nbt`, `alternative`, old tool predicates and `minecraft:grass` item IDs.
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
- Wyvern Lair dimension/worldgen JSON formats were migrated far enough for the dedicated server to load the datapack and create a fresh world.

## Validation completed

- Full Java/Gradle build with the real NeoForge classpath: pass.
- Dedicated server common-side classloading: pass through fresh-world startup.
- Fresh-world server startup: pass; server reaches `Done`.
- Recipe manager loads successfully after the data migration.
- Mo' Creatures loot/recipe parse-error scan of the server log: clean.
- Wyvern Lair biomes and biome modifier processing are reached during server startup without fatal registry/datapack errors.
- `net.minecraftforge` imports: 0.
- `RegistryObject` references: 0.
- `SimpleChannel` / old Forge networking references: 0.
- direct legacy ItemStack NBT calls (`getTag`, `getOrCreateTag`, `setTag`, `hasTag`): 0.
- legacy model vertex `.vertex()` / `.endVertex()` calls: 0.
- legacy RGBA `ModelPart.render(...)` tails: 0.

## Known high-risk / unfinished validation areas

1. Client launch and gameplay have not yet been fully runtime-tested on 1.21.1 NeoForge.
2. Clientbound networking still needs an actual client↔dedicated-server connection test; server-only classloading is now validated.
3. Runtime rendering should be checked for translucent entities/layers (ghost horses, wraiths, insect wings, golem effect layers, legacy big cats).
4. Texture-stitch/client registration, custom render types and client event signatures need confirmation by an actual client launch.
5. Wyvern Lair/worldgen data now loads on a fresh dedicated server, but actual terrain/feature placement and portal travel still need in-world testing.
6. Five custom mob drop routines currently set their manual looting bonus to zero; JSON loot tables preserve Looting behavior after migration, but those separate code-driven extra drops still need a proper 1.21 enchantment-context implementation.
7. Data-component compatibility for old 1.20.1 worlds/items should be tested; the port preserves the mod's logical keys but old serialized ItemStack NBT does not automatically become new components.
8. NeoForge still emits deprecation/legacy-tag warnings during development startup; they are non-fatal but should be cleaned before calling the port polished.

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

The compile and initial dedicated-server startup stages are no longer blockers. The next useful failures should come from client startup and real gameplay tests: rendering, packets, entity AI/interactions, portals and worldgen placement.
