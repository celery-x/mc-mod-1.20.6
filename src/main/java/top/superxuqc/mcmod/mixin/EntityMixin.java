package top.superxuqc.mcmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import top.superxuqc.mcmod.common.EntityModI;

import javax.swing.text.html.parser.Entity;
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
