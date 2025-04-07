package com.macuguita.slore;

import com.macuguita.lib.platform.registry.GuitaRegistries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.slore.item.ReaperItem;
import com.macuguita.slore.mixin.buckets.ItemAccessor;
import com.macuguita.slore.mixin.reaper.LivingEntityAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SloreTweaks implements ModInitializer {
	public static final String MOD_ID = "slore";

	public static final Logger LOGGER = LoggerFactory.getLogger("Slore SMP Tweaks");

	public static final GuitaRegistry<Item> ITEMS = GuitaRegistries.create(Registries.ITEM, MOD_ID);

	public static final GuitaRegistryEntry<ReaperItem> REAPER = ITEMS.register("reaper", () -> new ReaperItem(
			(float) Integer.MAX_VALUE, 1, ToolMaterials.NETHERITE, BlockTags.HOE_MINEABLE,
			new Item.Settings().maxCount(1).fireproof().maxDamage(-1)));

	public static final DefaultParticleType GHOST_PARTICLE = FabricParticleTypes.simple();

	public static final RegistryKey<DamageType> REAPER_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, SloreTweaks.id("reaper"));

	public static final TagKey<Item> BREAKABLE = TagKey.of(RegistryKeys.ITEM, id("breakable"));
	public static final TagKey<Item> BUCKET = TagKey.of(RegistryKeys.ITEM, id("bucket"));
	public static final TagKey<Item> BUCKET_BLACKLIST = TagKey.of(RegistryKeys.ITEM, id("bucket_blacklist"));

	@Override
	public void onInitialize() {
		ITEMS.init();
		Registry.register(Registries.PARTICLE_TYPE, id("ghost"), GHOST_PARTICLE);

		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity instanceof LivingEntity livingEntity && livingEntity.getMainHandStack().getItem() instanceof ReaperItem) {
				killedEntity.deathTime = 0;
				((LivingEntityAccessor) killedEntity).slore$setExperienceDroppingDisabled(false);
				ReaperItem.spawnGhostParticle(killedEntity);
				killedEntity.remove(Entity.RemovalReason.KILLED);
			}
		});

		List<Item> bucketList = Registries.ITEM.stream()
				.filter(item -> {
					if (item instanceof EntityBucketItem || item.getRegistryEntry().isIn(BUCKET_BLACKLIST)) {
						return false;
					}
					if (item.getRegistryEntry().isIn(BUCKET)) {
						return true;
					}
					Identifier id = Registries.ITEM.getId(item);
					return id.getPath().endsWith("_bucket");
				})
				.toList();

		for (Item bucket : bucketList) {
			try {
				((ItemAccessor) bucket).slore$setMaxCount(16);
			} catch (ClassCastException e) {
				LOGGER.error("Failed to set {}'s stack size", Registries.ITEM.getId(bucket));
			}
		}
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}

	public static boolean isUnbreakable(ItemStack stack) {
		return !stack.isEmpty() && stack.getMaxDamage() > 0 && !stack.isIn(BREAKABLE);
	}
}