package de.nike.extramodules2;

import de.nike.extramodules2.client.entities.DraconicBulletRenderer;
import de.nike.extramodules2.client.entities.DraconicLightningChainRenderer;
import de.nike.extramodules2.client.entities.model.DraconicBulletModel;
import de.nike.extramodules2.entities.EMEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ExtraModules2.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ExtraModules2.MODID, value = Dist.CLIENT)
public class ExtraModules2Client {
    public ExtraModules2Client(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EMEntities.DRACONIC_LIGHTNING_CHAIN.get(), DraconicLightningChainRenderer::new);
        EntityRenderers.register(EMEntities.DRACONIC_BULLET.get(), DraconicBulletRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
}
