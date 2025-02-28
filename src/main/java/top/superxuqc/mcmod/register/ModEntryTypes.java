package top.superxuqc.mcmod.register;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.entity.*;


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

    public static final EntityType<ModArrowEntity> ARROW_TNT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "arrow_tnt"),
            EntityType.Builder.<ModArrowEntity>create(ModArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20).build());

    public static final EntityType<ModArrowEntity> TNT_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "tnt_arrow"),
            EntityType.Builder.<ModArrowEntity>create(ModArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20).build());

    public static final EntityType<PlayerSelfEntity> PLAYER_SELF = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "player"),
            EntityType.Builder.<PlayerSelfEntity>create(PlayerSelfEntity::new, SpawnGroup.MISC)
                    .disableSaving()
                    .disableSummon()
                    .dimensions(0.6F, 1.8F)
                    .eyeHeight(1.62F)
                    .vehicleAttachment(PlayerEntity.VEHICLE_ATTACHMENT_POS)
                    .maxTrackingRange(32)
                    .trackingTickInterval(2).build()
    );

    public static final EntityType<ChuanXinZhouEntity> CHUAN_XIN_ZHOU_TYPR = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "chuan_xin_zhou"),
            EntityType.Builder.<ChuanXinZhouEntity>create(ChuanXinZhouEntity::new, SpawnGroup.MISC)
                    .dimensions(0.1F, 0.1F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20).build());


    public static final EntityType<Entity> NoneViewEntity = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "none_view_entity"),
            EntityType.Builder.<Entity>create(RepulsiveForceEntity::new, SpawnGroup.MISC)
                    .dimensions(2.0F, 2.0F).maxTrackingRange(4).trackingTickInterval(10).build());

    public static final EntityType<AttractionEntity> ATTRACTION_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "attraction_entity"),
            EntityType.Builder.<AttractionEntity>create(AttractionEntity::new, SpawnGroup.MISC)
                    .dimensions(2.0F, 2.0F).maxTrackingRange(4).trackingTickInterval(10).build());

    public static final EntityType<XianJianEntity> XIAN_JIAN_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "xian_jian"),
            EntityType.Builder.<XianJianEntity>create(XianJianEntity::new, SpawnGroup.MISC)
                    .dimensions(1F, 1F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20).build());

    public static final EntityType<LightArrowEntity> LIGHT_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MyModInitializer.MOD_ID, "tnt_arrow"),
            EntityType.Builder.<LightArrowEntity>create(LightArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20).build());

    public static void init() {

    }
}
