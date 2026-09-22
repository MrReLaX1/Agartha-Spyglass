package fr.mrrelax.spyglass.mixin.client;

import fr.mrrelax.spyglass.client.SpyglassIllusion;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
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

    @Inject(method = "getUseItem", at = @At("HEAD"), cancellable = true)
    private void spyglass$spyglassInUse(CallbackInfoReturnable<ItemStack> cir) {
        if (SpyglassIllusion.appliesTo(this)) {
            cir.setReturnValue(SpyglassIllusion.spyglass());
        }
    }

    @Inject(method = "getUseItemRemainingTicks", at = @At("HEAD"), cancellable = true)
    private void spyglass$spyglassTicksLeft(CallbackInfoReturnable<Integer> cir) {
        if (SpyglassIllusion.appliesTo(this)) {
            cir.setReturnValue(SpyglassIllusion.USE_TICKS);
        }
    }

    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    private void spyglass$spyglassInMainHand(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot == EquipmentSlot.MAINHAND && SpyglassIllusion.appliesTo(this)) {
            cir.setReturnValue(SpyglassIllusion.spyglass());
        }
    }
}
