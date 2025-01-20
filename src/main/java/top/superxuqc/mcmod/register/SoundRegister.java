package top.superxuqc.mcmod.register;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.PathUtil;
import top.superxuqc.mcmod.MyModInitializer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SoundRegister {

    public static AtomicBoolean SCARE_SELF_SOUND_PLAY = new AtomicBoolean(true);

    public static SoundEvent SCARE_SELF_SOUND = register(Identifier.of(MyModInitializer.MOD_ID, "scare_self_sound"));

    public static SoundEvent BAN_KAI = register(Identifier.of(MyModInitializer.MOD_ID, "ban_kai"));

    public static SoundEvent FEN_SHEN_KAI = register(Identifier.of(MyModInitializer.MOD_ID, "fenshenkai"));

    public static SoundEvent FEN_SHEN_GUAN = register(Identifier.of(MyModInitializer.MOD_ID, "fenshenguan"));
    private static SoundEvent register(Identifier id) {
        return register(id, id);
    }

    private static SoundEvent register(Identifier id, Identifier soundId) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(soundId));
    }


    public static void init() {

    }
}
