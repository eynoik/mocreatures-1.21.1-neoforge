package drzhark.mocreatures;

import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.List;

@GameTestHolder(MoCConstants.MOD_ID)
@PrefixGameTestTemplate(false)
public class MoCNameTests {
    private static List<EntityType<? extends Mob>> types() {
        // All four base naming implementations, including separate renderers.
        return List.of(MoCEntities.BUNNY.get(), MoCEntities.BUTTERFLY.get(),
                MoCEntities.SHARK.get(), MoCEntities.RAT.get());
    }

    @GameTest(template = "empty")
    public static void vanillaNameSurvivesReload(GameTestHelper helper) {
        for (var type : types()) {
            Mob mob = type.create(helper.getLevel());
            ((IMoCEntity) mob).setPetName("Old pet name");
            Component name = Component.literal("Żółw 😈").withStyle(ChatFormatting.GOLD);
            mob.setCustomName(name);
            mob.setCustomNameVisible(true);
            helper.assertTrue(((IMoCEntity) mob).getPetName().equals(name.getString()), "Nametag must update pet name: " + type);
            CompoundTag saved = mob.saveWithoutId(new CompoundTag());
            // Reproduce old saves where CustomName and the legacy field disagree.
            saved.putString("Name", "stale");
            saved.getCompound("MoCData").putString("Name", "stale");
            Mob loaded = type.create(helper.getLevel());
            loaded.load(saved);
            helper.assertTrue(name.equals(loaded.getCustomName()), "Styled vanilla name lost on reload: " + type);
            helper.assertTrue(loaded.isCustomNameVisible(), "Explicit name visibility lost: " + type);
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void legacyPetNameMigrates(GameTestHelper helper) {
        for (var type : types()) {
            Mob mob = type.create(helper.getLevel());
            CompoundTag saved = mob.saveWithoutId(new CompoundTag());
            saved.remove("CustomName");
            saved.putString("Name", "Legacy pet");
            CompoundTag data = saved.getCompound("MoCData");
            data.putString("Name", "Legacy pet");
            saved.put("MoCData", data);
            Mob loaded = type.create(helper.getLevel());
            loaded.load(saved);
            helper.assertTrue(Component.literal("Legacy pet").equals(loaded.getCustomName()), "Legacy name not migrated: " + type);
            helper.assertTrue(((IMoCEntity) loaded).getPetName().equals("Legacy pet"), "Legacy pet name mismatch: " + type);
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void removingNameDoesNotResurrectIt(GameTestHelper helper) {
        for (var type : types()) {
            Mob mob = type.create(helper.getLevel());
            ((IMoCEntity) mob).setPetName("Temporary");
            helper.assertTrue(Component.literal("Temporary").equals(mob.getCustomName()), "Pet GUI name not synchronized: " + type);
            mob.setCustomName(null);
            Mob loaded = type.create(helper.getLevel());
            loaded.load(mob.saveWithoutId(new CompoundTag()));
            helper.assertTrue(loaded.getCustomName() == null && ((IMoCEntity) loaded).getPetName().isEmpty(), "Removed name resurrected: " + type);
        }
        helper.succeed();
    }
}
