package fr.mrrelax.spyglass.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import fr.mrrelax.spyglass.client.SpyglassIllusion;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @WrapMethod(method = "render")
    private void spyglass$spyglassWhileRendering(GraphicsResourceAllocator allocator, boolean renderOutline,
            CameraRenderState camera, GpuBufferSlice fog, Vector4f fogColor, boolean renderSky, boolean renderBlockOutline,
            Operation<Void> original) {
        SpyglassIllusion.during(() -> original.call(allocator, renderOutline, camera, fog, fogColor, renderSky, renderBlockOutline));
    }
}
