package top.superxuqc.mcmod.register;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.effect.ChaKeLaStatusEffect;

public class ModEffectRegister {
    public static final StatusEffect CHA_KE_LA = new ChaKeLaStatusEffect();
    public static void init() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MyModInitializer.MOD_ID, "cha_ke_la_effect"), CHA_KE_LA);
    }
}
