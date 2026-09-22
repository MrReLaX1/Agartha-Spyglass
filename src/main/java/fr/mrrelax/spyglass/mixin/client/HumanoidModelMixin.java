package fr.mrrelax.spyglass.mixin.client;

import fr.mrrelax.spyglass.client.VanillaArmPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin implements VanillaArmPose {
    @Unique
    private float spyglass$rightX;
    @Unique
    private float spyglass$rightY;
    @Unique
    private float spyglass$leftX;
    @Unique
    private float spyglass$leftY;

    @Inject(method = "poseRightArm", at = @At("TAIL"))
    private void spyglass$rightSpyglassInFlight(HumanoidRenderState state, CallbackInfo ci) {
        if (state.isFallFlying && state.rightArmPose == HumanoidModel.ArmPose.SPYGLASS) {
            HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
            model.rightArm.xRot = model.head.xRot - 1.9198622F;
        }
    }

    @Inject(method = "poseLeftArm", at = @At("TAIL"))
    private void spyglass$leftSpyglassInFlight(HumanoidRenderState state, CallbackInfo ci) {
        if (state.isFallFlying && state.leftArmPose == HumanoidModel.ArmPose.SPYGLASS) {
            HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
            model.leftArm.xRot = model.head.xRot - 1.9198622F;
        }
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("RETURN"))
    private void spyglass$rememberVanillaArms(HumanoidRenderState state, CallbackInfo ci) {
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
        spyglass$rightX = model.rightArm.xRot;
        spyglass$rightY = model.rightArm.yRot;
        spyglass$leftX = model.leftArm.xRot;
        spyglass$leftY = model.leftArm.yRot;
    }

    @Override
    public float spyglass$vanillaArmX(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? spyglass$rightX : spyglass$leftX;
    }

    @Override
    public float spyglass$vanillaArmY(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? spyglass$rightY : spyglass$leftY;
    }
}
