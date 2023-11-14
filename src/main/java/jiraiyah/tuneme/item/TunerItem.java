package jiraiyah.tuneme.item;

import jiraiyah.tuneme.TuneMe;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TunerItem extends Item
{
    public TunerItem(Settings settings)
    {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if(user.isSneaking() && user.getStackInHand(hand).hasNbt())
            user.getStackInHand(hand).setNbt(new NbtCompound());
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand)
    {
        if(entity.isPlayer())
            return ActionResult.FAIL;
        if(!stack.hasNbt())
            return ActionResult.FAIL;
        if(entity.getType().isIn(TuneMe.CANNOT_TELEPORT_TAGKEY))
            return ActionResult.FAIL;
        if(!user.isSneaking())
        {
            var pos = NbtHelper.toBlockPos(stack.getNbt().getCompound("tuneme.pos"));

            if (!user.getWorld().isClient)
            {
                var dimension = stack.getNbt().getString("tuneme.dimension");
                var userDimension = user.getWorld().getRegistryKey().getValue().toString();
                if(dimension.equalsIgnoreCase(userDimension))
                {
                    entity.teleport(pos.getX(), pos.getY() + 1, pos.getZ(), true);
                    entity.refreshPositionAfterTeleport(pos.getX(), pos.getY() + 1, pos.getZ());
                    return ActionResult.SUCCESS;
                }
                return ActionResult.FAIL;
            }
            var dimension = stack.getNbt().getString("tuneme.dimension");
            var userDimension = user.getWorld().getRegistryKey().getValue().toString();
            var dimensionName = stack.getNbt().getString("tuneme.dimension");
            dimensionName = dimensionName.substring(dimensionName.indexOf(':') + 1).replace('_', ' ');
            if(dimension.equalsIgnoreCase(userDimension))
            {
                user.sendMessage(Text.translatable("tuneme.teleported",pos.getX(), pos.getY(), pos.getZ(), dimensionName), false);
                return ActionResult.SUCCESS;
            }
            else
            {
                user.sendMessage(Text.translatable("tuneme.error", dimensionName), false);
                return ActionResult.FAIL;
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context)
    {
        if(context.getStack().hasNbt())
            return super.useOnBlock(context);
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        NbtCompound data = new NbtCompound();

        if(!player.getWorld().isClient)
        {
            data.put("tuneme.pos", NbtHelper.fromBlockPos(pos));
            data.putString("tuneme.dimension", player.getWorld().getRegistryKey().getValue().toString());
            context.getStack().setNbt(data);
        }
        if(context.getWorld().isClient && !player.isSneaking())
        {
            var dimensionName = player.getWorld().getRegistryKey().getValue().toString();
            dimensionName = dimensionName.substring(dimensionName.indexOf(':') + 1).replace('_', ' ');
            outputCoordinatesToChat(pos, dimensionName, player);
        }
        return super.useOnBlock(context);
    }

    @Override
    public boolean hasGlint(ItemStack stack)
    {
        return stack.hasNbt();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        if(stack.hasNbt())
        {
            var dimensionName = stack.getNbt().getString("tuneme.dimension");
            dimensionName = dimensionName.substring(dimensionName.indexOf(':') + 1).replace('_', ' ');
            var pos = NbtHelper.toBlockPos(stack.getNbt().getCompound("tuneme.pos"));
            tooltip.add(Text.translatable("tuneme.tooltip", pos.getX(), pos.getY(), pos.getZ(), dimensionName));
        }
    }

    private void outputCoordinatesToChat(BlockPos pos, String dimensionName, PlayerEntity player)
    {
        player.sendMessage(Text.translatable("tuneme.tooltip", pos.getX(), pos.getY(), pos.getZ(), dimensionName), false);
    }
}