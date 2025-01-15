package top.superxuqc.mcmod;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.keymouse.KeyBindRegister;
import top.superxuqc.mcmod.particle.JianQiParticle;
import top.superxuqc.mcmod.particle.JianQiParticleEffect;
import top.superxuqc.mcmod.register.ModBlocksRegister;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.ParticleRegister;
import top.superxuqc.mcmod.register.SoundRegister;
import top.superxuqc.mcmod.renderer.HuChengTnTRenderer;
import top.superxuqc.mcmod.renderer.SwordQiEntityRenderer;

import java.util.function.Function;


public class TemplateModClient implements ClientModInitializer {


	@Override
	public void onInitializeClient() {
		//System.out.println("client init");
		EntityRendererRegistry.register(ModEntryTypes.HUCHENG_TNT,  (context) -> new HuChengTnTRenderer(ModBlocksRegister.HUCHENG_TNT_BLOCK, context));
		EntityRendererRegistry.register(ModEntryTypes.SWORD_QI_TYPE, SwordQiEntityRenderer::new);
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ServerLifecycleEvents.SERVER_STOPPING.register((client) -> KeyBindRegister.resetBlock());
		ParticleFactoryRegistry.getInstance().register(ParticleRegister.JIANQI, JianQiParticle.Factory::new);

		KeyBindRegister.init();
	}


}