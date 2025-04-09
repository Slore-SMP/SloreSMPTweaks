package com.macuguita.slore.mixin.durability;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.macuguita.slore.SloreTweaks;
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
	private ItemStack slore$unbreakableItems(ItemStack original, ItemStack stack, LootContext context) {
		if (SloreTweaks.isUnbreakable(stack)) {
			return stack;
		} else {
			return original;
		}
	}
}
