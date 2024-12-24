package net.macuguita.slore.mixin;

import net.macuguita.slore.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Unique
    private static final UUID DEVELOPER_UUID = UUID.fromString("0e56050b-ee27-478a-a345-d2b384919081");  // macuguita
    private static final UUID LADYBRINE_UUID = UUID.fromString("5d66606c-949c-47ce-ba4c-a1b9339ba3c8");

    // Hook into the dropInventory method to add custom drops
    @Inject(method = "dropInventory()V", at = @At("TAIL"))
    private void onDropInventory(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Check if the player's UUID matches the developer's UUID
        if (player.getUuid().equals(DEVELOPER_UUID)) {
            ItemStack customDrop = ModItems.BLOBFISH.getDefaultStack();
            player.dropStack(customDrop);
        }
        if (player.getUuid().equals(LADYBRINE_UUID)) {
            ItemStack customDrop = ModItems.LADYBRINES_FART.getDefaultStack();
            player.dropStack(customDrop);
        }
    }
}