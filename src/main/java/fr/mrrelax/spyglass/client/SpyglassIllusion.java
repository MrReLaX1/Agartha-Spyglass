package fr.mrrelax.spyglass.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class SpyglassIllusion {
    public static final int USE_TICKS = 1200;

    private static ItemStack spyglass;
    private static int depth;

    private SpyglassIllusion() {
    }

    public static void during(Runnable phase) {
        if (depth == 0) {
            faceWhereLooking();
        }
        depth++;
        try {
            phase.run();
        } finally {
            depth--;
        }
    }

    public static boolean appliesTo(Object entity) {
        return depth > 0 && entity instanceof AbstractClientPlayer player && shows(player);
    }

    private static void faceWhereLooking() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        for (AbstractClientPlayer player : level.players()) {
            if (modelVisible(player) && (shows(player) || player.isScoping())) {
                player.yBodyRotO = player.yHeadRotO;
                player.yBodyRot = player.yHeadRot;
            }
        }
    }

    private static boolean shows(AbstractClientPlayer player) {
        if (player instanceof LocalPlayer) {
            return ZoomController.isZooming() && modelVisible(player);
        }
        return RemoteZooms.isZooming(player.getId());
    }

    private static boolean modelVisible(AbstractClientPlayer player) {
        return !(player instanceof LocalPlayer) || !Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

    public static ItemStack spyglass() {
        if (spyglass == null) {
            spyglass = new ItemStack(Items.SPYGLASS);
        }
        return spyglass;
    }
}
