package fr.mrrelax.spyglass.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fr.mrrelax.spyglass.client.SpyglassIllusion;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelExtractor.class)
public abstract class LevelExtractorMixin {
    @WrapMethod(method = "extract")
    private void spyglass$spyglassWhileExtracting(DeltaTracker deltaTracker, Camera camera, float partialTicks, Operation<Void> original) {
        SpyglassIllusion.during(() -> original.call(deltaTracker, camera, partialTicks));
    }
}
