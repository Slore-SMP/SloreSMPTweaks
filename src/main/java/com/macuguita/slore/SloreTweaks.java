package com.macuguita.slore;

import com.macuguita.slore.item.ReaperItem;
import com.macuguita.slore.mixin.buckets.ItemAccessor;
import com.macuguita.slore.mixin.reaper.LivingEntityAccessor;
import com.macuguita.slore.reg.SloreObjects;
import com.macuguita.slore.reg.SloreParticles;
import com.macuguita.slore.reg.SloreSounds;
import com.macuguita.slore.reg.SloreTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
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

public class SloreTweaks implements ModInitializer {
	public static final String MOD_ID = "slore";
	public static final String MOD_NAME = "Slore SMP Tweaks";
	public static final Identifier BUCKET_STACK_PACKET = id("sync_bucket_stacks");

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		SloreObjects.init();
		SloreParticles.init();
		SloreSounds.init();

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

	public static void processBuckets() {
		for (Item item : Registries.ITEM) {
			if (isBucket(item)) {
				((ItemAccessor) item).slore$setMaxCount(16);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static boolean isBucket(Item item) {
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

	public static void syncBucketStacksToPlayer(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();

		List<Identifier> bucketIds = Registries.ITEM.stream()
				.filter(SloreTweaks::isBucket)
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
