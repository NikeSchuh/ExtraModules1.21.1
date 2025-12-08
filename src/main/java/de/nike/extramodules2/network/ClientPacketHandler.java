package de.nike.extramodules2.network;

import codechicken.lib.packet.ICustomPacketHandler;
import codechicken.lib.packet.PacketCustom;
import de.nike.extramodules2.entities.EMEntities;
import de.nike.extramodules2.entities.projectiles.DraconicLightningChain;
import de.nike.extramodules2.modules.entities.defensebrain.EyeMode;
import de.nike.extramodules2.utils.ModuleHostFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;

public class ClientPacketHandler implements ICustomPacketHandler.IClientPacketHandler {
    @Override
    public void handlePacket(PacketCustom packetCustom, Minecraft minecraft) {
        switch (packetCustom.getType()) {
            case ModuleProtocol.S_DEFENSE_BRAIN_RAGE_PROGRESS_CHANGE:
                float newProgress = packetCustom.readFloat();
                ModuleHostFinder.findDefenseBrain(Minecraft.getInstance().player).ifPresent(brainEntity -> {
              //      brainEntity.clientChangeRageProgress(newProgress);
                });
                break;
            case ModuleProtocol.S_DEFENSE_BRAIN_EYE_MODE_CHANGE:
                int eyeMode = packetCustom.readVarInt();
                ModuleHostFinder.findDefenseBrain(Minecraft.getInstance().player).ifPresent(brainEntity -> {
               //     brainEntity.clientChangeEyeMode(EyeMode.valueOf(eyeMode));
                });
                break;
            case ModuleProtocol.S_DEFENSE_BRAIN_RAGE_MODE_CHANGE:
                boolean rageMode = packetCustom.readBoolean();
                ModuleHostFinder.findDefenseBrain(Minecraft.getInstance().player).ifPresent(brainEntity -> {
              //      brainEntity.clientChangeRageMode(rageMode);
                });
            break;
            case ModuleProtocol.S_ADD_CHAIN:
                double x = packetCustom.readDouble();
                double y = packetCustom.readDouble();
                double z = packetCustom.readDouble();

                int startEntityid = packetCustom.readInt();
                int endEntityid = packetCustom.readInt();

                int color = packetCustom.readInt();


                ClientLevel level = minecraft.level;
                DraconicLightningChain lightningChain = new DraconicLightningChain(EMEntities.DRACONIC_LIGHTNING_CHAIN.get(), level);

                lightningChain.setPos(x, y, z);

                lightningChain.setStartEntity(level.getEntity(startEntityid));
                lightningChain.setEndEntity(level.getEntity(endEntityid));
                lightningChain.setLightningColor(color);

                level.addEntity(lightningChain);
                break;

        }
    }
}
