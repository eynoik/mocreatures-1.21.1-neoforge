/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.entity.tameable.MoCPetData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;


@SuppressWarnings("removal")
public class CommandMoCTP {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("moctp")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("petId", IntegerArgumentType.integer(0))
                .executes(context -> {
                    int petId = IntegerArgumentType.getInteger(context, "petId");
                    return teleportPet(context.getSource(), petId, context.getSource().getPlayerOrException());
                })
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> {
                        int petId = IntegerArgumentType.getInteger(context, "petId");
                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                        return teleportPet(context.getSource(), petId, player);
                    })
                )
            )
        );
    }
    
    private static int teleportPet(CommandSourceStack source, int petId, ServerPlayer player) {
        // Search for tamed entity in mocreatures.dat
        MoCPetData ownerPetData = MoCreatures.instance.mapData.getPetData(player.getUUID());
        if (ownerPetData != null) {
            for (int i = 0; i < ownerPetData.getTamedList().size(); i++) {
                CompoundTag nbt = ownerPetData.getTamedList().getCompound(i);
                if (nbt.contains("PetId") && nbt.getInt("PetId") == petId) {
                    String petName = nbt.getString("Name");
                    ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, 
                            ResourceLocation.parse(nbt.getString("Dimension")));
                    ServerLevel world = source.getServer().getLevel(dimensionKey);
                    
                    if (world != null) {
                        if (!teleportLoadedPet(world, player, petId, petName, source)) {
                            double posX = nbt.getList("Pos", 6).getDouble(0);
                            double posY = nbt.getList("Pos", 6).getDouble(1);
                            double posZ = nbt.getList("Pos", 6).getDouble(2);
                            int x = (int) Math.floor(posX);
                            int y = (int) Math.floor(posY);
                            int z = (int) Math.floor(posZ);
                            
                            source.sendSuccess(() -> Component.literal("Found unloaded pet " + 
                                    nbt.getString("id") + " with name " + 
                                    petName + " at location " + x + ", " + y + ", " + z + 
                                    " with Pet ID " + nbt.getInt("PetId")), true);
                                    
                            // Attempt to TP again
                            boolean result = teleportLoadedPet(world, player, petId, petName, source);
                            if (!result) {
                                source.sendFailure(Component.literal("Unable to transfer entity ID " + 
                                        petId + ". It may only be transferred to " + player.getName().getString()));
                            }
                        }
                        return 1;
                    }
                }
            }
        }
        source.sendFailure(Component.literal("Tamed entity could not be located."));
        return 0;
    }

    private static boolean teleportLoadedPet(ServerLevel world, ServerPlayer player, int petId, String petName, CommandSourceStack source) {
        for (Entity entity : world.getAllEntities()) {
            // Search for entities that are IMoCTameable
            if (entity instanceof IMoCTameable tameable && !tameable.getPetName().isEmpty() && tameable.getOwnerPetId() == petId) {
                // Grab the entity data
                CompoundTag compound = new CompoundTag();
                entity.save(compound);
                
                if (!compound.isEmpty() && !compound.getString("Owner").isEmpty()) {
                    String owner = compound.getString("Owner");
                    String name = compound.getString("Name");
                    
                    if (!owner.isEmpty() && owner.equalsIgnoreCase(player.getName().getString())) {
                        // Check if in same dimension
                        if (entity.level().dimension() == player.level().dimension()) {
                            entity.setPos(player.getX(), player.getY(), player.getZ());
                        } else if (!player.level().isClientSide()) {
                            // Minecraft 1.21.x uses DimensionTransition for cross-dimension moves.
                            ServerLevel targetLevel = player.serverLevel();
                            entity.changeDimension(new DimensionTransition(
                                    targetLevel,
                                    new Vec3(player.getX(), player.getY(), player.getZ()),
                                    entity.getDeltaMovement(),
                                    entity.getYRot(),
                                    entity.getXRot(),
                                    false,
                                    DimensionTransition.DO_NOTHING));
                        }
                        
                        source.sendSuccess(() -> Component.literal(name + 
                                " has been teleported to location " + Math.round(player.getX()) + 
                                ", " + Math.round(player.getY()) + ", " + Math.round(player.getZ()) + 
                                " in dimension " + player.level().dimension().location()), true);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
