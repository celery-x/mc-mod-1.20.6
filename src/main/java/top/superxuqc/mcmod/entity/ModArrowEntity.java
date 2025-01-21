package top.superxuqc.mcmod.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import top.superxuqc.mcmod.block.HuChengTnTBlock;
import top.superxuqc.mcmod.common.EnchantmentHandle;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModItemRegister;


// top.superxuqc.mcmod.mixin.ArrowEntityMixin.arrowEntityMixin 在此处修改不同弓箭的物品渲染
public class ModArrowEntity extends ArrowEntity {

    private EntityType<? extends ArrowEntity> selfType;

    public int modAge = 0;

    public int step = 0;

    public int preStep = 0;

    public boolean isTianZai = false;

    public boolean isTianZai() {
        return isTianZai;
    }

    public void setTianZai(boolean tianZai) {
        isTianZai = tianZai;
    }

    public int getModAge() {
        return modAge;
    }

    public void setModAge(int modAge) {
        this.modAge = modAge;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public ModArrowEntity(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public ModArrowEntity(World world, double x, double y, double z, ItemStack stack, int step, int preStep) {
        super(world, x, y, z, stack);
        setStep(step);
        this.preStep = preStep;
    }

    public ModArrowEntity(World world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
    }

    @Override
    public void tick() {
        super.tick();
        modAge ++;

        if (modAge == 4 && step > 0) {
            genrateRandomArrow(this);
        }
    }

    public void genrateRandomArrow(ArrowEntity father) {

        for (int i = 0; i < preStep; i++) {
            double nwex = father.getX() + father.getWorld().random.nextInt( + 16) - 8;
            double nwey = father.getY() + father.getWorld().random.nextInt(16) - 8;
            double nwez = father.getZ() + father.getWorld().random.nextInt(16) - 8;
            ModArrowEntity arrow = new ModArrowEntity(father.getWorld(), nwex, nwey, nwez, this.getItemStack().copyWithCount(1), step - 1, preStep);
//            System.out.println("shengcheng");
            arrow.setVelocity(this.getVelocity());
            father.getWorld().spawnEntity(arrow);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        HitResult.Type type = hitResult.getType();
        if (this.getItemStack().getItem().getTranslationKey().equals(ModItemRegister.ARROW_TNT.getTranslationKey())
            || this.getItemStack().getItem().getTranslationKey().equals(ModItemRegister.TNT_ARROW.getTranslationKey())
        ) {
            if (type == HitResult.Type.ENTITY) {
                // ignore
            } else if (type == HitResult.Type.BLOCK) {
                explosion();
            }

        }
    }


    private void explosion() {

        ItemStack itemStack = getItemStack();
        if (itemStack.getItem().getTranslationKey().equals(ModItemRegister.TNT_ARROW.getTranslationKey())) {
            boolean enchantmentHandle = EnchantmentHandle.isEnchantmentHandle(itemStack, ModEnchantmentRegister.HUCHENG);

            if (enchantmentHandle) {
                int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.HUCHENG, itemStack);
                HuChengTnTEntity tntEntity = new HuChengTnTEntity(getWorld(), getX() + 0.5, getY(), getZ() + 0.5, (LivingEntity) getOwner(), level, level);
                tntEntity.setFuse(4);
                getWorld().spawnEntity(tntEntity);
                getWorld().playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                getWorld().emitGameEvent(getOwner(), GameEvent.PRIME_FUSE, getPos());
            }else {
                TntEntity tntEntity = new TntEntity(getWorld(), getX() + 0.5, getY(), getZ() + 0.5, (LivingEntity) getOwner());
                int i = tntEntity.getFuse();
                tntEntity.setFuse((short)(getWorld().random.nextInt(i / 4) + i / 8));
                getWorld().spawnEntity(tntEntity);
            }
            this.discard();
            return;
        }
        if (!this.getWorld().isClient()) {
            this.getWorld().createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 4.0F, World.ExplosionSourceType.TNT);
            this.discard();
        }
    }

    @Override
    public void onHit(LivingEntity target) {
        super.onHit(target);
        if (this.getItemStack().getItem().getTranslationKey().equals(ModItemRegister.ARROW_TNT.getTranslationKey())
                || this.getItemStack().getItem().getTranslationKey().equals(ModItemRegister.TNT_ARROW.getTranslationKey())
        ) {
            explosion();
        } else {
            discard();
        }
    }
}
