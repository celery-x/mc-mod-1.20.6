package top.superxuqc.mcmod.entity.goal.playself;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class UseAxeGoal extends UseToolGoal{

    public UseAxeGoal(TameableEntity entity, World world, int width) {
        super(entity, world, width);
    }
}
