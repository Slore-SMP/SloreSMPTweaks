package com.macuguita.slore.mixin.durability;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.macuguita.slore.SloreTweaks;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@WrapOperation(
			method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"
			)
	)
	private boolean slore$unbreakablesSendDurabilityChangeAdvancement(ItemStack instance, Operation<Boolean> original, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
		if (entity instanceof ServerPlayerEntity player && SloreTweaks.isUnbreakable(instance)) {
			Criteria.ITEM_DURABILITY_CHANGED.trigger(player, instance, instance.getDamage());
		}

		return original.call(instance);
	}

	@ModifyReturnValue(
			method = "isDamageable",
			at = @At("RETURN")
	)
	private boolean slore$unbreakableItemsDoNotHaveUnbreakableProperty(boolean original) {
		if (SloreTweaks.isUnbreakable((ItemStack) (Object) this)) {
			return false;
		}
		return original;
	}
}
