# Mo' Creatures: Aura Edition — Changelog

## v1.0.2 — 2026-08-12

- Fixed Ogre destroy blasts so the blast center is the vertical center of the Ogre's actual hitbox instead of a fixed `Y + 1` point near its feet.
- Added `config/MoCreatures/MoCCombatScaling.cfg` with global `HealthMultiplier` and `DamageMultiplier` settings.
- Health scaling uses a stable permanent max-health modifier so it does not stack again when entities/chunks reload, while preserving the entity's current health percentage.
- Damage scaling is applied to incoming damage caused by Mo' Creatures entities, so special attacks are scaled as well as normal melee damage.
- Bumped the Aura Edition mod version to `1.21.1-1.0.2-neoforge-port`.

## v1.0.1 — 2026-08-11

- Fixed human-form werewolf audio fallbacks: vanilla player hurt/death sounds are now used when legacy werehuman sounds are disabled.
- Human-form werewolves now have a subtle vanilla player breathing ambient instead of being completely silent while idle.
- Fixed butterfly visual jitter by replacing render-frame-mutated bobbing state with deterministic tick + partial-tick interpolation.
- Bumped the Aura Edition mod version to `1.21.1-1.0.1-neoforge-port`.

## v1.0.0 — 2026-08-10

- Initial public Aura Edition pre-release for Minecraft 1.21.1 NeoForge.
- Includes the current NeoForge port fixes and Wyvern Lair feature-order-cycle/world-generation freeze fix.
