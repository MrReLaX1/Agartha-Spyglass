package fr.mrrelax.spyglass.client;

import com.mojang.blaze3d.platform.InputConstants;
import fr.mrrelax.spyglass.Spyglass;
import fr.mrrelax.spyglass.network.ZoomStatePayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

public final class ZoomController {
    private static final float DEFAULT_ZOOM = 4.0F;
    private static final float MIN_ZOOM = DEFAULT_ZOOM;
    private static final float MAX_ZOOM = 230.66F;
    private static final float SCROLL_STEP = 1.2F;
    public static final float MIN_FOV_MODIFIER = 1.0F / MAX_ZOOM;

    private static KeyMapping spyglassKey;
    private static boolean active;
    private static float zoom = DEFAULT_ZOOM;

    private ZoomController() {
    }

    static void init() {
        KeyMapping.Category category = KeyMapping.Category.register(Spyglass.id("main"));
        spyglassKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.spyglass.zoom", InputConstants.Type.KEYBOARD, InputConstants.KEY_C, category));
        ClientTickEvents.END_CLIENT_TICK.register(ZoomController::tick);
    }

    private static void tick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        boolean wanted = player != null && minecraft.gui.screen() == null && spyglassKey.isDown();
        if (wanted == active) {
            return;
        }
        active = wanted;
        if (active) {
            zoom = DEFAULT_ZOOM;
        }
        if (player != null) {
            player.playSound(active ? SoundEvents.SPYGLASS_USE : SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
            if (ClientPlayNetworking.canSend(ZoomStatePayload.TYPE)) {
                ClientPlayNetworking.send(new ZoomStatePayload(active));
            }
        }
    }

    public static boolean isZooming() {
        return active;
    }

    public static boolean isZoomingFirstPerson() {
        return active && Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

    public static float fovModifier() {
        return 1.0F / zoom;
    }

    public static double sensitivityFactor() {
        if (!isZoomingFirstPerson()) {
            return 1.0;
        }
        return Math.min(1.0, 1.25 / zoom);
    }

    public static boolean onScroll(double amount) {
        if (!isZoomingFirstPerson() || amount == 0.0 || Minecraft.getInstance().gui.screen() != null) {
            return false;
        }
        zoom = Mth.clamp(zoom * (float) Math.pow(SCROLL_STEP, amount), MIN_ZOOM, MAX_ZOOM);
        return true;
    }
}
