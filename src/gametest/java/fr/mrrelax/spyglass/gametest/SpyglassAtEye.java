package fr.mrrelax.spyglass.gametest;

import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;

public final class SpyglassAtEye {
    private static final AtomicInteger USING = new AtomicInteger();
    private static final AtomicInteger AT_EYE = new AtomicInteger();

    private SpyglassAtEye() {
    }

    public static void recordUsingArm() {
        USING.incrementAndGet();
    }

    public static void recordAtEye() {
        AT_EYE.incrementAndGet();
    }

    public static void assertHeldToEye(ClientGameTestContext context, int ticks, String situation) {
        assertHeldToEye(context, ticks, situation, () -> { });
    }

    public static void assertHeldToEye(ClientGameTestContext context, int ticks, String situation, Runnable eachTick) {
        USING.set(0);
        AT_EYE.set(0);
        for (int i = 0; i < ticks; i++) {
            eachTick.run();
            context.waitTick();
        }
        int using = USING.get();
        int atEye = AT_EYE.get();
        System.out.printf("[spyglass-test] %s: spyglass at the eye in %d/%d frames%n", situation, atEye, using);
        if (using == 0 || atEye < using * 0.9) {
            throw new AssertionError(situation + ": spyglass at the eye in only " + atEye + "/" + using + " frames");
        }
    }
}
