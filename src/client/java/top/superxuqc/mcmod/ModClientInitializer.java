package top.superxuqc.mcmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import top.superxuqc.mcmod.entity.ModArrowEntity;
import top.superxuqc.mcmod.keymouse.KeyBindRegister;
import top.superxuqc.mcmod.network.C2SPayloadRegister;
import top.superxuqc.mcmod.network.ClientPayloadHandlerRegister;
import top.superxuqc.mcmod.network.S2CPayloadRegister;
import top.superxuqc.mcmod.network.SeverPayloadHandlerRegister;
import top.superxuqc.mcmod.particle.JianQiParticle;
import top.superxuqc.mcmod.register.ModBlocksRegister;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.ModRendererRegister;
import top.superxuqc.mcmod.register.ParticleRegister;
import top.superxuqc.mcmod.renderer.*;


public class ModClientInitializer implements ClientModInitializer {


	@Override
	public void onInitializeClient() {
		//System.out.println("client init");
		ModRendererRegister.init();
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.



        // 此处代码在服务器可能会崩溃
		ServerLifecycleEvents.SERVER_STOPPING.register((server) ->
		{
			KeyBindRegister.resetBlock();
			server.getWorlds().forEach(v -> {
				v.iterateEntities().forEach(e -> {
					if (e instanceof ModArrowEntity) {
						e.discard();
					}
				});
			});
		}
		);

		ParticleFactoryRegistry.getInstance().register(ParticleRegister.JIANQI, JianQiParticle.Factory::new);

		// init network
		C2SPayloadRegister.init();
		S2CPayloadRegister.init();
		ClientPayloadHandlerRegister.init();
		SeverPayloadHandlerRegister.init();
		KeyBindRegister.init();
	}


}