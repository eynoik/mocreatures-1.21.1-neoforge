package drzhark.mocreatures.init;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.item.*;
import drzhark.mocreatures.util.MoCArmorMaterial;
import drzhark.mocreatures.util.MoCItemTier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MoCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MoCConstants.MOD_ID);

    // Misc
    public static final Supplier<Item> HORSE_GUIDE = ITEMS.register("horseguide", () -> new ItemHorseGuide(new Properties().stacksTo(1)));
    public static final Supplier<Item> RECORD_SHUFFLE = ITEMS.register("recordshuffle", () -> new MoCItemRecord(15, MoCSoundEvents.ITEM_RECORD_SHUFFLING::get, new Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final Supplier<Item> HORSE_SADDLE = ITEMS.register("horsesaddle", () -> new MoCItemHorseSaddle(new Properties()));
    public static final Supplier<Item> SHARK_TEETH = ITEMS.register("sharkteeth", () -> new MoCItem(new Properties()));
    public static final Supplier<Item> HAYSTACK = ITEMS.register("haystack", () -> new MoCItem(new Properties().stacksTo(16)));

    public static final Supplier<Item> SUGAR_LUMP = ITEMS.register("sugarlump", () -> new MoCItemFood.Builder(new Properties(), 1, 0.1F, false, 12)
            .setPotionEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0), 0.15F)
            .setPotionEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 0), 1.0F).build());

    public static final Supplier<Item> MOC_EGG = ITEMS.register("mocegg", () -> new MoCItemEgg(new Properties()));
    public static final Supplier<Item> BIGCAT_CLAW = ITEMS.register("bigcatclaw", () -> new MoCItem(new Properties()));
    public static final Supplier<Item> WHIP = ITEMS.register("whip", () -> new MoCItemWhip(new Properties()));
    public static final Supplier<Item> STAFF_PORTAL = ITEMS.register("staffportal", () -> new ItemStaffPortal(new Properties()));
    public static final Supplier<Item> MEDALLION = ITEMS.register("medallion", () -> new MoCItem(new Properties()));
    public static final Supplier<Item>[] KITTYBED = new Supplier[16];
    static {
        for (int i = 0; i < 16; i++) {
            DyeColor dye = DyeColor.byId(i);
            String colorName = dye.getName();
            int finalI = i;
            KITTYBED[i] = ITEMS.register("kittybed_" + colorName, () -> new MoCItemKittyBed(new Item.Properties(), finalI));
        }
    }
    public static final Supplier<Item> LITTER_BOX = ITEMS.register("kittylitter", () -> new MoCItemLitterBox(new Properties()));
    public static final Supplier<Item> WOOL_BALL = ITEMS.register("woolball", () -> new MoCItem(new Properties()));
    public static final Supplier<Item> PET_FOOD = ITEMS.register("petfood", () -> new MoCItem(new Properties()));
    public static final Supplier<Item> REPTILE_HIDE = ITEMS.register("reptilehide", () -> new MoCItem(new Properties()));
    public static final Supplier<Item> FUR = ITEMS.register("fur", () -> new MoCItem(new Properties()));
    public static final Supplier<Item> ESSENCE_DARKNESS = ITEMS.register("essencedarkness", () -> new MoCItem(new Properties()));
    public static final Supplier<Item> ESSENCE_FIRE = ITEMS.register("essencefire", () -> new MoCItem(new Properties()));
    public static final Supplier<Item> ESSENCE_UNDEAD = ITEMS.register("essenceundead", () -> new MoCItem(new Properties()));
    public static final Supplier<Item> ESSENCE_LIGHT = ITEMS.register("essencelight", () -> new MoCItem(new Properties()));

    // Materials & Drops
    public static final Supplier<Item> HEARTDARKNESS = ITEMS.register("heartdarkness", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> HEARTFIRE = ITEMS.register("heartfire", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> HEARTUNDEAD = ITEMS.register("heartundead", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> UNICORNHORN = ITEMS.register("unicornhorn", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> HORSEARMORCRYSTAL = ITEMS.register("horsearmorcrystal", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> ANIMALHIDE = ITEMS.register("hide", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> CHITINCAVE = ITEMS.register("chitinblack", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> CHITINFROST = ITEMS.register("chitinfrost", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> CHITINNETHER = ITEMS.register("chitinnether", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> CHITINUNDEAD = ITEMS.register("chitinundead", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> CHITIN = ITEMS.register("chitin", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> TUSKSWOOD = ITEMS.register("tuskswood", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> TUSKSIRON = ITEMS.register("tusksiron", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> TUSKSDIAMOND = ITEMS.register("tusksdiamond", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> ELEPHANTHARNESS = ITEMS.register("elephantharness", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> ELEPHANTCHEST = ITEMS.register("elephantchest", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> ELEPHANTGARMENT = ITEMS.register("elephantgarment", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> ELEPHANTHOWDAH = ITEMS.register("elephanthowdah", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> MAMMOTHPLATFORM = ITEMS.register("mammothplatform", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> SCROLLFREEDOM = ITEMS.register("scrolloffreedom", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> SCROLLOFSALE = ITEMS.register("scrollofsale", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> SCROLLOFOWNER = ITEMS.register("scrollofowner", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> ANCIENTSILVERSCRAP = ITEMS.register("ancientsilverscrap", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> ANCIENTSILVERINGOT = ITEMS.register("ancientsilveringot", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> ANCIENTSILVERNUGGET = ITEMS.register("ancientsilvernugget", () -> new MoCItem(new Item.Properties()));
    public static final Supplier<Item> FIRESTONECHUNK = ITEMS.register("firestonechunk", () -> new MoCItem(new Item.Properties()));

    // Amulets
    public static final Supplier<Item> AMULET_BONE = ITEMS.register("amuletbone", () -> new MoCItemHorseAmulet(new Item.Properties()));
    public static final Supplier<Item> AMULET_BONE_FULL = ITEMS.register("amuletbonefull", () -> new MoCItemHorseAmulet(new Item.Properties()));
    public static final Supplier<Item> AMULET_GHOST = ITEMS.register("amuletghost", () -> new MoCItemHorseAmulet(new Item.Properties()));
    public static final Supplier<Item> AMULET_GHOST_FULL = ITEMS.register("amuletghostfull", () -> new MoCItemHorseAmulet(new Item.Properties()));
    public static final Supplier<Item> AMULET_FAIRY = ITEMS.register("amuletfairy", () -> new MoCItemHorseAmulet(new Item.Properties()));
    public static final Supplier<Item> AMULET_FAIRY_FULL = ITEMS.register("amuletfairyfull", () -> new MoCItemHorseAmulet(new Item.Properties()));
    public static final Supplier<Item> AMULET_PEGASUS = ITEMS.register("amuletpegasus", () -> new MoCItemHorseAmulet(new Item.Properties()));
    public static final Supplier<Item> AMULET_PEGASUS_FULL = ITEMS.register("amuletpegasusfull", () -> new MoCItemHorseAmulet(new Item.Properties()));
    public static final Supplier<Item> FISH_NET = ITEMS.register("fishnet", () -> new MoCItemPetAmulet(new Item.Properties()));
    public static final Supplier<Item> FISH_NET_FULL = ITEMS.register("fishnetfull", () -> new MoCItemPetAmulet(new Item.Properties()));
    public static final Supplier<Item> PET_AMULET = ITEMS.register("petamulet", () -> new MoCItemPetAmulet(new Item.Properties(), 1));
    public static final Supplier<Item> PET_AMULET_FULL = ITEMS.register("petamuletfull", () -> new MoCItemPetAmulet(new Item.Properties(), 1));

    // Food
    public static final Supplier<Item> COOKED_TURKEY = ITEMS.register("turkeycooked", () -> new MoCItemFood.Builder(new Item.Properties(), 7, 0.8F, true).build());
    public static final Supplier<Item> CRAB_RAW = ITEMS.register("crabraw", () -> new MoCItemFood.Builder(new Item.Properties(), 2, 0.1F, true).setPotionEffect(new MobEffectInstance(MobEffects.HUNGER, 30 * 20, 0), 0.8F).build());
    public static final Supplier<Item> CRAB_COOKED = ITEMS.register("crabcooked", () -> new MoCItemFood.Builder(new Item.Properties(), 4, 0.6F, true).build());
    public static final Supplier<Item> DUCK_COOKED = ITEMS.register("duckcooked", () -> new MoCItemFood.Builder(new Item.Properties(), 6, 0.7F, true).build());
    public static final Supplier<Item> DUCK_RAW = ITEMS.register("duckraw", () -> new MoCItemFood.Builder(new Item.Properties(), 2, 0.4F, true).build());
    public static final Supplier<Item> MYSTIC_PEAR = ITEMS.register("mysticpear", () -> new MoCItemFood.Builder(new Item.Properties(), 4, 0.8F, false, 16).setAlwaysEdible().setPotionEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10 * 20, 1), 1.0F).setPotionEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10 * 20, 1), 1.0F).build());
    public static final Supplier<Item> OMELET = ITEMS.register("omelet", () -> new MoCItemFood.Builder(new Item.Properties(), 3, 0.5F, false).build());
    public static final Supplier<Item> OSTRICH_RAW = ITEMS.register("ostrichraw", () -> new MoCItemFood.Builder(new Item.Properties(), 3, 0.4F, true).setPotionEffect(new MobEffectInstance(MobEffects.HUNGER, 30 * 20, 0), 0.8F).build());
    public static final Supplier<Item> OSTRICH_COOKED = ITEMS.register("ostrichcooked", () -> new MoCItemFood.Builder(new Item.Properties(), 7, 0.8F, true).build());
    public static final Supplier<Item> RAT_BURGER = ITEMS.register("ratburger", () -> new MoCItemFood.Builder(new Item.Properties(), 9, 0.5F, false).build());
    public static final Supplier<Item> RAT_COOKED = ITEMS.register("ratcooked", () -> new MoCItemFood.Builder(new Item.Properties(), 4, 0.5F, true).build());
    public static final Supplier<Item> RAT_RAW = ITEMS.register("ratraw", () -> new MoCItemFood.Builder(new Item.Properties(), 2, 0.1F, true).setPotionEffect(new MobEffectInstance(MobEffects.HUNGER, 30 * 20, 0), 0.8F).build());
    public static final Supplier<Item> RAW_TURKEY = ITEMS.register("turkeyraw", () -> new MoCItemFood.Builder(new Item.Properties(), 3, 0.4F, true).setPotionEffect(new MobEffectInstance(MobEffects.HUNGER, 30 * 20, 0), 0.8F).build());
    public static final Supplier<Item> TURTLE_COOKED = ITEMS.register("turtlecooked", () -> new MoCItemFood.Builder(new Item.Properties(), 6, 0.7F, true).build());
    public static final Supplier<Item> TURTLE_RAW = ITEMS.register("turtleraw", () -> new MoCItemFood.Builder(new Item.Properties(), 2, 0.2F, true).build());
    public static final Supplier<Item> TURTLE_SOUP = ITEMS.register("turtlesoup", () -> new MoCItemTurtleSoup.Builder(new Item.Properties(), 8, 0.8F, false).build());
    public static final Supplier<Item> VENISON_COOKED = ITEMS.register("venisoncooked", () -> new MoCItemFood.Builder(new Item.Properties(), 8, 0.9F, true).build());
    public static final Supplier<Item> VENISON_RAW = ITEMS.register("venisonraw", () -> new MoCItemFood.Builder(new Item.Properties(), 3, 0.4F, true).build());

    // Weapons
    public static final Supplier<Item> NUNCHAKU = ITEMS.register("nunchaku", () -> new MoCItemSword(new Item.Properties(), Tiers.IRON));
    public static final Supplier<Item> SAI = ITEMS.register("sai", () -> new MoCItemSword(new Item.Properties(), Tiers.IRON));
    public static final Supplier<Item> BO = ITEMS.register("bo", () -> new MoCItemSword(new Item.Properties(), Tiers.IRON));
    public static final Supplier<Item> KATANA = ITEMS.register("katana", () -> new MoCItemSword(new Item.Properties(), Tiers.IRON));
    public static final Supplier<Item> SHARKSWORD = ITEMS.register("sharksword", () -> new MoCItemSword(new Item.Properties(), MoCItemTier.SHARK));
    public static final Supplier<Item> SHARKAXE = ITEMS.register("sharkaxe", () -> new MoCItemAxe(new Item.Properties(), MoCItemTier.SHARK, 9.5F, 1.0F));
    public static final Supplier<Item> SILVERSWORD = ITEMS.register("silversword", () -> new MoCItemSword(new Item.Properties(), MoCItemTier.SILVER));
    public static final Supplier<Item> SILVERAXE = ITEMS.register("silveraxe", () -> new MoCItemAxe(new Item.Properties(), MoCItemTier.SILVER, 10.0F, 1.1F));
    public static final Supplier<Item> SCORPSWORDCAVE = ITEMS.register("scorpswordcave", () -> new MoCItemSword(new Item.Properties(), MoCItemTier.SCORPC, 4));
    public static final Supplier<Item> SCORPAXECAVE = ITEMS.register("scorpaxecave", () -> new MoCItemAxe(new Item.Properties(), MoCItemTier.SCORPC, 9.5F, 1.0F, 4));
    public static final Supplier<Item> SCORPSWORDFROST = ITEMS.register("scorpswordfrost", () -> new MoCItemSword(new Item.Properties(), MoCItemTier.SCORPF, 2));
    public static final Supplier<Item> SCORPAXEFROST = ITEMS.register("scorpaxefrost", () -> new MoCItemAxe(new Item.Properties(), MoCItemTier.SCORPF, 9.5F, 1.0F, 2));
    public static final Supplier<Item> SCORPSWORDNETHER = ITEMS.register("scorpswordnether", () -> new MoCItemSword(new Item.Properties(), MoCItemTier.SCORPN, 3));
    public static final Supplier<Item> SCORPAXENETHER = ITEMS.register("scorpaxenether", () -> new MoCItemAxe(new Item.Properties(), MoCItemTier.SCORPN, 9.5F, 1.0F, 3));
    public static final Supplier<Item> SCORPSWORDDIRT = ITEMS.register("scorpsworddirt", () -> new MoCItemSword(new Item.Properties(), MoCItemTier.SCORPD, 1));
    public static final Supplier<Item> SCORPAXEDIRT = ITEMS.register("scorpaxedirt", () -> new MoCItemAxe(new Item.Properties(), MoCItemTier.SCORPD, 9.5F, 1.0F, 1));
    public static final Supplier<Item> SCORPSWORDUNDEAD = ITEMS.register("scorpswordundead", () -> new MoCItemSword(new Item.Properties(), MoCItemTier.SCORPU, 5));
    public static final Supplier<Item> SCORPAXEUNDEAD = ITEMS.register("scorpaxeundead", () -> new MoCItemAxe(new Item.Properties(), MoCItemTier.SCORPU, 9.5F, 1.0F, 5));
    public static final Supplier<Item> SCORPSTINGCAVE = ITEMS.register("scorpstingcave", () -> new MoCItemWeapon(new Item.Properties(), MoCItemTier.STING, 4));
    public static final Supplier<Item> SCORPSTINGFROST = ITEMS.register("scorpstingfrost", () -> new MoCItemWeapon(new Item.Properties(), MoCItemTier.STING, 2));
    public static final Supplier<Item> SCORPSTINGNETHER = ITEMS.register("scorpstingnether", () -> new MoCItemWeapon(new Item.Properties(), MoCItemTier.STING, 3));
    public static final Supplier<Item> SCORPSTINGDIRT = ITEMS.register("scorpstingdirt", () -> new MoCItemWeapon(new Item.Properties(), MoCItemTier.STING, 1));
    public static final Supplier<Item> SCORPSTINGUNDEAD = ITEMS.register("scorpstingundead", () -> new MoCItemWeapon(new Item.Properties(), MoCItemTier.STING, 5));

    // Armor
    public static final Supplier<Item> PLATE_CROC = ITEMS.register("reptileplate", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.CROC, ArmorItem.Type.CHESTPLATE));
    public static final Supplier<Item> HELMET_CROC = ITEMS.register("reptilehelmet", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.CROC, ArmorItem.Type.HELMET));
    public static final Supplier<Item> LEGS_CROC = ITEMS.register("reptilelegs", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.CROC, ArmorItem.Type.LEGGINGS));
    public static final Supplier<Item> BOOTS_CROC = ITEMS.register("reptileboots", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.CROC, ArmorItem.Type.BOOTS));
    public static final Supplier<Item> HELMET_SCORP_D = ITEMS.register("scorphelmetdirt", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPD, ArmorItem.Type.HELMET));
    public static final Supplier<Item> PLATE_SCORP_D = ITEMS.register("scorpplatedirt", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPD, ArmorItem.Type.CHESTPLATE));
    public static final Supplier<Item> LEGS_SCORP_D = ITEMS.register("scorplegsdirt", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPD, ArmorItem.Type.LEGGINGS));
    public static final Supplier<Item> BOOTS_SCORP_D = ITEMS.register("scorpbootsdirt", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPD, ArmorItem.Type.BOOTS));
    public static final Supplier<Item> HELMET_SCORP_F = ITEMS.register("scorphelmetfrost", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPF, ArmorItem.Type.HELMET));
    public static final Supplier<Item> PLATE_SCORP_F = ITEMS.register("scorpplatefrost", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPF, ArmorItem.Type.CHESTPLATE));
    public static final Supplier<Item> LEGS_SCORP_F = ITEMS.register("scorplegsfrost", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPF, ArmorItem.Type.LEGGINGS));
    public static final Supplier<Item> BOOTS_SCORP_F = ITEMS.register("scorpbootsfrost", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPF, ArmorItem.Type.BOOTS));
    public static final Supplier<Item> HELMET_SCORP_N = ITEMS.register("scorphelmetnether", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPN, ArmorItem.Type.HELMET));
    public static final Supplier<Item> PLATE_SCORP_N = ITEMS.register("scorpplatenether", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPN, ArmorItem.Type.CHESTPLATE));
    public static final Supplier<Item> LEGS_SCORP_N = ITEMS.register("scorplegsnether", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPN, ArmorItem.Type.LEGGINGS));
    public static final Supplier<Item> BOOTS_SCORP_N = ITEMS.register("scorpbootsnether", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPN, ArmorItem.Type.BOOTS));
    public static final Supplier<Item> HELMET_SCORP_C = ITEMS.register("scorphelmetcave", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPC, ArmorItem.Type.HELMET));
    public static final Supplier<Item> PLATE_SCORP_C = ITEMS.register("scorpplatecave", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPC, ArmorItem.Type.CHESTPLATE));
    public static final Supplier<Item> LEGS_SCORP_C = ITEMS.register("scorplegscave", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPC, ArmorItem.Type.LEGGINGS));
    public static final Supplier<Item> BOOTS_SCORP_C = ITEMS.register("scorpbootscave", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPC, ArmorItem.Type.BOOTS));
    public static final Supplier<Item> HELMET_SCORP_U = ITEMS.register("scorphelmetundead", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPU, ArmorItem.Type.HELMET));
    public static final Supplier<Item> PLATE_SCORP_U = ITEMS.register("scorpplateundead", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPU, ArmorItem.Type.CHESTPLATE));
    public static final Supplier<Item> LEGS_SCORP_U = ITEMS.register("scorplegsundead", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPU, ArmorItem.Type.LEGGINGS));
    public static final Supplier<Item> BOOTS_SCORP_U = ITEMS.register("scorpbootsundead", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SCORPU, ArmorItem.Type.BOOTS));
    public static final Supplier<Item> HELMET_FUR = ITEMS.register("furhelmet", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.FUR, ArmorItem.Type.HELMET));
    public static final Supplier<Item> CHEST_FUR = ITEMS.register("furchest", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.FUR, ArmorItem.Type.CHESTPLATE));
    public static final Supplier<Item> LEGS_FUR = ITEMS.register("furlegs", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.FUR, ArmorItem.Type.LEGGINGS));
    public static final Supplier<Item> BOOTS_FUR = ITEMS.register("furboots", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.FUR, ArmorItem.Type.BOOTS));
    public static final Supplier<Item> HELMET_HIDE = ITEMS.register("hidehelmet", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.HIDE, ArmorItem.Type.HELMET));
    public static final Supplier<Item> CHEST_HIDE = ITEMS.register("hidechest", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.HIDE, ArmorItem.Type.CHESTPLATE));
    public static final Supplier<Item> LEGS_HIDE = ITEMS.register("hidelegs", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.HIDE, ArmorItem.Type.LEGGINGS));
    public static final Supplier<Item> BOOTS_HIDE = ITEMS.register("hideboots", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.HIDE, ArmorItem.Type.BOOTS));
    public static final Supplier<Item> HELMET_SILVER = ITEMS.register("ancient_silver_helmet", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SILVER, ArmorItem.Type.HELMET));
    public static final Supplier<Item> CHEST_SILVER = ITEMS.register("ancient_silver_chestplate", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SILVER, ArmorItem.Type.CHESTPLATE));
    public static final Supplier<Item> LEGS_SILVER = ITEMS.register("ancient_silver_leggings", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SILVER, ArmorItem.Type.LEGGINGS));
    public static final Supplier<Item> BOOTS_SILVER = ITEMS.register("ancient_silver_boots", () -> new MoCItemArmor(new Item.Properties(), MoCArmorMaterial.SILVER, ArmorItem.Type.BOOTS));
    
}
    


//@SubscribeEvent
//public static void registerCreativeTabs(CreativeModeTabEvent.Register event) {
//    event.registerCreativeModeTab(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "mocreatures_tab"),
//            builder -> builder.icon(() -> new ItemStack(HORSE_GUIDE.get()))
//                    .title(Component.translatable("itemGroup.mocreatures"))
//                    .displayItems((parameters, output) -> {
//                        output.accept(HORSE_GUIDE.get());
//                        output.accept(SHARK_TEETH.get());
//                        // Add others here
//                    }));
//} Kitty beds (as array or individual entries)