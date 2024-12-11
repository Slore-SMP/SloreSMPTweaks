package net.macuguita.slore.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModFoodComponents {
    public static final FoodComponent BLOBFISH = new FoodComponent.Builder().nutrition(20).saturationModifier(1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300), 1.0f).build();
}
