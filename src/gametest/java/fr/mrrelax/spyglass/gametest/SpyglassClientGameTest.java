package fr.mrrelax.spyglass.gametest;

import com.mojang.blaze3d.platform.InputConstants;
import fr.mrrelax.spyglass.client.ZoomController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.TestInput;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.repository.PackRepository;

public class SpyglassClientGameTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        context.runOnClient(minecraft -> minecraft.options.pauseOnLostFocus = false);
        enableCompatPacks(context);

        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            world.getServer().runCommand("time set noon");
            world.getServer().runCommand("execute as @p at @s run summon minecraft:villager ~ ~ ~20 {NoAI:1b}");
            world.getConnection().waitForChunksRender();

            TestInput input = context.getInput();
            BlockPos villager = context.computeOnClient(minecraft -> minecraft.player.blockPosition().offset(0, 1, 20));
            input.lookAt(villager);
            context.waitTicks(5);
            context.takeScreenshot("spyglass-1-normal");

            InputConstants.Key spyglassKey = InputConstants.Type.KEYBOARD.getOrCreate(InputConstants.KEY_C);
            input.holdKey(spyglassKey);
            context.waitTicks(20);
            if (!context.computeOnClient(minecraft -> ZoomController.isZooming())) {
                throw new AssertionError("C is held but the zoom is not active");
            }
            context.takeScreenshot("spyglass-2-zoom");

            input.scroll(4);
            context.waitTicks(20);
            context.takeScreenshot("spyglass-3-scroll-zoom");

            input.pressKey(options -> options.keyTogglePerspective);
            input.pressKey(options -> options.keyTogglePerspective);
            context.waitTicks(20);
            SpyglassAtEye.assertHeldToEye(context, 10, "third person, front view");
            context.takeScreenshot("spyglass-4-front-view");

            SpyglassAtEye.assertHeldToEye(context, 10, "third person, turning the camera", () -> input.moveCursor(25, 0));
            context.takeScreenshot("spyglass-4b-front-view-turning");

            input.releaseKey(spyglassKey);
            context.waitTicks(20);
            if (context.computeOnClient(minecraft -> ZoomController.isZooming())) {
                throw new AssertionError("C is released but the zoom is still active");
            }
            context.takeScreenshot("spyglass-5-released");
        }
    }

    private static void enableCompatPacks(ClientGameTestContext context) {
        boolean added = context.computeOnClient(minecraft -> {
            PackRepository packs = minecraft.getResourcePackRepository();
            packs.reload();
            List<String> selected = new ArrayList<>(packs.getSelectedIds());
            List<String> extra = packs.getAvailableIds().stream()
                    .filter(id -> id.startsWith("file/") && !selected.contains(id))
                    .sorted(Comparator.comparing((String id) -> id.contains("FA+")).thenComparing(id -> id))
                    .toList();
            selected.addAll(extra);
            packs.setSelected(selected);
            return !extra.isEmpty();
        });
        if (added) {
            context.runOnClient(minecraft -> minecraft.reloadResourcePacks());
            context.waitFor(minecraft -> minecraft.gui.overlay() == null);
        }
    }
}
