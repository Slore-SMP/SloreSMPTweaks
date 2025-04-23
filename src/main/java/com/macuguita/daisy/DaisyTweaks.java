/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy;

import com.macuguita.daisy.chatminigame.ChatMinigame;
import com.macuguita.daisy.chatminigame.ChatMinigameCommand;
import com.macuguita.daisy.chatminigame.DatapackQuestionLoader;
import com.macuguita.daisy.item.ReaperItem;
import com.macuguita.daisy.mixin.reaper.LivingEntityAccessor;
import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.reg.DaisyParticles;
import com.macuguita.daisy.reg.DaisySounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaisyTweaks implements ModInitializer {
	public static final String MOD_ID = "daisy";
	public static final String MOD_NAME = "Daisy SMP Tweaks";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		DaisyObjects.init();
		DaisyParticles.init();
		DaisySounds.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA)
				.registerReloadListener(new DatapackQuestionLoader());
		ChatMinigame.init();
		ChatMinigameCommand.init();

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
}
