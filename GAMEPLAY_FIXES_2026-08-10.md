# Gameplay fixes — 2026-08-10

Safety backup before these changes: `backup/pre-kitty-ai-taming-fix-2026-08-10` at `205f49e9510ada201b23a1032be93f93b57ccae7`.

Changes:
- Kitty no longer treats players as a fear/boogey source while retaining fear behavior for other valid entities.
- Kitty explicitly targets Mo' Creatures mice with its melee AI.
- MoC pet names now bridge the legacy synced `NAME_STR` field to vanilla 1.21 `CustomName` and `CustomNameVisible`, so tameable animals use the modern overhead nametag path.
- Clearing a MoC pet name also clears the vanilla custom name and visibility.

The source-changing workflow ran `./gradlew clean build` successfully before committing the fixes. This documentation commit intentionally triggers the normal repository CI so the saved source is also checked by the dedicated-server and headless-client smoke tests.
