package com.macuguita.slore.mixin.durability;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ToolMaterials.class)
public class ToolMaterialsMixin {

    // Changing around the mining speed and enchantability of gold, diamond and nethertie
    // now it is more worth it than ever to get netherite!!!
    /*
     * GOLD -> old diamond
     * DIAMOND -> old netherite
     * NETHERITE -> old gold
     */
    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;IIIFFILjava/util/function/Supplier;)Lnet/minecraft/item/ToolMaterials;"
            )
    )
    private static ToolMaterials slore$rebalanceToolSpeedAndEnchantability(String name, int ordinal, int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient, Operation<ToolMaterials> original) {
        switch (name) {
            case "GOLD":
                miningSpeed = 8.0F;
                enchantability = 10;
                break;
            case "DIAMOND":
                miningSpeed = 9.0F;
                enchantability = 15;
                break;
            case "NETHERITE":
                miningSpeed = 12.0F;
                enchantability = 22;
                break;
        }
        return original.call(name, ordinal, miningLevel, itemDurability, miningSpeed, attackDamage, enchantability, repairIngredient);
    }
}
