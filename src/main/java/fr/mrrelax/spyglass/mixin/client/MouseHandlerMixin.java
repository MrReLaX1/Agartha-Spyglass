package fr.mrrelax.spyglass.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.mrrelax.spyglass.client.ZoomController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @WrapOperation(method = "turnPlayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private void spyglass$slowTurn(LocalPlayer player, double dx, double dy, Operation<Void> original) {
        double factor = ZoomController.sensitivityFactor();
        original.call(player, dx * factor, dy * factor);
    }

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void spyglass$scrollZoom(long handle, double xOffset, double yOffset, CallbackInfo ci) {
        if (handle == Minecraft.getInstance().getWindow().handle() && ZoomController.onScroll(yOffset)) {
            ci.cancel();
        }
    }
}
