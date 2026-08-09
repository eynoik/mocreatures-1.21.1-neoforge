/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

// TODO: Ents, Elephants, Horses, Horse Mobs, Snakes
@SuppressWarnings("removal")
public class MoCLootTables {
    // Ambient - All of these are empty, they're mainly for modpack developers
    public static final ResourceKey<LootTable> ANT = key("entities/ambient/ant");
    public static final ResourceKey<LootTable> BEE = key("entities/ambient/bee");
    public static final ResourceKey<LootTable> BUTTERFLY = key("entities/ambient/butterfly");
    public static final ResourceKey<LootTable> CRICKET = key("entities/ambient/cricket");
    public static final ResourceKey<LootTable> DRAGONFLY = key("entities/ambient/dragonfly");
    public static final ResourceKey<LootTable> FIREFLY = key("entities/ambient/firefly");
    public static final ResourceKey<LootTable> FLY = key("entities/ambient/fly");
    public static final ResourceKey<LootTable> GRASSHOPPER = key("entities/ambient/grasshopper");
    public static final ResourceKey<LootTable> MAGGOT = key("entities/ambient/maggot");
    public static final ResourceKey<LootTable> ROACH = key("entities/ambient/roach");
    public static final ResourceKey<LootTable> SNAIL = key("entities/ambient/snail");
    // Animals
    public static final ResourceKey<LootTable> BIRD = key("entities/bird");
    public static final ResourceKey<LootTable> BOAR = key("entities/boar");
    public static final ResourceKey<LootTable> BUNNY = key("entities/bunny");
    public static final ResourceKey<LootTable> CROCODILE = key("entities/crocodile");
    public static final ResourceKey<LootTable> DEER = key("entities/deer");
    public static final ResourceKey<LootTable> DUCK = key("entities/duck");
    public static final ResourceKey<LootTable> FOX = key("entities/fox");
    public static final ResourceKey<LootTable> GOAT = key("entities/goat");
    public static final ResourceKey<LootTable> KITTY = key("entities/kitty");
    public static final ResourceKey<LootTable> KOMODO_DRAGON = key("entities/komodo_dragon");
    public static final ResourceKey<LootTable> MOLE = key("entities/mole");
    public static final ResourceKey<LootTable> OSTRICH = key("entities/ostrich");
    public static final ResourceKey<LootTable> RACCOON = key("entities/raccoon");
    public static final ResourceKey<LootTable> TURKEY = key("entities/turkey");
    public static final ResourceKey<LootTable> TURTLE = key("entities/turtle");
    public static final ResourceKey<LootTable> WILD_WOLF = key("entities/wild_wolf");
    // Bears
    public static final ResourceKey<LootTable> BLACK_BEAR = key("entities/black_bear");
    public static final ResourceKey<LootTable> GRIZZLY_BEAR = key("entities/grizzly_bear");
    public static final ResourceKey<LootTable> PANDA_BEAR = key("entities/panda_bear");
    public static final ResourceKey<LootTable> POLAR_BEAR = key("entities/polar_bear");
    // Big Cats
    public static final ResourceKey<LootTable> LEOGER = key("entities/leoger");
    public static final ResourceKey<LootTable> LEOPARD = key("entities/leopard");
    public static final ResourceKey<LootTable> LIARD = key("entities/liard");
    public static final ResourceKey<LootTable> LIGER = key("entities/liger");
    public static final ResourceKey<LootTable> LION = key("entities/lion");
    public static final ResourceKey<LootTable> LITHER = key("entities/lither");
    public static final ResourceKey<LootTable> PANTHARD = key("entities/panthard");
    public static final ResourceKey<LootTable> PANTHER = key("entities/panther");
    public static final ResourceKey<LootTable> PANTHGER = key("entities/panthger");
    public static final ResourceKey<LootTable> TIGER = key("entities/tiger");
    // Dragons
    public static final ResourceKey<LootTable> WYVERN = key("entities/wyvern"); // Currently empty as a placeholder
    // Filch Lizard
    public static final ResourceKey<LootTable> FILCH_LIZARD = key("entities/filch_lizard/filch_lizard"); // Currently empty as a placeholder
    public static final ResourceKey<LootTable> FILCH_LIZARD_SPAWN = key("entities/filch_lizard/filch_lizard_spawn");
    public static final ResourceKey<LootTable> FILCH_LIZARD_STEAL = key("entities/filch_lizard/filch_lizard_steal");
    // Golems
    public static final ResourceKey<LootTable> BIG_GOLEM = key("entities/big_golem");
    public static final ResourceKey<LootTable> MINI_GOLEM = key("entities/mini_golem");
    // Manticores
    public static final ResourceKey<LootTable> DARK_MANTICORE = key("entities/dark_manticore");
    public static final ResourceKey<LootTable> FIRE_MANTICORE = key("entities/fire_manticore");
    public static final ResourceKey<LootTable> FROST_MANTICORE = key("entities/frost_manticore");
    public static final ResourceKey<LootTable> PLAIN_MANTICORE = key("entities/plain_manticore");
    public static final ResourceKey<LootTable> TOXIC_MANTICORE = key("entities/toxic_manticore");
    // Ogres
    public static final ResourceKey<LootTable> CAVE_OGRE = key("entities/cave_ogre");
    public static final ResourceKey<LootTable> FIRE_OGRE = key("entities/fire_ogre");
    public static final ResourceKey<LootTable> GREEN_OGRE = key("entities/green_ogre");
    // Rodents
    public static final ResourceKey<LootTable> HELL_RAT = key("entities/hell_rat");
    public static final ResourceKey<LootTable> MOUSE = key("entities/mouse");
    public static final ResourceKey<LootTable> RAT = key("entities/rat");
    // Sea Creatures
    public static final ResourceKey<LootTable> ANCHOVY = key("entities/aquatic/anchovy");
    public static final ResourceKey<LootTable> ANGELFISH = key("entities/aquatic/angelfish");
    public static final ResourceKey<LootTable> ANGLERFISH = key("entities/aquatic/anglerfish");
    public static final ResourceKey<LootTable> BASS = key("entities/aquatic/bass");
    public static final ResourceKey<LootTable> CLOWNFISH = key("entities/aquatic/clownfish");
    public static final ResourceKey<LootTable> COD = key("entities/aquatic/cod");
    public static final ResourceKey<LootTable> CRAB = key("entities/aquatic/crab");
    public static final ResourceKey<LootTable> DOLPHIN = key("entities/aquatic/dolphin");
    public static final ResourceKey<LootTable> FISHY = key("entities/aquatic/fishy");
    public static final ResourceKey<LootTable> GOLDFISH = key("entities/aquatic/goldfish");
    public static final ResourceKey<LootTable> HIPPO_TANG = key("entities/aquatic/hippo_tang");
    public static final ResourceKey<LootTable> JELLYFISH = key("entities/aquatic/jellyfish");
    public static final ResourceKey<LootTable> MANDARINFISH = key("entities/aquatic/mandarinfish");
    public static final ResourceKey<LootTable> MANTA_RAY = key("entities/aquatic/manta_ray"); // Currently empty as a placeholder
    public static final ResourceKey<LootTable> PIRANHA = key("entities/aquatic/piranha");
    public static final ResourceKey<LootTable> SALMON = key("entities/aquatic/salmon");
    public static final ResourceKey<LootTable> SHARK = key("entities/aquatic/shark");
    public static final ResourceKey<LootTable> STINGRAY = key("entities/aquatic/stingray"); // Currently empty as a placeholder
    // Scorpions
    public static final ResourceKey<LootTable> CAVE_SCORPION = key("entities/cave_scorpion");
    public static final ResourceKey<LootTable> DIRT_SCORPION = key("entities/dirt_scorpion");
    public static final ResourceKey<LootTable> FIRE_SCORPION = key("entities/fire_scorpion");
    public static final ResourceKey<LootTable> FROST_SCORPION = key("entities/frost_scorpion");
    public static final ResourceKey<LootTable> UNDEAD_SCORPION = key("entities/undead_scorpion");
    // Undead
    public static final ResourceKey<LootTable> FLAME_WRAITH = key("entities/flame_wraith");
    public static final ResourceKey<LootTable> SILVER_SKELETON = key("entities/silver_skeleton");
    public static final ResourceKey<LootTable> WRAITH = key("entities/wraith");
    // Werewolves
    public static final ResourceKey<LootTable> WEREHUMAN = key("entities/werehuman");
    public static final ResourceKey<LootTable> WEREWOLF = key("entities/werewolf");

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, path));
    }
}
