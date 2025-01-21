package top.superxuqc.mcmod.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import top.superxuqc.mcmod.common.EntityModI;

import java.util.UUID;

@Mixin(Entity.class)
public class EntityMixin implements EntityModI {

    @Unique
    private UUID byPlayerUuid;

    @Override
    public void setByPlayer(UUID playerUuid) {
        this.byPlayerUuid = playerUuid;
    }

    @Override
    public UUID getByPlayer() {
        return byPlayerUuid;
    }


}
