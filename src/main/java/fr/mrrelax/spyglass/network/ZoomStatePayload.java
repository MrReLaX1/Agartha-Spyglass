package fr.mrrelax.spyglass.network;

import fr.mrrelax.spyglass.Spyglass;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ZoomStatePayload(boolean active) implements CustomPacketPayload {
    public static final Type<ZoomStatePayload> TYPE = new Type<>(Spyglass.id("zoom_state"));
    public static final StreamCodec<ByteBuf, ZoomStatePayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.BOOL, ZoomStatePayload::active, ZoomStatePayload::new);

    @Override
    public Type<ZoomStatePayload> type() {
        return TYPE;
    }
}
