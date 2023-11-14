package jiraiyah.tuneme;

import jiraiyah.tuneme.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TuneMe implements ModInitializer
{
    public static final String ModID = "tuneme";
    public static final Logger LOGGER = LoggerFactory.getLogger("tuneme");

    public static TagKey CANNOT_TELEPORT_TAGKEY = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(ModID,"cannot_teleport"));

    @Override
    public void onInitialize()
    {
        LOGGER.info(">>> Initializing");

        ModItems.register();

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) ->
        {
            ItemStack stack = player.getStackInHand(hand);
            if(stack.isOf(ModItems.TUNER))
            {
                if(!stack.hasNbt())
                    return ActionResult.FAIL;

                if(entity.getType().isIn(TuneMe.CANNOT_TELEPORT_TAGKEY))
                    return ActionResult.FAIL;

                if(entity.getType() == EntityType.VILLAGER)
                {
                    if (stack.hasNbt())
                    {
                        if (!player.isSneaking())
                        {
                            var pos = NbtHelper.toBlockPos(stack.getNbt().getCompound("tuneme.pos"));

                            if (!player.getWorld().isClient)
                            {
                                var dimension = stack.getNbt().getString("tuneme.dimension");
                                var userDimension = player.getWorld().getRegistryKey().getValue().toString();
                                if(dimension.equalsIgnoreCase(userDimension))
                                {
                                    ((VillagerEntity)entity).teleport(pos.getX(), pos.getY() + 1, pos.getZ(), true);
                                    entity.refreshPositionAfterTeleport(pos.getX(), pos.getY() + 1, pos.getZ());
                                    return ActionResult.SUCCESS;
                                }
                                return ActionResult.FAIL;
                            }
                            var dimension = stack.getNbt().getString("tuneme.dimension");
                            var userDimension = player.getWorld().getRegistryKey().getValue().toString();
                            var dimensionName = stack.getNbt().getString("tuneme.dimension");
                            dimensionName = dimensionName.substring(dimensionName.indexOf(':') + 1).replace('_', ' ');
                            if(dimension.equalsIgnoreCase(userDimension))
                            {
                                player.sendMessage(Text.translatable("tuneme.teleported",pos.getX(), pos.getY(), pos.getZ(), dimensionName), false);
                                return ActionResult.SUCCESS;
                            }
                            else
                            {
                                player.sendMessage(Text.translatable("tuneme.error", dimensionName), false);
                                return ActionResult.FAIL;
                            }
                        }
                    }
                }
            }
            return ActionResult.PASS;
        });
    }

    @NotNull
    public static Identifier identifier(@NotNull String path)
    {
        return new Identifier(ModID, path);
    }
}