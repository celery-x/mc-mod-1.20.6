package top.superxuqc.mcmod.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import top.superxuqc.mcmod.entity.PlayerSelfEntity;
import top.superxuqc.mcmod.network.payload.PlayerSelfSpawnPayload;
import top.superxuqc.mcmod.register.ModEffectRegister;
import top.superxuqc.mcmod.register.SoundRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerPlayerSelfSpawnPayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<PlayerSelfSpawnPayload> {

    public ConcurrentHashMap<UUID, List<Integer>> entityByOwner = new ConcurrentHashMap<>();

    @Override
    public void receive(PlayerSelfSpawnPayload playerSelfSpawnPayload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerWorld world = context.server().getWorld(context.player().getSpawnPointDimension());
            UUID fatherUuid = playerSelfSpawnPayload.father();
            List<Integer> ids = entityByOwner.get(fatherUuid);
            if (ids != null && !ids.isEmpty()) {
                boolean ret = false;
                for (Integer id : ids) {
                    Entity e = world.getEntityById(id);
                    if (e != null && e.isAlive()){
                        ret = true;
                        ((PlayerSelfEntity)e).preToDiscard();
                        context.player().playSoundToPlayer(SoundRegister.FEN_SHEN_GUAN, SoundCategory.PLAYERS, 1, 1);
                    }
                }
                if (ret) {
                    entityByOwner.remove(fatherUuid);
                    return;
                }
            }
            if (!world.isClient()) {
                if (fatherUuid != null) {

                    ServerPlayerEntity player = context.server().getPlayerManager().getPlayer(fatherUuid);

                    for (StatusEffectInstance effect : context.player().getStatusEffects()) {
                        if (effect.getEffectType().value().equals(ModEffectRegister.CHA_KE_LA)){
                            int amplifier = effect.getAmplifier();
                            for (int i = 0; i < amplifier; i++) {
                                spawnFenShen(world, fatherUuid, player);
                            }
                            break;
                        }
                    }
                    spawnFenShen(world, fatherUuid, player);
                    context.player().playSoundToPlayer(SoundRegister.FEN_SHEN_KAI, SoundCategory.PLAYERS, 1, 1);
                }
            }
        });
    }

    private void spawnFenShen(ServerWorld world, UUID fatherUuid, ServerPlayerEntity player) {
        int nwex = world.random.nextInt( + 16) - 8;
        int nwey = world.random.nextInt(4);
        int nwez = world.random.nextInt(16) - 8;
        PlayerSelfEntity entity = new PlayerSelfEntity(null, world,
                fatherUuid, player.getMainHandStack(), player.getOffHandStack(), (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        entity.setPosition(player.getPos().add(nwex, nwey, nwez));
        entity.initEquipment(world.getRandom(), world.getLocalDifficulty(entity.getBlockPos()));
        world.spawnEntity(entity);
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20));
        entityByOwner.computeIfAbsent(fatherUuid, v -> new ArrayList<>()).add(entity.getId());
    }

}
