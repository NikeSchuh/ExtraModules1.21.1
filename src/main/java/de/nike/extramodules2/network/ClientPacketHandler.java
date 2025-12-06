package de.nike.extramodules2.network;

import codechicken.lib.packet.ICustomPacketHandler;
import codechicken.lib.packet.PacketCustom;
import de.nike.extramodules2.modules.entities.defensebrain.EyeMode;
import de.nike.extramodules2.utils.ModuleHostFinder;
import net.minecraft.client.Minecraft;

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

        }
    }
}
