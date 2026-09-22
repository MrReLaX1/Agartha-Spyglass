package fr.mrrelax.spyglass.client;

import fr.mrrelax.spyglass.network.PlayerZoomPayload;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class RemoteZooms {
    private static final IntSet ZOOMING = new IntOpenHashSet();

    private RemoteZooms() {
    }

    static void init() {
        ClientPlayNetworking.registerGlobalReceiver(PlayerZoomPayload.TYPE, (payload, context) -> {
            if (payload.active()) {
                ZOOMING.add(payload.entityId());
            } else {
                ZOOMING.remove(payload.entityId());
            }
        });
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, level) -> ZOOMING.remove(entity.getId()));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, minecraft) -> ZOOMING.clear());
    }

    public static boolean isZooming(int entityId) {
        return ZOOMING.contains(entityId);
    }
}
