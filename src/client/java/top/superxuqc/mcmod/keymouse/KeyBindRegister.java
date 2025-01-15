package top.superxuqc.mcmod.keymouse;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.common.PositionDouble;
import top.superxuqc.mcmod.entity.FeiLeiShenEntity;
import top.superxuqc.mcmod.register.SoundRegister;

import java.util.concurrent.atomic.AtomicBoolean;

import static top.superxuqc.mcmod.register.SoundRegister.SCARE_SELF_SOUND_PLAY;

public class KeyBindRegister {

    private static KeyBinding keyKPAddBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "scare.self.sound", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_ADD, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));

    private static KeyBinding keyKP0Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.zero.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_0, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));

    private static KeyBinding keyKP1Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.one.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_1, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));

    private static KeyBinding keyKP2Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.two.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_2, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));
    private static KeyBinding keyKP3Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.three.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_3, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));
    private static KeyBinding keyKP4Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.four.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_4, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));
    private static KeyBinding keyKP5Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.five.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_5, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));
    private static KeyBinding keyKP6Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.six.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_6, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));
    private static KeyBinding keyKP7Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.seven.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_7, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));
    private static KeyBinding keyKP8Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.eight.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_8, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));
    private static KeyBinding keyKP9Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.night.feilei", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_KP_9, // The keycode of the key
            "category.examplemod.test" // The translation key of the keybinding's category.
    ));

    public static boolean show = false;

    private static void tp(Entity entity, PositionDouble pos) {
        if(pos == null) {
            return;
        }
        show = true;
        if (entity.hasVehicle()) {
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.requestTeleportAndDismount(pos.getX(), pos.getY(), pos.getZ());
            }
        } else {
            entity.requestTeleport(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    private static void tp(Entity entity, Entity target) {
        BlockPos pos = target.getBlockPos();
        if(pos == null) {
            return;
        }
        if (entity.hasVehicle()) {
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.requestTeleportAndDismount(pos.getX(), pos.getY(), pos.getZ());
            }
        } else {
            entity.requestTeleport(pos.getX(), pos.getY(), pos.getZ());
        }
    }



    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP0Binding.wasPressed()) {
                //client.player.teleport();
                resetBlock();
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP1Binding.wasPressed()) {
                //client.player.teleport();
                tp(FeiLeiShenEntity.player, FeiLeiShenEntity.POS[0]);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP2Binding.wasPressed()) {
                //client.player.teleport();
                tp(FeiLeiShenEntity.player, FeiLeiShenEntity.POS[1]);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP3Binding.wasPressed()) {
                //client.player.teleport();
                tp(FeiLeiShenEntity.player, FeiLeiShenEntity.POS[2]);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP4Binding.wasPressed()) {
                //client.player.teleport();
                tp(FeiLeiShenEntity.player, FeiLeiShenEntity.POS[3]);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP5Binding.wasPressed()) {
                //client.player.teleport();
                tp(FeiLeiShenEntity.player, FeiLeiShenEntity.POS[4]);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP6Binding.wasPressed()) {
                //client.player.teleport();
                tp(FeiLeiShenEntity.player, FeiLeiShenEntity.POS[5]);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP7Binding.wasPressed()) {
                //client.player.teleport();
                tp(FeiLeiShenEntity.player, FeiLeiShenEntity.POS[6]);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP8Binding.wasPressed()) {
                //client.player.teleport();
                tp(FeiLeiShenEntity.player, FeiLeiShenEntity.POS[7]);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKP9Binding.wasPressed()) {
                //client.player.teleport();
                tp(FeiLeiShenEntity.player, FeiLeiShenEntity.TARGET);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyKPAddBinding.wasPressed()) {
                //client.player.teleport();
                boolean old = SCARE_SELF_SOUND_PLAY.get();
                SCARE_SELF_SOUND_PLAY.compareAndSet(old, !old);
                client.player.sendMessage(Text.translatable("scare.self.tip", SCARE_SELF_SOUND_PLAY.get() ? "开" : "关"));

            }
        });
    }

    public static void resetBlock() {
        FeiLeiShenEntity.POS = new PositionDouble[9];
        for (int i = 0; i < 9; i++) {
            if (FeiLeiShenEntity.ORI_BLOCK_STATE[i] != null && FeiLeiShenEntity.ORI_BLOCK_POS[i] != null) {
                FeiLeiShenEntity.w.setBlockState(FeiLeiShenEntity.ORI_BLOCK_POS[i], FeiLeiShenEntity.ORI_BLOCK_STATE[i]);
            }
        }
        FeiLeiShenEntity.ORI_BLOCK_STATE = new BlockState[9];
        FeiLeiShenEntity.ORI_BLOCK_POS = new BlockPos[9];
    }
}
