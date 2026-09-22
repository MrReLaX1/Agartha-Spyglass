package fr.mrrelax.spyglass.mixin.client;

import fr.mrrelax.spyglass.client.SpyglassIllusion;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Inject(method = "isUsingItem", at = @At("HEAD"), cancellable = true)
    private void spyglass$usingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        if (SpyglassIllusion.appliesTo(this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getUsedItemHand", at = @At("HEAD"), cancellable = true)
    private void spyglass$spyglassHand(CallbackInfoReturnable<InteractionHand> cir) {
        if (SpyglassIllusion.appliesTo(this)) {
            cir.setReturnValue(InteractionHand.MAIN_HAND);
        }
    }
}
