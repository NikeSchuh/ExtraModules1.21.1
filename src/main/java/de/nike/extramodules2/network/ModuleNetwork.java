package de.nike.extramodules2.network;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.packet.PacketCustomChannel;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.entities.projectiles.DraconicLightningChain;
import de.nike.extramodules2.modules.entities.defensebrain.EyeMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;

public class ModuleNetwork {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(ExtraModules2.MODID, "emnetwork");

    public static PacketCustomChannel networkChannel;

    public static void sendBrainEyeChange(ServerPlayer target, EyeMode mode) {
        PacketCustom packetCustom = new PacketCustom(CHANNEL, ModuleProtocol.S_DEFENSE_BRAIN_EYE_MODE_CHANGE, null);
        packetCustom.writeVarInt(mode.getValue());
        packetCustom.sendToPlayer(target);
    }

    public static void sendBrainEyeRageState(ServerPlayer target, boolean rageMode) {
        PacketCustom packetCustom = new PacketCustom(CHANNEL, ModuleProtocol.S_DEFENSE_BRAIN_RAGE_PROGRESS_CHANGE, null);
        packetCustom.writeBoolean(rageMode);
        packetCustom.sendToPlayer(target);
    }

    public static void sendBrainRageChange(ServerPlayer target, float rageChargeProgress) {
        PacketCustom packetCustom = new PacketCustom(CHANNEL, ModuleProtocol.S_DEFENSE_BRAIN_RAGE_PROGRESS_CHANGE, null);
        packetCustom.writeFloat(rageChargeProgress);
        packetCustom.sendToPlayer(target);
    }

    public static void sendLightningChain(DraconicLightningChain chain) {
        PacketCustom packetCustom = new PacketCustom(CHANNEL, ModuleProtocol.S_ADD_CHAIN, null);

        packetCustom.writeDouble(chain.position().x);
        packetCustom.writeDouble(chain.position().y);
        packetCustom.writeDouble(chain.position().z);

        packetCustom.writeInt(chain.getStartEntity().getId());
        packetCustom.writeInt(chain.getEndEntity().getId());
        packetCustom.writeInt(chain.getLightningColor());

        packetCustom.sendToAllAround(chain.blockPosition(), 64F, chain.level().dimension());
    }


    public static void init(IEventBus modEventBus) {
        networkChannel = new PacketCustomChannel(CHANNEL)
                .client(()->ClientPacketHandler::new)
                .server(()->ServerPacketHandler::new);

        networkChannel.init(modEventBus);
    }

}
