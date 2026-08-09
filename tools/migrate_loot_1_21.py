from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path("src/main/resources/data/mocreatures")


def migrate(node):
    changed = False

    if isinstance(node, list):
        out = []
        for value in node:
            value, did_change = migrate(value)
            changed |= did_change
            out.append(value)
        return out, changed

    if not isinstance(node, dict):
        if node == "minecraft:grass":
            return "minecraft:short_grass", True
        return node, False

    out = dict(node)

    function = out.get("function")
    if function in ("looting_enchant", "minecraft:looting_enchant"):
        out["function"] = "minecraft:enchanted_count_increase"
        out["enchantment"] = "minecraft:looting"
        changed = True
    elif function in ("set_nbt", "minecraft:set_nbt"):
        tag = out.get("tag")
        match = re.fullmatch(r"\{EggType:(-?\d+)\}", tag or "")
        if not match:
            raise ValueError(f"Unsupported legacy set_nbt payload: {tag!r}")
        out["function"] = "minecraft:set_custom_data"
        out["tag"] = {"EggType": int(match.group(1))}
        changed = True

    condition = out.get("condition")
    if condition in ("random_chance_with_looting", "minecraft:random_chance_with_looting"):
        base = float(out.pop("chance"))
        per_level = float(out.pop("looting_multiplier"))
        out["condition"] = "minecraft:random_chance_with_enchanted_bonus"
        out["enchantment"] = "minecraft:looting"
        out["unenchanted_chance"] = base
        out["enchanted_chance"] = {
            "type": "linear",
            "base": base + per_level,
            "per_level_above_first": per_level,
        }
        changed = True
    elif condition in ("alternative", "minecraft:alternative"):
        out["condition"] = "minecraft:any_of"
        changed = True

    if out.get("condition") in ("match_tool", "minecraft:match_tool") and isinstance(out.get("predicate"), dict):
        predicate = dict(out["predicate"])
        predicate_changed = False

        if "item" in predicate:
            predicate["items"] = [predicate.pop("item")]
            predicate_changed = True

        if "enchantments" in predicate:
            enchantments = []
            for legacy in predicate.pop("enchantments"):
                modern = dict(legacy)
                if "enchantment" in modern:
                    modern["enchantments"] = modern.pop("enchantment")
                enchantments.append(modern)
            predicates = dict(predicate.get("predicates", {}))
            predicates["minecraft:enchantments"] = enchantments
            predicate["predicates"] = predicates
            predicate_changed = True

        if predicate_changed:
            out["predicate"] = predicate
            changed = True

    for key, value in list(out.items()):
        value, did_change = migrate(value)
        out[key] = value
        changed |= did_change

    return out, changed


def main():
    changed_files = []
    for path in ROOT.rglob("*.json"):
        data = json.loads(path.read_text(encoding="utf-8"))
        data, changed = migrate(data)
        if changed:
            path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
            changed_files.append(path)

    print(f"Migrated {len(changed_files)} JSON files to Minecraft/NeoForge 1.21.1 loot/recipe formats")
    for path in changed_files:
        print(path)


if __name__ == "__main__":
    main()
