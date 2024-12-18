package net.macuguita.slore.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MaceItem.class)
public class MaceItemMixin {

	// Modify the return value of getBonusAttackDamage to adjust the damage based on fall distance
	@ModifyReturnValue(method = "getBonusAttackDamage", at = @At(value = "RETURN", ordinal = 2))
	private float modifyMaceDamage(float original, Entity target, float baseAttackDamage, DamageSource damageSource, @Local LivingEntity living) {
		// If the target is a LivingEntity (player or mob)
		if (living != null) {
			// Calculate damage based on fall distance
			// Return the fall damage as the new attack damage
			return (float) (5 * Math.log(living.fallDistance + 1));
		}
		return original;  // If no LivingEntity, return the original damage (fallback)
	}
}
