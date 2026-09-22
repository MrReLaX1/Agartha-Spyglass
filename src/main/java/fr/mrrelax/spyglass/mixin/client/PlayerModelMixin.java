package fr.mrrelax.spyglass.mixin.client;

import fr.mrrelax.spyglass.client.VanillaArmPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerModel.class, priority = 2000)
public abstract class PlayerModelMixin {
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("RETURN"))
    private void spyglass$exactSpyglassArm(AvatarRenderState state, CallbackInfo ci) {
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
        if (state.rightArmPose == HumanoidModel.ArmPose.SPYGLASS) {
            spyglass$restore(model, HumanoidArm.RIGHT);
        }
        if (state.leftArmPose == HumanoidModel.ArmPose.SPYGLASS) {
            spyglass$restore(model, HumanoidArm.LEFT);
        }
    }

    private static void spyglass$restore(HumanoidModel<?> model, HumanoidArm arm) {
        VanillaArmPose vanilla = (VanillaArmPose) model;
        ModelPart part = model.getArm(arm);
        part.xRot = vanilla.spyglass$vanillaArmX(arm);
        part.yRot = vanilla.spyglass$vanillaArmY(arm);
    }
}
