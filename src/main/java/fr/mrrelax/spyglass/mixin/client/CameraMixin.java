package fr.mrrelax.spyglass.mixin.client;

import fr.mrrelax.spyglass.client.ZoomController;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @ModifyConstant(method = "tickFov", constant = @Constant(floatValue = 0.1F))
    private float spyglass$allowDeeperZoom(float vanillaMin) {
        return Math.min(vanillaMin, ZoomController.MIN_FOV_MODIFIER);
    }
}
