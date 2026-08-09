/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.compat.datafixes;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.nbt.CompoundTag;

public class EntityIDFixer {
/**
     * Called manually when loading legacy entities to remap old IDs.
     */
    public CompoundTag fixTagCompound(CompoundTag compound) {
        String entityId = compound.getString("id");
        if (entityId.equals(MoCConstants.MOD_PREFIX + "scorpion")) {
            int entityType = compound.getInt("TypeInt");
            switch (entityType) {
                case 2:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "cavescorpion");
                    break;
                case 3:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "firescorpion");
                    break;
                case 4:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "frostscorpion");
                    break;
                default:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "dirtscorpion");
            }
            compound.putInt("TypeInt", 1);
        }

        if (entityId.equals(MoCConstants.MOD_PREFIX + "manticore")) {
            int entityType = compound.getInt("TypeInt");
            switch (entityType) {
                case 2:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "darkmanticore");
                    break;
                case 3:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "frostmanticore");
                    break;
                case 4:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "toxicmanticore");
                    break;
                default:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "firemanticore");
            }
            compound.putInt("TypeInt", 1);
        }

        return compound;
    }
}
