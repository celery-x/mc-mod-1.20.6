package top.superxuqc.mcmod.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import top.superxuqc.mcmod.block.HuChengTnTBlock;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.SoundRegister;

public class HuchengTnTItem extends BlockItem {


    public HuchengTnTItem(Block block, Settings settings) {
        super(block, settings);
    }

    public ActionResult place(ItemPlacementContext context) {
        ItemPlacementContext itemPlacementContext = this.getPlacementContext(context);
        BlockPos blockPos = new BlockPos(itemPlacementContext.getBlockPos());
        ActionResult result = super.place(context);
        //SwordQiEntity swordQiEntity = new SwordQiEntity(context.getWorld(), blockPos.getX() + 2, blockPos.getY(), blockPos.getZ() + 2, context.getStack());
        //context.getWorld().spawnEntity(swordQiEntity);
        int i = EnchantmentHelper.getLevel(ModEnchantmentRegister.HUCHENG, itemPlacementContext.getStack());
        Block block = context.getWorld().getBlockState(blockPos).getBlock();
        if (block instanceof HuChengTnTBlock) {
            ((HuChengTnTBlock) block).setStep(i);
        }
        return result;
    }
}
