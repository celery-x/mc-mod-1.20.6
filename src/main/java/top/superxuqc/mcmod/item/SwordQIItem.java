package top.superxuqc.mcmod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import top.superxuqc.mcmod.entity.SwordQiEntity;

public class SwordQIItem extends SwordItem {
    public SwordQIItem(Item.Settings settings) {
        super(ToolMaterials.DIAMOND, settings);
    }

}
