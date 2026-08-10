# Gameplay fix validation — 2026-08-10

Source fix commit: `34e77952280560a4352f72dd2548cde477ec3f44`.

This round fixes two user-reported runtime regressions:

- MoC's base renderer no longer returns `false` unconditionally from `shouldShowName`; named MoC entities under the client crosshair are allowed through the vanilla hover-name render pass, using synchronized `NAME_STR` as the displayed pet name.
- Ogre destroy-blast entity damage now uses a radial LivingEntity query with radius `2 * strength`, no line-of-sight exposure multiplier, excludes only the Ogre producing the blast, damages players/animals/monsters/other Ogres, and applies radial knockback.

The source fix already passed `./gradlew clean build` on the isolated fix branch. This commit exists to trigger the full repository CI (build + dedicated server + headless client) on the main working branch.
