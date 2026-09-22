package fr.mrrelax.spyglass.network;

import fr.mrrelax.spyglass.Spyglass;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PlayerZoomPayload(int entityId, boolean active) implements CustomPacketPayload {
    public static final Type<PlayerZoomPayload> TYPE = new Type<>(Spyglass.id("player_zoom"));
    public static final StreamCodec<ByteBuf, PlayerZoomPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, PlayerZoomPayload::entityId,
            ByteBufCodecs.BOOL, PlayerZoomPayload::active,
            PlayerZoomPayload::new);

    @Override
    public Type<PlayerZoomPayload> type() {
        return TYPE;
    }
}
