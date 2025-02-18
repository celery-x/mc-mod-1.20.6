package top.superxuqc.mcmod.listener.handle;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClearAbleEntityHandler implements ServerLifecycleEvents.ServerStopping {

    public static ConcurrentHashMap<RegistryKey<World>, List<Integer>> entitiesIds = new ConcurrentHashMap<>();

    @Override
    public void onServerStopping(MinecraftServer minecraftServer) {
        entitiesIds.forEach((k, v) -> {
            ServerWorld world = minecraftServer.getWorld(k);
            for (Integer i : v) {
                Entity entityById = world.getEntityById(i);
                if (entityById != null && entityById.isAlive() && !entityById.isRemoved()) {
                    entityById.discard();
                }
            }
        });
    }
}
