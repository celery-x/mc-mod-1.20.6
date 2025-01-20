package top.superxuqc.mcmod.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

import java.util.Optional;

public class SeverHitCheckPayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<HitCheckPayload> {

    public Optional<Float> getBlastResistance(BlockState blockState, FluidState fluidState) {
        return blockState.isAir() && fluidState.isEmpty()
                ? Optional.empty()
                : Optional.of(Math.max(blockState.getBlock().getBlastResistance(), fluidState.getBlastResistance()));
    }


    @Override
    public void receive(HitCheckPayload hitCheckPayload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerWorld world = context.server().getWorld(context.player().getSpawnPointDimension());
            float blastResistance = world.getBlockState(hitCheckPayload.blockPos()).getBlock().getBlastResistance();
            float h = 4;
            h -= (blastResistance + 0.3F) * 0.3F;
            if (h > 0.0F ) {
                world.breakBlock(hitCheckPayload.blockPos(), false);
            }
            if (!world.isClient()) {
                try {
                    world.iterateEntities().forEach(v -> {
                        if (v != null && v.getBlockPos().equals(hitCheckPayload.blockPos())) {
                            v.damage(context.player().getDamageSources().mobProjectile(context.player(), context.player()), hitCheckPayload.amount());
                        }
                    });
                } catch (ArrayIndexOutOfBoundsException e) {
                    //ignore
                }
            }
        });
    }
}
