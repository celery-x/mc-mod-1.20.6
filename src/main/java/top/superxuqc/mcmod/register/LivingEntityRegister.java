package top.superxuqc.mcmod.register;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.mob.MobEntity;
import top.superxuqc.mcmod.entity.PlayerSelfEntity;

public class LivingEntityRegister {

    public static void init() {
        FabricDefaultAttributeRegistry.register(ModEntryTypes.PLAYER_SELF, PlayerSelfEntity.createSelfAttributes());
    }
}
