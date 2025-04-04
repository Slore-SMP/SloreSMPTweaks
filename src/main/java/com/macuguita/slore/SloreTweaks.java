package com.macuguita.slore;

import com.macuguita.lib.platform.registry.GuitaRegistries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.slore.item.ReaperItem;
import com.macuguita.slore.mixin.ItemAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
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

public class SloreTweaks implements ModInitializer {
	public static final String MOD_ID = "slore";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final GuitaRegistry<Item> ITEMS = GuitaRegistries.create(Registries.ITEM, MOD_ID);

	public static final GuitaRegistryEntry<ReaperItem> REAPER = ITEMS.register("reaper", () -> new ReaperItem(
			(float) Integer.MAX_VALUE, 1, ToolMaterials.NETHERITE, BlockTags.HOE_MINEABLE,
			new Item.Settings().maxCount(1).fireproof().maxDamage(-1)));

	public static final DefaultParticleType GHOST_PARTICLE = FabricParticleTypes.simple();

	public static final RegistryKey<DamageType> REAPER_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, SloreTweaks.id("reaper"));

	public static final TagKey<Item> BREAKABLE = TagKey.of(RegistryKeys.ITEM, id("breakable"));

	@Override
	public void onInitialize() {

		Registry.register(Registries.PARTICLE_TYPE, id("ghost"), GHOST_PARTICLE);
		((ItemAccessor) Items.WATER_BUCKET).slore$setMaxCount(16);
		((ItemAccessor) Items.LAVA_BUCKET).slore$setMaxCount(16);
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}

	public static boolean isUnbreakable(ItemStack stack) {
		return !stack.isEmpty() && stack.getMaxDamage() > 0 && !stack.isIn(BREAKABLE);
	}
}