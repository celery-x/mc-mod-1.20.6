package top.superxuqc.mcmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.register.*;

import java.util.Set;
import java.util.stream.IntStream;


/**
 * 求方向
 * double d = vec3d.horizontalLength();
 * 			this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 180.0F / (float)Math.PI));  水平转向角度 +z为0度
 * 			this.setPitch((float)(MathHelper.atan2(vec3d.y, d) * 180.0F / (float)Math.PI));  垂直转向角
 */
public class MyModInitializer implements ModInitializer {
    public static  final String MOD_ID = "superx";

    public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "item_group"));

    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItemRegister.HUCHENG_TNT_ITEM))
            .displayName(Text.translatable("itemGroup.superx"))
            .entries((displayContext, entries) -> {

                // 注册物品
                entries.add(ModItemRegister.HUCHENG_TNT_ITEM);
                entries.add(ModItemRegister.KUWU);
                entries.add(ModItemRegister.ZHAN_YUE);
                entries.add(ModItemRegister.ARROW_TNT);
                entries.add(ModItemRegister.TNT_ARROW);


                // 注册附魔书
                Set<TagKey<Item>> set = Set.of(
                        ModTarKeys.MOD_ITEM_ENCHANT_TAG,
                        ModTarKeys.SCARE_SELF_ENCHANT_TAG,
                        ModTarKeys.SWORD_QI_TAG
                );
                displayContext.lookup().getOptionalWrapper(RegistryKeys.ENCHANTMENT).ifPresent(registryWrapper -> {
//                    addMaxLevelEnchantedBooks(entries, registryWrapper, set, ItemGroup.StackVisibility.PARENT_TAB_ONLY, displayContext.enabledFeatures());
//                    addAllLevelEnchantedBooks(entries, registryWrapper, set, ItemGroup.StackVisibility.SEARCH_TAB_ONLY, displayContext.enabledFeatures());
                    addAllLevelEnchantedBooks(entries, registryWrapper, set, ItemGroup.StackVisibility.PARENT_TAB_ONLY, displayContext.enabledFeatures());
                });
            })
            .build();

    private static void addAllLevelEnchantedBooks(
            ItemGroup.Entries entries,
            RegistryWrapper<Enchantment> registryWrapper,
            Set<TagKey<Item>> tags,
            ItemGroup.StackVisibility visibility,
            FeatureSet enabledFeatures
    ) {
        registryWrapper.streamEntries()
                .map(RegistryEntry::value)
                .filter(enchantment -> enchantment.isEnabled(enabledFeatures))
                .filter(enchantment -> tags.contains(enchantment.getApplicableItems()))
                .flatMap(
                        enchantment -> IntStream.rangeClosed(enchantment.getMinLevel(), enchantment.getMaxLevel())
                                .mapToObj(level -> EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, level)))
                )
                .forEach(stack -> entries.add(stack, visibility));
    }

    private static void addMaxLevelEnchantedBooks(
            ItemGroup.Entries entries,
            RegistryWrapper<Enchantment> registryWrapper,
            Set<TagKey<Item>> tags,
            ItemGroup.StackVisibility visibility,
            FeatureSet enabledFeatures
    ) {
        registryWrapper.streamEntries()
                .map(RegistryEntry::value)
                .filter(enchantment -> enchantment.isEnabled(enabledFeatures))
                .filter(enchantment -> tags.contains(enchantment.getApplicableItems()))
                .map(enchantment -> EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, enchantment.getMaxLevel())))
                .forEach(stack -> entries.add(stack, visibility));
    }


    @Override
    public void onInitialize() {
        //Registry.register(Registries.ITEM, Identifier.of("tutorial", "custom_item"), CUSTOM_ITEM);
        ModItemRegister.init();
        ModBlocksRegister.init();
        ModEntityRegister.init();
        ModEntryTypes.init();
        ModEnchantmentRegister.init();
        ParticleRegister.init();
        LivingEntityRegister.init();
        ModSeverEventRegister.init();
        Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
//        ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register(itemGroup -> {
//            itemGroup.add(ModItemRegister.HUCHENG_TNT_ITEM);
//            itemGroup.add(ModItemRegister.KUWU);
//            itemGroup.add(ModItemRegister.ARROW1);
//            // ...
//        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {

        });
        SoundRegister.init();
    }
}
