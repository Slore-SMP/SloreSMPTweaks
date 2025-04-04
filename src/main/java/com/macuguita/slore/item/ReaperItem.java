package com.macuguita.slore.item;

import com.macuguita.slore.SloreTweaks;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;

public class ReaperItem extends MiningToolItem {

    public ReaperItem(float attackDamage, float attackSpeed, ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }

    public static void spawnGhostParticle(LivingEntity hitEntity) {
        double deltaX = -MathHelper.sin((float) (double) hitEntity.getYaw() * 0.017453292F);
        double deltaZ = MathHelper.cos((float) (double) hitEntity.getYaw() * 0.017453292F);
        if (hitEntity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(SloreTweaks.GHOST_PARTICLE, hitEntity.getX(), hitEntity.getEyeY(), hitEntity.getZ(), 0, deltaX * 0.15, 0.25, deltaZ * 0.15, 0.15);
        }
    }
}
