package com.macuguita.daisy.mixin.reaper.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.macuguita.daisy.DaisyTweaks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {

    @Shadow protected abstract void addModel(ModelIdentifier modelId);

    @Definition(id = "addModel", method = "Lnet/minecraft/client/render/model/ModelLoader;addModel(Lnet/minecraft/client/util/ModelIdentifier;)V")
    @Definition(id = "SPYGLASS_IN_HAND", field = "Lnet/minecraft/client/render/item/ItemRenderer;SPYGLASS_IN_HAND:Lnet/minecraft/client/util/ModelIdentifier;")
    @Expression("this.addModel(SPYGLASS_IN_HAND)")
    @Inject(
            method = "<init>",
            at = @At(
                    value = "MIXINEXTRAS:EXPRESSION",
                    shift = At.Shift.AFTER
            )
    )
    public void daisy$addReaper(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<ModelLoader.SourceTrackedData>> blockStates, CallbackInfo ci) {
        this.addModel(new ModelIdentifier(DaisyTweaks.MOD_ID, "reaper_handheld", "inventory"));
    }
}
