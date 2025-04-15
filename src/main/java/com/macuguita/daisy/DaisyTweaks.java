package com.macuguita.daisy;

import com.macuguita.daisy.item.ReaperItem;
import com.macuguita.daisy.mixin.buckets.ItemAccessor;
import com.macuguita.daisy.mixin.reaper.LivingEntityAccessor;
import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.reg.DaisyParticles;
import com.macuguita.daisy.reg.DaisySounds;
import com.macuguita.daisy.reg.DaisyTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

		// Here we tell the server the bucket sizes (i think)…
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            processBuckets();
		});

		// When player joins we tell them the new stack sizes of the bucket items
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			syncBucketStacksToPlayer(handler.getPlayer());
		});

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

	public static void processBuckets() {
		for (Item item : Registries.ITEM) {
			if (isBucket(item)) {
				((ItemAccessor) item).daisy$setMaxCount(16);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static boolean isBucket(Item item) {
		Identifier id = Registries.ITEM.getId(item);
		if (item instanceof EntityBucketItem) {
			return false;
		}
		if (item.getRegistryEntry().isIn(DaisyTags.Items.BUCKET_BLACKLIST)) {
			return false;
		}
		if (item.getRegistryEntry().isIn(DaisyTags.Items.BUCKET)) {
			return true;
		}
		return id.getPath().contains("_bucket");
	}

	public static void syncBucketStacksToPlayer(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();

		List<Identifier> bucketIds = Registries.ITEM.stream()
				.filter(DaisyTweaks::isBucket)
				.map(Registries.ITEM::getId)
				.toList();

		buf.writeVarInt(bucketIds.size());
		for (Identifier id : bucketIds) {
			buf.writeIdentifier(id);
		}

		ServerPlayNetworking.send(player, BUCKET_STACK_PACKET, buf);
	}


	// For the bucket mixins
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
