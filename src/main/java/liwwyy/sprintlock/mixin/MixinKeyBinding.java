package liwwyy.sprintlock.mixin;

import liwwyy.sprintlock.config.SprintLockConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * PolySprint-style input locking for the sprint key.
 *
 * SprintLock works on the KeyBinding state rather than the player's sprint
 * state. This leaves Minecraft's normal movement and sprint packet path alone.
 */
@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {
    @Inject(method = "setKeyBindState", at = @At("HEAD"), cancellable = true)
    private static void sprintlock$keepSprintKeyDown(int keyCode, boolean pressed, CallbackInfo ci) {
        if (!SprintLockConfig.enabled || pressed) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null || minecraft.gameSettings == null || minecraft.gameSettings.keyBindSprint == null) {
            return;
        }

        if (keyCode == minecraft.gameSettings.keyBindSprint.getKeyCode()) {
            ci.cancel();
        }
    }
}
