package top.superxuqc.mcmod.entity;

import net.minecraft.entity.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.superxuqc.mcmod.register.ModEntryTypes;

public class HuChengTnTEntity extends TntEntity {

    private Integer step = 1;

    private Integer preStep = 1;
    public static void init(){}

    public HuChengTnTEntity(EntityType<? extends HuChengTnTEntity> entityType, World world) {
        super(entityType, world);
    }

    public HuChengTnTEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(ModEntryTypes.HUCHENG_TNT, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * (float) (Math.PI * 2);
        this.setVelocity(-Math.sin(d) * 0.02, 0.2F, -Math.cos(d) * 0.02);
        this.setFuse(80);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public HuChengTnTEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter, Integer step, Integer preStep) {
        this(world, x, y, z, igniter);
        this.step = step;
        this.preStep = preStep;
    }

    @Override
    public void tick() {
        this.applyGravity();
        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.98));
        if (this.isOnGround()) {
            this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
        }

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0 && step > 0) {
            this.discard();
            if (!this.getWorld().isClient) {
                float f = 4.0F;

                World world = this.getWorld();
                for (int j = 0; j < preStep; j++) {
                    newTnt(world);
                }
                //world.setBlockState(new BlockPos(nwex, nwey ,nwez), ModBlocksRegister.HUCHENG_TNT_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
                this.getWorld().createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 4.0F, World.ExplosionSourceType.TNT);
            }
        } else {
            this.updateWaterState();
            if (this.getWorld().isClient) {
                this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
            }
        }
        if (step <= 0) {
            this.discard();
        }
    }

    private void newTnt(World world) {
        int nwex = (int) this.getX() + world.random.nextInt( + 16) - 8;
        int nwey = (int) this.getY() + world.random.nextInt(16) - 8;
        int nwez = (int) this.getZ() + world.random.nextInt(16) - 8;

        HuChengTnTEntity tntEntity = new HuChengTnTEntity(world, (double)nwex + 0.5, (double)nwey, (double)nwez + 0.5, this.getOwner(), step - 1, preStep);
        int i2 = tntEntity.getFuse();
        tntEntity.setFuse((short)(world.random.nextInt(i2 / 4) + i2 / 8));
        world.spawnEntity(tntEntity);
    }
}
