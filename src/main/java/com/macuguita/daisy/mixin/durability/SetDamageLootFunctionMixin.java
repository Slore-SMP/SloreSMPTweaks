package com.macuguita.daisy.mixin.durability;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.macuguita.daisy.DaisyTweaks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetDamageLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SetDamageLootFunction.class)
public class SetDamageLootFunctionMixin {

	@ModifyReturnValue(
			method = "process",
			at = @At("RETURN")
	)
	private ItemStack daisy$unbreakableItems(ItemStack original, ItemStack stack, LootContext context) {
		if (DaisyTweaks.isUnbreakable(stack)) {
			return stack;
		} else {
			return original;
		}
	}
}
