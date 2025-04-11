package com.macuguita.slore;

import com.macuguita.slore.item.ReaperItem;
import com.macuguita.slore.mixin.buckets.ItemAccessor;
import com.macuguita.slore.mixin.reaper.LivingEntityAccessor;
import com.macuguita.slore.reg.SloreObjects;
import com.macuguita.slore.reg.SloreParticles;
import com.macuguita.slore.reg.SloreSounds;
import com.macuguita.slore.reg.SloreTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SloreTweaks implements ModInitializer {
	public static final String MOD_ID = "slore";
	public static final String MOD_NAME = "Slore SMP Tweaks";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		SloreObjects.init();
		SloreParticles.init();
		SloreSounds.init();

		ServerWorldEvents.LOAD.register((server, world) -> {
			processBuckets(Registries.ITEM.stream().toList());
		});

		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity instanceof LivingEntity livingEntity && livingEntity.getMainHandStack().getItem() instanceof ReaperItem) {
				killedEntity.deathTime = 0;
				((LivingEntityAccessor) killedEntity).slore$setExperienceDroppingDisabled(false);
				ReaperItem.spawnGhostParticle(killedEntity);
				killedEntity.remove(Entity.RemovalReason.KILLED);
			}
		});
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}

	public static boolean isUnbreakable(ItemStack stack) {
		return !stack.isEmpty() && stack.getMaxDamage() > 0 && !stack.isIn(SloreTags.Items.BREAKABLE);
	}

	private void processBuckets(List<Item> items) {
		for (Item item : items) {
			if (isBucket(item)) {
				((ItemAccessor) item).slore$setMaxCount(16);
			}
		}
	}

	@SuppressWarnings("deprecation")
    private boolean isBucket(Item item) {
		Identifier id = Registries.ITEM.getId(item);
		if (item instanceof EntityBucketItem) {
			return false;
		}
		if (item.getRegistryEntry().isIn(SloreTags.Items.BUCKET_BLACKLIST)) {
			return false;
		}
		if (item.getRegistryEntry().isIn(SloreTags.Items.BUCKET)) {
			return true;
		}
		return id.getPath().contains("_bucket");
	}

	public static ItemStack handleStackableBucket(ItemStack stack, PlayerEntity player, ItemStack emptyContainer) {
		if (player == null || player.getAbilities().creativeMode) {
			return stack;
		}

		if (stack.getCount() == 1) {
			stack.decrement(1);
			return emptyContainer;
		}

		stack.decrement(1);
		if (!player.getInventory().insertStack(emptyContainer)) {
			player.dropItem(emptyContainer, false);
		}

		return stack;
	}
}
