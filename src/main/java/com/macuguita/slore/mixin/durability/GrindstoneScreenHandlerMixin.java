package com.macuguita.slore.mixin.durability;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.macuguita.slore.SloreTweaks;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {

	@WrapOperation(
			method = "updateResult",
			at = @At(
					value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"
			)
	)
	private boolean slore$modifyUpdateResult(ItemStack left, ItemStack right, Operation<Boolean> original) {
		var condition = original.call(left, right) && (SloreTweaks.isUnbreakable(left) || SloreTweaks.isUnbreakable(right));
		return condition? !condition : original.call(left, right);
	}
}
