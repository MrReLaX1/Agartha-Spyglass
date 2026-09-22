package fr.mrrelax.spyglass;

import fr.mrrelax.spyglass.network.PlayerZoomPayload;
import fr.mrrelax.spyglass.network.ZoomStatePayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.Identifier;

public final class Spyglass implements ModInitializer {
    public static final String MOD_ID = "spyglass";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.serverboundPlay().register(ZoomStatePayload.TYPE, ZoomStatePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(PlayerZoomPayload.TYPE, PlayerZoomPayload.CODEC);
        ZoomServer.init();
    }
}
