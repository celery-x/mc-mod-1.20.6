package top.superxuqc.mcmod.mixin.client;

import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.enchantment.FeiLeiEnchantment;
import top.superxuqc.mcmod.register.ModItemRegister;

import java.util.Set;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {


    @Shadow
    abstract ItemModels getModels();

    @Inject(cancellable = true, at = @At("HEAD"), method = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;")
    private void getModelMixin(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> ci){
        Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(stack).getEnchantments();
        boolean isFeiLei = false;
        for (RegistryEntry<Enchantment> enchantment : enchantments) {
            isFeiLei = enchantment.value() instanceof FeiLeiEnchantment;
            if (isFeiLei) {
                break;
            }
        }
        BakedModel model = getModels().getModel(ModItemRegister.KUWU);
        if(isFeiLei && model != null) {
            ci.setReturnValue(model);
        }
        //System.out.println("my en :::  " + isFeiLei);


    }

}
