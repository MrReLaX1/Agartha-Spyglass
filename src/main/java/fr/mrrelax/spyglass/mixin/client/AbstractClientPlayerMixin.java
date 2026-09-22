package fr.mrrelax.spyglass.mixin.client;

import fr.mrrelax.spyglass.client.ZoomController;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
    @Inject(method = "getFieldOfViewModifier", at = @At("HEAD"), cancellable = true)
    private void spyglass$zoomFov(boolean firstPerson, float effectScale, CallbackInfoReturnable<Float> cir) {
        if (firstPerson && (Object) this instanceof LocalPlayer && ZoomController.isZooming()) {
            cir.setReturnValue(ZoomController.fovModifier());
        }
    }
}
