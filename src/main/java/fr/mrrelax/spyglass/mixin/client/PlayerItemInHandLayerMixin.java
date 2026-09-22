package fr.mrrelax.spyglass.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fr.mrrelax.spyglass.client.VanillaArmPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerItemInHandLayer.class)
public abstract class PlayerItemInHandLayerMixin {
    private static final float ARM_AT_EYE_TOLERANCE = 0.3F;

    @ModifyExpressionValue(
            method = "submitArmWithItem(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;isEmpty()Z", ordinal = 1))
    private boolean spyglass$keepInHandUntilRaised(boolean nothingAtEye, @Local(argsOnly = true) HumanoidArm arm) {
        return nothingAtEye || !spyglass$armAtEye(arm);
    }

    @WrapOperation(method = "renderItemHeldToEye", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"))
    private float spyglass$followHeadInFlight(float headXRot, float min, float max, Operation<Float> original,
            @Local(argsOnly = true) AvatarRenderState state) {
        return state.isFallFlying ? headXRot : original.call(headXRot, min, max);
    }

    private boolean spyglass$armAtEye(HumanoidArm arm) {
        if (!(((RenderLayer<?, ?>) (Object) this).getParentModel() instanceof HumanoidModel<?> model)
                || !(model instanceof VanillaArmPose vanilla)) {
            return true;
        }
        ModelPart part = model.getArm(arm);
        return Math.abs(part.xRot - vanilla.spyglass$vanillaArmX(arm)) < ARM_AT_EYE_TOLERANCE
                && Math.abs(part.yRot - vanilla.spyglass$vanillaArmY(arm)) < ARM_AT_EYE_TOLERANCE;
    }
}
