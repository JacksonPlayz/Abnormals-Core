package com.teamabnormals.abnormals_core.common.network;

import com.teamabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.teamabnormals.abnormals_core.core.AbnormalsCore;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Message for updating if the Slabfish hat is enabled.
 *
 * @author Jackson
 */
public final class MessageC2SUpdateSlabfishHat {
	private final boolean enabled;

	public MessageC2SUpdateSlabfishHat(boolean enabled) {
		this.enabled = enabled;
	}

	public void serialize(PacketBuffer buf) {
		buf.writeBoolean(this.enabled);
	}

	public static MessageC2SUpdateSlabfishHat deserialize(PacketBuffer buf) {
		return new MessageC2SUpdateSlabfishHat(buf.readBoolean());
	}

	public static void handle(MessageC2SUpdateSlabfishHat message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				ServerPlayerEntity player = context.getSender();
				IDataManager data = (IDataManager) player;
				if(data != null) data.setValue(AbnormalsCore.SLABFISH_HEAD, message.isEnabled());
			});
			context.setPacketHandled(true);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}
}