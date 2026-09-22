package fr.mrrelax.spyglass.gametest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.mrrelax.spyglass.gametest.SpyglassAtEye;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerItemInHandLayer.class)
public abstract class SpyglassAtEyeMixin {
    @Inject(method = "submitArmWithItem(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
            at = @At("HEAD"))
    private void spyglassTest$countUsingArm(AvatarRenderState state, ItemStackRenderState itemState, ItemStack stack, HumanoidArm arm,
            PoseStack poseStack, SubmitNodeCollector collector, int light, CallbackInfo ci) {
        if (state.isUsingItem && state.useItemHand.asArm(state.mainArm) == arm && stack.is(Items.SPYGLASS)) {
            SpyglassAtEye.recordUsingArm();
        }
    }

    @Inject(method = "renderItemHeldToEye", at = @At("HEAD"))
    private void spyglassTest$countAtEye(CallbackInfo ci) {
        SpyglassAtEye.recordAtEye();
    }
}
