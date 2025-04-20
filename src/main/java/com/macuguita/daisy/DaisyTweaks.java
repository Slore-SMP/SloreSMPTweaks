package com.macuguita.daisy;

import com.macuguita.daisy.item.ReaperItem;
import com.macuguita.daisy.mixin.reaper.LivingEntityAccessor;
import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.reg.DaisyParticles;
import com.macuguita.daisy.reg.DaisySounds;
import com.macuguita.daisy.reg.DaisyTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaisyTweaks implements ModInitializer {
	public static final String MOD_ID = "daisy";
	public static final String MOD_NAME = "Daisy SMP Tweaks";
	public static final Identifier BUCKET_STACK_PACKET = id("sync_bucket_stacks");

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		DaisyObjects.init();
		DaisyParticles.init();
		DaisySounds.init();

		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity instanceof LivingEntity livingEntity && livingEntity.getMainHandStack().getItem() instanceof ReaperItem) {
				killedEntity.deathTime = 0;
				((LivingEntityAccessor) killedEntity).daisy$setExperienceDroppingDisabled(false);
				ReaperItem.spawnGhostParticle(killedEntity);
				killedEntity.remove(Entity.RemovalReason.KILLED);
			}
		});
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}

	public static boolean isUnbreakable(ItemStack stack) {
		return !stack.isEmpty() && stack.getMaxDamage() > 0 && !stack.isIn(DaisyTags.Items.BREAKABLE);
	}
}
