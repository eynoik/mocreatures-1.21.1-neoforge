/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

public class MoCEntityAngler extends MoCEntitySmallFish {

    public MoCEntityAngler(EntityType<? extends MoCEntityAngler> type, Level world) {
        super(type, world);
        this.setTypeMoC(3);
    }

    @Override
    public ResourceLocation getTexture() {
        return MoCreatures.proxy.getModelTexture("smallfish_anglerfish.png");
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return MoCLootTables.ANGLERFISH;
    }
}
