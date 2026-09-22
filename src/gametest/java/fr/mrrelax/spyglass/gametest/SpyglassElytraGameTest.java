package fr.mrrelax.spyglass.gametest;

import com.mojang.blaze3d.platform.InputConstants;
import fr.mrrelax.spyglass.client.ZoomController;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.TestInput;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.CameraType;

public class SpyglassElytraGameTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        context.runOnClient(minecraft -> minecraft.options.pauseOnLostFocus = false);
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            world.getServer().runCommand("time set noon");
            world.getServer().runCommand("gamemode survival @a");
            world.getServer().runCommand("item replace entity @a armor.chest with minecraft:elytra");
            world.getServer().runCommand("effect give @a minecraft:resistance 600 4 true");
            world.getConnection().waitForChunksRender();
            context.runOnClient(minecraft -> minecraft.options.setCameraType(CameraType.THIRD_PERSON_FRONT));
            context.waitTicks(20);
            TestInput input = context.getInput();
            InputConstants.Key spyglassKey = InputConstants.Type.KEYBOARD.getOrCreate(InputConstants.KEY_C);

            input.holdKey(spyglassKey);
            context.waitTicks(20);
            SpyglassAtEye.assertHeldToEye(context, 10, "zoom on the ground");
            takeOff(context, world, input);
            context.waitTicks(20);
            if (!context.computeOnClient(minecraft -> ZoomController.isZooming() && minecraft.player.isFallFlying())) {
                throw new AssertionError("The zoom should be active during flight");
            }
            SpyglassAtEye.assertHeldToEye(context, 10, "zoom during flight");
            context.takeScreenshot("spyglass-flight-1-zoom");
            SpyglassAtEye.assertHeldToEye(context, 8, "zoom during flight, looking up", () -> input.moveCursor(0, -25));
            SpyglassAtEye.assertHeldToEye(context, 8, "zoom during flight, looking down", () -> input.moveCursor(0, 50));
            SpyglassAtEye.assertHeldToEye(context, 10, "zoom during flight, turning the camera", () -> input.moveCursor(20, 0));
            context.takeScreenshot("spyglass-flight-2-zoom-turning");
            input.releaseKey(spyglassKey);
            context.waitTicks(10);

            world.getServer().runCommand("item replace entity @a weapon.mainhand with minecraft:spyglass");
            takeOff(context, world, input);
            context.waitTicks(10);
            input.holdKey(options -> options.keyUse);
            context.waitTicks(20);
            SpyglassAtEye.assertHeldToEye(context, 10, "real spyglass during flight");
            context.takeScreenshot("spyglass-flight-3-real");
            SpyglassAtEye.assertHeldToEye(context, 10, "real spyglass during flight, turning the camera", () -> input.moveCursor(20, 0));
            input.releaseKey(options -> options.keyUse);

            world.getServer().runCommand("item replace entity @a armor.chest with minecraft:air");
            world.getServer().runCommand("execute as @a at @s run tp @s ~ -60 ~ ~ 0");
            context.waitTicks(30);
            input.holdKey(options -> options.keyUse);
            context.waitTicks(20);
            SpyglassAtEye.assertHeldToEye(context, 12, "real spyglass on the ground, turning the camera", () -> input.moveCursor(25, 0));
            context.takeScreenshot("spyglass-ground-real-turning");
            input.releaseKey(options -> options.keyUse);
            context.waitTicks(5);
        }
    }

    private static void takeOff(ClientGameTestContext context, TestSingleplayerContext world, TestInput input) {
        world.getServer().runCommand("execute as @a at @s run tp @s ~ ~160 ~ ~ 10");
        context.waitTicks(10);
        for (int attempt = 0; attempt < 5 && !context.computeOnClient(minecraft -> minecraft.player.isFallFlying()); attempt++) {
            input.holdKeyFor(options -> options.keyJump, 2);
            context.waitTicks(3);
        }
        context.waitFor(minecraft -> minecraft.player.isFallFlying(), 40);
    }
}
