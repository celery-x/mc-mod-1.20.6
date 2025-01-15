package top.superxuqc.mcmod.register;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.entity.HuChengTnTEntity;
import top.superxuqc.mcmod.entity.SwordQiEntity;


public class ModEntryTypes {
    public static final EntityType<HuChengTnTEntity> HUCHENG_TNT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "hucheng_tnt"),
            EntityType.Builder.<HuChengTnTEntity>create(HuChengTnTEntity::new, SpawnGroup.MISC)
                    .makeFireImmune()
                    .dimensions(0.98F, 0.98F)
                    .eyeHeight(0.15F)
                    .maxTrackingRange(10)
                    .trackingTickInterval(10).build());

    public static final EntityType<SwordQiEntity> SWORD_QI_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "sword_qi"),
            EntityType.Builder.<SwordQiEntity>create(SwordQiEntity::new, SpawnGroup.MISC)
                    .dimensions(10.0F, 10.0F).maxTrackingRange(4).trackingTickInterval(10).build());


    public static void init() {

    }
}
