package com.macuguita.slore.mixin.reaper.client;

import com.macuguita.slore.SloreTweaks;
import com.macuguita.slore.reg.SloreObjects;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow public abstract ItemModels getModels();

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    public BakedModel slore$useBigReaperModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
        boolean inHand = renderMode.isFirstPerson() || renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND || renderMode == ModelTransformationMode.HEAD || renderMode == ModelTransformationMode.FIXED;
        if (stack.isOf(SloreObjects.REAPER.get())) {
            return this.getModels().getModelManager().getModel(!inHand ? new ModelIdentifier(SloreTweaks.MOD_ID, "reaper", "inventory") : new ModelIdentifier(SloreTweaks.MOD_ID, "reaper_handheld", "inventory"));
        }
        return value;
    }
}
