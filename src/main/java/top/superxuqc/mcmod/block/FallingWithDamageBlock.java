package top.superxuqc.mcmod.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.ColoredFallingBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.ColorCode;

import java.util.function.Function;

public class FallingWithDamageBlock extends FallingBlock {

    public static final MapCodec<FallingWithDamageBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.INT.fieldOf("falling_block_damage").forGetter(block -> block.damage), createSettingsCodec())
                    .apply(instance, FallingWithDamageBlock::new)
    );

    private final int damage;

    public FallingWithDamageBlock(int damage, Settings settings) {
        super(settings);
        this.damage = damage;
    }

    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
        entity.setHurtEntities(damage, 40);
    }

}
