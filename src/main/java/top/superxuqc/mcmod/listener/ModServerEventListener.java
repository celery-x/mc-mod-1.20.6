package top.superxuqc.mcmod.listener;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import top.superxuqc.mcmod.listener.handle.ClearAbleEntityHandler;

public class ModServerEventListener {
    public static void init() {
        ServerLifecycleEvents.SERVER_STOPPING.register(new ClearAbleEntityHandler());
    }
}
