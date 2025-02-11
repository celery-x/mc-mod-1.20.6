package top.superxuqc.mcmod.mixin;

import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import top.superxuqc.mcmod.block.interfaces.WithTag;

@Mixin(BlockState.class)
public class BlockStateMixin implements WithTag {

    @Unique
    String attractionItemTag;

    @Override
    public void setTag(String tag) {
        attractionItemTag = tag;
    }

    @Override
    public String getTag() {
        return attractionItemTag == null ? "" : attractionItemTag;
    }

}
