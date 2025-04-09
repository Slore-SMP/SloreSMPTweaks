package com.macuguita.slore.mixin.durability;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.EnumMap;
import java.util.function.Supplier;

@Mixin(ArmorMaterials.class)
public class ArmorMaterialsMixin {

    // Changing around the enchantability of gold, diamond and nethertie
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
                target = "(Ljava/lang/String;ILjava/lang/String;ILjava/util/EnumMap;ILnet/minecraft/sound/SoundEvent;FFLjava/util/function/Supplier;)Lnet/minecraft/item/ArmorMaterials;"
            )
    )
    private static ArmorMaterials slore$rebalanceEquipmentEnchantability(String enumName, int ordinal, String name, int durabilityMultiplier, EnumMap protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier repairIngredientSupplier, Operation<ArmorMaterials> original) {
        switch (enumName) {
            case "GOLD":
                enchantability = 10;
                break;
            case "DIAMOND":
                enchantability = 15;
                break;
            case "NETHERITE":
                enchantability = 25;
                break;
        }
        return original.call(enumName, ordinal, name, durabilityMultiplier, protectionAmounts, enchantability, equipSound, toughness, knockbackResistance, repairIngredientSupplier);
    }
}
