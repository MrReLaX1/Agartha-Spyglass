package fr.mrrelax.spyglass;

import fr.mrrelax.spyglass.network.PlayerZoomPayload;
import fr.mrrelax.spyglass.network.ZoomStatePayload;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;

final class ZoomServer {
    private static final Set<UUID> ZOOMING = new HashSet<>();

    private ZoomServer() {
    }

    static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ZoomStatePayload.TYPE,
                (payload, context) -> setZooming(context.player(), payload.active()));

        EntityTrackingEvents.START_TRACKING.register((tracked, watcher) -> {
            if (tracked instanceof ServerPlayer player && ZOOMING.contains(player.getUUID())) {
                sendTo(watcher, new PlayerZoomPayload(player.getId(), true));
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> ZOOMING.remove(handler.getPlayer().getUUID()));
    }

    private static void setZooming(ServerPlayer player, boolean active) {
        boolean changed = active ? ZOOMING.add(player.getUUID()) : ZOOMING.remove(player.getUUID());
        if (!changed) {
            return;
        }
        if (!player.isSpectator()) {
            player.playSound(active ? SoundEvents.SPYGLASS_USE : SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
        }
        PlayerZoomPayload update = new PlayerZoomPayload(player.getId(), active);
        for (ServerPlayer watcher : PlayerLookup.tracking(player)) {
            sendTo(watcher, update);
        }
    }

    private static void sendTo(ServerPlayer watcher, PlayerZoomPayload payload) {
        if (ServerPlayNetworking.canSend(watcher, PlayerZoomPayload.TYPE)) {
            ServerPlayNetworking.send(watcher, payload);
        }
    }
}
