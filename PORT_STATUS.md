# Mo' Creatures 1.21.1 NeoForge port — status

Target: Minecraft 1.21.1, NeoForge 21.1.219, Java 21.

This repository contains a substantial source-level port of the supplied 1.20.1 Forge project. The port now **passes a full `./gradlew clean build` directly from the repository source**, a dedicated-server startup smoke test, and a headless client startup smoke test under Xvfb on GitHub Actions. It is not yet claimed to be release-ready because gameplay behavior still needs in-game validation.

## Build status

- Full repository-source `./gradlew clean build`: **PASS**.
- Dedicated server `runServer` startup: **PASS**; fresh server reaches `Done`.
- Headless client `runClient` startup under Xvfb: **PASS** for a 90-second startup window with no Mo' Creatures mod-loading crash.
- Validated client-fix code SHA: `43790475da1e7bb06dfa84bcef9ec032bb7354c6`.
- Validated GitHub Actions run: `31334615915`.
- Uploaded `mocreatures-fixes.patch` was applied to the current port in source commit `679623e202b04773afa6b6c622135c59a39355ae`; CI validation is triggered from this status update.
- Pet hover nametags now preserve vanilla `CustomName` rendering, and Ogre smash uses a full radial block/entity blast while excluding only the source Ogre; source fix commit `a993af8a821e64192974572f4ea8728bf86b705e` passed a clean build and this status update triggers full client/server validation.
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
- Invalid empty NeoForge event-subscriber registrations were removed, including the client-only `MoCProxyClient` crash exposed by a real client launch.
- Wyvern Lair dimension/worldgen JSON formats and legacy loot/recipe formats were migrated to 1.21.1-compatible codecs.

## Validation completed

- Full Java/Gradle build with the real NeoForge classpath: pass.
- Dedicated server fresh-world startup: pass.
- Headless client startup under Xvfb for 90 seconds: pass; renderer/resource initialization proceeds without a Mo' Creatures mod-loading crash.
- The CI runner logs expected headless-Linux audio/narrator warnings (missing `flite` / OpenAL device); they are environment warnings, not Mo' Creatures loading failures, and the client continues through texture/resource initialization.
- `net.minecraftforge` imports: 0.
- `RegistryObject` references: 0.
- `SimpleChannel` / old Forge networking references: 0.
- direct legacy ItemStack NBT calls (`getTag`, `getOrCreateTag`, `setTag`, `hasTag`): 0.
- legacy model vertex `.vertex()` / `.endVertex()` calls: 0.
- legacy RGBA `ModelPart.render(...)` tails: 0.
- JSON parse validation: 701 files, 0 malformed.

## Known high-risk / unfinished validation areas

1. Real interactive gameplay still needs manual client testing; the headless smoke test validates startup/loading, not every screen, renderer or interaction path.
2. Real client ↔ dedicated-server packet flow should be exercised in multiplayer.
3. Runtime rendering should be checked for translucent entities/layers (ghost horses, wraiths, insect wings, golem effect layers, legacy big cats).
4. Worldgen/biome modifiers, spawn placement, Wyvern Lair terrain and portal travel need in-world behavioral testing.
5. Five custom mob drop routines currently set their manual looting bonus to zero; vanilla loot tables still work, but the mod-specific extra looting bonus needs a proper 1.21 enchantment-context implementation.
6. Data-component compatibility for old 1.20.1 worlds/items should be tested; the port preserves the mod's logical keys but old serialized ItemStack NBT does not automatically become new components.

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

The current blockers are no longer compilation or initial client/server mod loading. The next useful failures are gameplay/runtime behavior from an actual player session.
