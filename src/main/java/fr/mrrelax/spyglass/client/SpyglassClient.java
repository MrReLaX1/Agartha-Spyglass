package fr.mrrelax.spyglass.client;

import net.fabricmc.api.ClientModInitializer;

public final class SpyglassClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ZoomController.init();
        RemoteZooms.init();
    }
}
