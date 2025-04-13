package com.dark_lion_jp.wheel_jump.mixin;

import java.lang.Math;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

  // Inject into the onMouseScroll method at the beginning (HEAD), allowing cancellation.
  @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
  private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo callbackInfo) {
    MinecraftClient client = MinecraftClient.getInstance();

    // Only handle vertical scroll input
    if (vertical != 0) {
      PlayerInventory inventory = client.player.getInventory();
      int currentSlot = inventory.getSelectedSlot();

      // Determine scroll direction: +1 for down, -1 for up
      int delta = (int) Math.signum(vertical);

      // Update selected hotbar slot with wrapping (0–8)
      inventory.setSelectedSlot(((currentSlot + delta) % 9 + 9) % 9);

      // Trigger jump if in-game (no screen open) and the player is on the ground
      if (
        client.currentScreen == null &&
        client.player != null &&
        client.player.isOnGround()
      ) {
        client.player.jump();
      }
    }
  }
}
