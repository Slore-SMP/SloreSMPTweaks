package net.macuguita.slore.item.custom;

import net.macuguita.slore.utils.ModTags;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PresentItem extends Item {
    private static final int COOLDOWN_TICKS = 1200;
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public PresentItem(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient || user.isSneaking()) {
            return super.use(world, user, hand);
        }

        user.getItemCooldownManager().set(this, 20);

        ItemStack itemStack = user.getStackInHand(hand);

        Random random = new Random();
        List<Item> items = Registries.ITEM.stream()
                .filter(item -> {
                    String itemName = Registries.ITEM.getKey(item).toString();
                    return !item.getRegistryEntry().isIn(ModTags.Items.PRESENT_BLACKLIST)
                            && !itemName.contains("book")
                            && !itemName.contains("command_block")
                            && !itemName.contains("creative")
                            && !itemName.contains("debug")
                            && !itemName.contains("diamond")
                            && !itemName.contains("egg")
                            && !itemName.contains("ender")
                            && !itemName.contains("map")
                            && !itemName.contains("netherite")
                            && !itemName.contains("nether_star")
                            && !itemName.contains("ore")
                            && !itemName.contains("shulker")
                            && !itemName.contains("spawn_egg")
                            && !itemName.contains("spawn_keg")
                            && !itemName.contains("worldshaper")
                            && !itemName.contains("_head")
                            && !itemName.contains("dragon")
                            && !itemName.contains("light");
                })
                .toList();

        Item randomItem = items.get(random.nextInt(items.size()));
        ItemStack spawnedItem = new ItemStack(randomItem);

        world.spawnEntity(new ItemEntity(world, user.getX(), user.getY(), user.getZ(), spawnedItem));

        if (!user.isCreative()) {
            itemStack.decrement(1);
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("item.slore_tweaks.present.tooltip").setStyle(Style.EMPTY.withColor(Formatting.RED).withItalic(true)));
    }
}
