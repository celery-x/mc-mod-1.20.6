package top.superxuqc.mcmod.entity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ColoredFallingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ColorCode;
import net.minecraft.world.World;
import top.superxuqc.mcmod.register.ModEntryTypes;

public class ChuanXinZhouEntity extends MobEntity {
    public ChuanXinZhouEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(ModEntryTypes.CHUAN_XIN_ZHOU_TYPR, world);
    }


    public ChuanXinZhouEntity( World world) {
        super(ModEntryTypes.CHUAN_XIN_ZHOU_TYPR, world);
    }

    public static DefaultAttributeContainer.Builder createSelfAttributes() {
        return DefaultAttributeContainer.builder()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1024)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 0)
                .add(EntityAttributes.GENERIC_SCALE)
                .add(EntityAttributes.GENERIC_MAX_ABSORPTION)
                .add(EntityAttributes.GENERIC_GRAVITY, 0)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1);
    }

}
